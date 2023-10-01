#python3 doc\exportUML.py

import os
import glob
from enum import Enum
import argparse

parser = argparse.ArgumentParser()
parser.add_argument("--ignorePackage", type=str, nargs='+', help="packages to ignore")

args = parser.parse_args()
packagesToIgnore = vars(args)["ignorePackage"] or []

class ClassType(Enum):
    CLASS = "class"
    INTERFACE = "interface"
    ENUM = "enum"

class Visibility(Enum):
    PRIVATE = '-'
    PROTECTED = '#'
    PACKAGE = '~'
    PUBLIC = '+'

class Class:
    name = ""
    classType = ClassType.CLASS
    visibility = Visibility.PUBLIC
    additional = []
    attributes = []
    methods = []
    enumTypes = []

    def __init__(self):
        self.name = ""
        self.additional = []
        self.attributes = []
        self.methods = []
        self.enumTypes = []

class Attribute:
    name = ""
    visibility = Visibility.PUBLIC
    attributeType = ""
    additional = []

    def __init__(self):
        self.name = ""
        self.attributeType = ""
        self.additional = []

class Method:
    name = ""
    visibility = Visibility.PUBLIC
    returnType = ""
    additional = []
    arguments = []

    def __init__(self):
        self.name = ""
        self.returnType = ""
        self.additional = []
        self.arguments = []

def writeLine(s):
    outputUml.write(s + '\n')

def cleanLine (s):
    while " (" in s:
        s = s.replace(" (","(")
    while "( " in s:
        s = s.replace("( ","(")
    while " )" in s:
        s = s.replace(" )",")")
    while ") " in s:
        s = s.replace(") ",")")
    while " <" in s:
        s = s.replace(" <","<")
    while "< " in s:
        s = s.replace("< ","<")
    while " >" in s:
        s = s.replace(" >",">")
    while " ," in s:
        s = s.replace(" ,",",")
    while ", " in s:
        s = s.replace(", ",",")
    return s

def getPaths():
    l = set ()
    for filename in glob.iglob("src/**/**/**/**/**", recursive=True):
        for i in packagesToIgnore:
            if i in filename:
                break
        else:
            if filename.split('.')[-1] == "java":
                l.add(filename)
    return l

def parseVisibility(s):
    if "public" in s:
        return Visibility.PUBLIC
    if "protected" in s:
        return Visibility.PROTECTED
    if "private" in s:
        return Visibility.PRIVATE
    return Visibility.PACKAGE

def parseAdditional(s):
    l = []
    if "abstract" in s.split():
        l.append("abstract")
    if "final" in s.split():
        l.append("final")
    if "static" in s.split():
        l.append("static")
    return l

def parseArguments(s):
    if not s.strip():
        return []
    l = []
    elts = s.strip().split(',')
    for e in elts:
        e = e.strip()
        l.append((" ".join(e.split()[:-1]), e.split()[-1]))
    return l

classes = []
heritage = []
implements = []
associations = {}

for f in getPaths():
    src = open(f, 'r')
    nbAccol = 0
    inComment = False
    for line in src:
        line = cleanLine(line.rstrip())
        if "/*" in line:
            inComment = True
            continue
        if "*/" in line:
            inComment = False
            continue
        if inComment:
            continue
        if nbAccol == 0 and ("class" in line or "enum" in line or "interface" in line):
            t = ClassType.CLASS
            if "enum" in line:
                t = ClassType.ENUM
            if "interface" in line:
                t = ClassType.INTERFACE
            lineParsed = line.replace('{','').strip()
            index = lineParsed.split().index(t.value)
            c = Class()
            className = lineParsed.split()[index + 1]
            c.name = className
            c.classType = t
            c.visibility = parseVisibility(lineParsed)
            c.additional = parseAdditional(lineParsed)
            classes.append(c)
            if "extends" in lineParsed.split():
                index = lineParsed.split().index("extends")
                heritage.append((className, lineParsed.split()[index+1]))
            if "implements" in lineParsed.split():
                index = lineParsed.split().index("implements")
                for elt in lineParsed.split()[index+1].split(','):
                    implements.append((className, elt))
        if nbAccol == 1 \
                and classes[-1].classType == ClassType.ENUM \
                and (';' in line or ',' in line) \
                and (line.count('{') == line.count('}')) \
                and len(line.split('(')[0].strip().split()) == 1:
            classes[-1].enumTypes.append(line.replace(';','').replace(',','').split('(')[0].strip())
            continue
        if nbAccol == 1 and '(' in line and not '=' in line:
            lineParsed = line.replace('{','').strip()
            m = Method()
            signature = lineParsed.replace('(', " (").split('(')[0]
            args = lineParsed.replace('(', " (").split('(')[1].split(')')[0]
            m.name = signature.split()[-1]
            if len(signature.split()) > 1:
                m.returnType = signature.split()[-2]
                if m.returnType in ["public", "private", "protected"]:
                    m.returnType = ""
            m.visibility = parseVisibility(signature)
            m.additional = parseAdditional(signature)
            m.arguments = parseArguments(args)
            classes[-1].methods.append(m)
        if nbAccol == 1 and ';' in line and (not '(' in line or '=' in line):
            lineParsed = line.replace(';','').split('=')[0].strip()
            a = Attribute()
            a.name = lineParsed.split()[-1]
            if "final" in parseAdditional(lineParsed):
                if '=' in line:
                    a.name += " = " + line.replace(';','').split('=')[1].strip()
            a.visibility = parseVisibility(lineParsed)
            a.attributeType = lineParsed.split()[-2]
            a.additional = parseAdditional(lineParsed)
            classes[-1].attributes.append(a)
        nbAccol += line.count('{')
        nbAccol -= line.count('}')
    src.close()

classesNames = [c.name for c in classes]

for c in classes:
    print(c.name)
    for a in c.attributes:
        print("    ", a.name)
    for m in c.methods:
        print("    ", m.name, m.arguments)

outputUml = open(os.path.dirname(__file__) + os.sep + "outputUml.txt", 'w')

writeLine("@startUml")

for c in classes:
    if c.classType == ClassType.ENUM:
        name = c.name
        if '<' in c.name:
            name = c.name.split('<')[0].strip()
        writeLine(c.classType.value + " " + name + "{")
        for e in c.enumTypes:
            writeLine(e)
        writeLine('}')
    else:
        ext = ""
        if "abstract" in c.additional:
            ext = "abstract "
        writeLine(ext + c.classType.value + " " + c.name)
    for a in c.attributes:
        name = c.name
        if '<' in c.name:
            name = c.name.split('<')[0].strip()
        for c2 in classesNames:
            if c2 in a.attributeType and a.attributeType != c.name:
                t = a.attributeType
                nb = '1'
                if '<' in t:
                    t = t.split('<')[1].split('>')[0].split(',')[-1].strip()
                    nb = "0..*"
                if "static" in a.additional:
                    writeLine(name + " --> \"__" + nb + " " + a.visibility.value + a.name + "__\" " + t)
                else:
                    writeLine(name + " --> \"" + nb + " " + a.visibility.value + a.name + "\" " + t)
                break
        else:
            ext = ""
            if "static" in a.additional:
                ext = "{static} "
            writeLine(name + " : " + ext + a.visibility.value + " " + a.attributeType + " " + a.name)
    for m in c.methods:
        s = c.name
        if '<' in c.name:
            s = c.name.split('<')[0].strip()
        s += " : " + m.visibility.value
        if "abstract" in m.additional:
            s += "{abstract} "
        if "static" in m.additional:
            s += "{static} "
        s += " " + m.name
        s += '(' + ",".join([a + " "  + b for a,b in m.arguments]) + ')'
        if m.returnType:
            s += " : " + m.returnType
        writeLine(s)
    writeLine("")

writeLine("")
for child, mother in heritage:
    if mother in classesNames:
        writeLine(child + " --|> " + mother)

writeLine("")
for child, mother in implements:
    if mother in classesNames:
        writeLine(child + " ..|> " + mother)

writeLine("@endUml")

outputUml.close()
