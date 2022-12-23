/**
 * This class represents a packet object which is sent through the three levels of routers
 * @author zhenb 
 */
public class Packet{
    public static int packetCount = 0;

    private int id;
    private int packetSize;
    private int timeArrive;
    private int timeToDest;

    /**
     * Constructor for a packet object
     * @param packetSize The size of the packet
     * @param timeArrive The time unit in the simulation the packet is generated
     */
    public Packet(int packetSize, int timeArrive){
        packetCount += 1;
        id = packetCount;
        this.packetSize = packetSize;
        this.timeArrive = timeArrive;
        timeToDest = packetSize/100;
    }

    /**
     * Gives the id of the packet
     * @return The id of the packet
     */
    public int getId(){
        return id;
    }

    /**
     * Sets the id of the packet
     * @param id The id the packet is set to
     */
    public void setId(int id){
        this.id = id;
    }

    /**
     * Gives the size of the packet
     * @return The size of the packet
     */
    public int getPacketSize(){
        return packetSize;
    }

    /**
     * Sets the size of the packet
     * @param size The size to set the packet to
     */
    public void setPacketSize(int size){
        packetSize = size;
    }

    /**
     * Gives the time unit in the simulation the packet was generated during
     * @return The time unit the packet was generated during
     */
    public int getTimeArrive(){
        return timeArrive;
    }

    /**
     * Sets the time in which the packet was generated
     * @param time The time to set the packet's arrival to
     */
    public void setTimeArrive(int time){
        timeArrive = time;
    }

    /**
     * Gives the amount of time units left to process before being sent to destination router
     * @return The amount of time units left to process
     */
    public int getTimeToDest(){
        return timeToDest;
    }

    /**
     * Sets the amount of time left for processing the pakcet
     * @param time The amount of processing time left to set the packet to
     */
    public void setTimeToDest(int time){
        timeToDest = time;
    }

    /**
     * Returns a string representation of the packet. Includes the id, arrival time, and processing time left
     */
    public String toString(){
        return "[" + getId() + ", " + getTimeArrive() + ", " + getTimeToDest() + "]";
    }
}