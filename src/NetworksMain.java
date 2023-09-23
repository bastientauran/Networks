import model.network.MacAddress;

public class NetworksMain {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");

        MacAddress address = new MacAddress(new int[]{0,30,9,7,5,6});

        System.out.println(address);
    }
}
