package model.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import model.logger.LogSeverity;
import model.logger.Logger;
import model.network.Packet;
import model.simulator.Simulator;

/**
 * Class used to trace all packets transmissions
 * It uses Singleton design pattern.
 * 
 * @author Bastien Tauran
 * @version 1.0
 * 
 * @see Layer
 */
public class PacketTracer {

    /**
     * Instance of PacketTracer
     */
    private static PacketTracer instance;

    /**
     * Instance writing logs to file
     */
    private PrintWriter writer;

    /**
     * Private constructor
     */
    private PacketTracer() {
        String scenarioName = Simulator.getInstance().getScenarioName();
        if (scenarioName != "") {
            String path = "out/simulation" + "/" + scenarioName + "/PacketTrace.log";
            Logger.getInstance().log(LogSeverity.INFO, "Create PacketTracer on file " + path);
            try {
                File file = new File(path);
                file.getParentFile().mkdirs();
                this.writer = new PrintWriter(new FileWriter(file));
            } catch (IOException e) {
                Logger.getInstance().log(LogSeverity.CRITICAL, "Cannot open " + path + " for PacketTracer");
            }
        } else {
            this.writer = null;
        }
    }

    /**
     * Get the instance of PacketTracer. Construct it if not already created.
     * 
     * @return The instance of PacketTracer
     */
    public static PacketTracer getInstance() {
        if (instance == null) {
            instance = new PacketTracer();
        }
        return instance;
    }

    /**
     * Write a new line to the log file, corresponding to a packet.
     * Write the details of the node calling the tracer, and the details of the
     * packet
     * 
     * @param nodeId ID of the node tracing the packet
     * @param layer  Layer of the node that calls the trace
     * @param event  Event on the packet
     * @param packet The packet to trace
     */
    public void tracePacket(int nodeId, Layer layer, PacketEvent event, Packet packet) {
        if (this.writer != null) {
            this.writer.write(Simulator.getInstance().getCurrentTime().toStringWithPrecision(6) + " ");
            this.writer.write(nodeId + " " + layer + " " + event + " ");
            this.writer.write(packet.formatToTrace() + "\n");
        }
    }

    /**
     * Write header of the trace
     */
    public void initTrace() {
        this.writer.write("Packet trace\n");
        this.writer.write("============\n");
        this.writer.write("Format: Time NodeId Layer Event PacketId HeadersDetails Payload\n\n");
    }

    /**
     * Close the PrinteWriter writing to file
     */
    public void closeTrace() {
        this.writer.close();
        this.writer = null;
    }

    /**
     * Destroy the instance of PacketTracer
     */
    public static void destroy() {
        instance = null;
    }

}
