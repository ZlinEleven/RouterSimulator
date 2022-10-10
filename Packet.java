public class Packet{
    public static int packetCount = 0;

    private int id;
    private int packetSize;
    private int timeArrive;
    private int timeToDest;

    public Packet(int packetSize, int timeArrive){
        packetCount += 1;
        id = packetCount;
        this.packetSize = packetSize;
        this.timeArrive = timeArrive;
        timeToDest = packetSize/100;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getPacketSize(){
        return packetSize;
    }

    public void setPacketSize(int size){
        packetSize = size;
    }

    public int getTimeArrive(){
        return timeArrive;
    }

    public void setTimeArrive(int time){
        timeArrive = time;
    }

    public int getTimeToDest(){
        return timeToDest;
    }

    public void setTimeToDest(int time){
        timeToDest = time;
    }

    public String toString(){
        return "[" + getId() + ", " + getTimeArrive() + ", " + getTimeToDest() + "]";
    }
}