package model.link;

import model.network.Packet;
import model.nodes.Interface;
import model.simulator.Schedulable;
import model.simulator.SchedulableMethod;
import model.simulator.Simulator;
import model.simulator.Time;

/**
 * Class representing a point to point link
 * 
 * @author Bastien Tauran
 * @version 1.0
 * @see Link
 */
public class PointToPointLink extends Link implements Schedulable {

    private class DirectionStruct {
        public Interface src;
        public Interface dst;
        boolean isTransmitting;

        /**
         * Create a new DirectionStruct
         */
        public DirectionStruct() {
            this.src = null;
            this.dst = null;
            this.isTransmitting = false;
        }
    }

    /**
     * Propagation delay of this link
     */
    private Time delay;

    /**
     * Bandwidth of this link in bytes per second
     */
    private long bandwidthBytesPerSecond;

    /**
     * Number of interfaces that can be connected to this link
     */
    private static final int NB_INTERFACES = 2;

    /**
     * Each direction of this link
     */
    private DirectionStruct[] directions;

    /**
     * Current number of interfaces connected
     */
    private int interfacesConnected;

    /**
     * Create a new PointToPointLink
     */
    public PointToPointLink() {
        this.delay = new Time(1, 0);
        this.bandwidthBytesPerSecond = 1000000L;
        this.directions = new DirectionStruct[] { new DirectionStruct(), new DirectionStruct() };
        this.interfacesConnected = 0;
    }

    public void attachInterface(Interface interf) {
        if (this.interfacesConnected >= PointToPointLink.NB_INTERFACES) {
            throw new IllegalStateException("Too many interfaces connected to this link");
        }
        this.directions[this.interfacesConnected].src = interf;
        this.interfacesConnected++;

        if (this.interfacesConnected == PointToPointLink.NB_INTERFACES) {
            this.directions[0].dst = this.directions[1].src;
            this.directions[1].dst = this.directions[0].src;
            this.directions[0].isTransmitting = false;
            this.directions[1].isTransmitting = false;
        }
    }

    /**
     * Start transmission of a packet
     * 
     * @param packet The packet to send
     * @param src    The source interface
     */
    @Override
    public void startTx(Packet packet, Interface src) {
        if (this.interfacesConnected != 2) {
            throw new IllegalStateException("Point to point link does not have both itnterfaces connected");
        }

        int direction = this.directions[0].src == src ? 0 : 1;

        Time transmissionDelay = this.getTransmissionDelay(packet);

        if (this.directions[direction].isTransmitting) {
            throw new IllegalStateException("Cannot start TX while a packet is already being transmitted");
        }
        this.directions[direction].isTransmitting = true;

        Simulator.getInstance().schedule(Simulator.getInstance().getCurrentTime().add(transmissionDelay), this,
                SchedulableMethod.POINT_TO_POINT_LINK__END_TX,
                packet, direction);
        Simulator.getInstance().schedule(Simulator.getInstance().getCurrentTime().add(this.delay), this,
                SchedulableMethod.POINT_TO_POINT_LINK__START_RX,
                packet, direction);
        Simulator.getInstance().schedule(
                Simulator.getInstance().getCurrentTime().add(transmissionDelay).add(this.delay), this,
                SchedulableMethod.POINT_TO_POINT_LINK__END_RX, packet, direction);
    }

    /**
     * Transmission of packet is finished
     * 
     * @param packet    The packet sent
     * @param direction The direction of the link used
     */
    public void endTx(Packet packet, int direction) {
        this.directions[direction].isTransmitting = false;
        this.directions[direction].src.endTx(packet);
    }

    /**
     * Start reception of a packet
     * 
     * @param packet    The packet to receive
     * @param direction The direction of the link used
     */
    public void startRx(Packet packet, int direction) {
        this.directions[direction].dst.startRx(packet);
    }

    /**
     * Reception of packet is complete
     * 
     * @param packet    The packet to receive
     * @param direction The direction of the link used
     */
    public void endRx(Packet packet, int direction) {
        this.directions[direction].dst.endRx(packet);
    }

    /**
     * Get propagation delay
     * 
     * @return The propagation delay
     */
    public Time getDelay() {
        return this.delay;
    }

    /**
     * Set the propagation delay
     * 
     * @param delay The propagation delay to set
     */
    public void setDelay(Time delay) {
        this.delay = delay;
    }

    /**
     * Get the link bandwidth
     * 
     * @return The link bandwidth in bytes per second
     */
    public long getBandwidthBytesPerSecond() {
        return this.bandwidthBytesPerSecond;
    }

    /**
     * Set the link bandwidth
     * 
     * @param bandwidthBytesPerSecond The link bandwidth in bytes per second
     */
    public void setBandwidthBytesPerSecond(long bandwidthBytesPerSecond) {
        this.bandwidthBytesPerSecond = bandwidthBytesPerSecond;
    }

    /**
     * Compute the transmission delay of a packet
     * 
     * @param packet The packet to send
     * @return The transmission delay
     */
    private Time getTransmissionDelay(Packet packet) {
        int packetTotalSize = packet.getTotalSizeBytes();

        double timeSeconds = 1.0 * packetTotalSize / this.bandwidthBytesPerSecond;

        int seconds = (int) timeSeconds;
        int nanoSeconds = (int) (1000000000 * (timeSeconds - seconds));

        return new Time(seconds, nanoSeconds);
    }

    @Override
    public void run(SchedulableMethod method, Object[] arguments) {
        switch (method) {
            case POINT_TO_POINT_LINK__END_TX: {
                endTx((Packet) arguments[0], (int) arguments[1]);
                break;
            }
            case POINT_TO_POINT_LINK__START_RX: {
                startRx((Packet) arguments[0], (int) arguments[1]);
                break;
            }
            case POINT_TO_POINT_LINK__END_RX: {
                endRx((Packet) arguments[0], (int) arguments[1]);
                break;
            }
            default: {
                throw new IllegalArgumentException("Unknow method for class PointToPointLink: " + method);
            }
        }
    }

}
