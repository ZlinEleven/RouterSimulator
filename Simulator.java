import java.util.EmptyStackException;
import java.util.Scanner;

public class Simulator {
    final static int MAX_PACKETS =  3;

    private Router dispatcher;
    private Router[] routers;
    private int totalServiceTime = 0;
    private int totalPacketsArrived = 0;
    private int packetsDropped = 0;
    private double arrivalProb;
    private int intRouter;
    private int maxBufferSize;
    private int minPacketSize;
    private int maxPacketSize;
    private int bandWidth;
    private static int duration;    


    public Simulator(){
        Scanner scan = new Scanner(System.in);

        System.out.println("Starting simulator...\n");

        System.out.print("Enter the number of Intermediate routers: ");
        intRouter = Integer.parseInt(scan.nextLine());

        System.out.print("\nEnter the arrival probablity of a packet: ");
        arrivalProb = Double.parseDouble(scan.nextLine());

        System.out.print("\nEnter the maximum buffer size of a router: ");
        maxBufferSize = Integer.parseInt(scan.nextLine());

        System.out.print("\nEnter the minimum size of a packet: ");
        minPacketSize = Integer.parseInt(scan.nextLine());

        System.out.print("\nEnter the maximum size of a packet: ");
        maxPacketSize = Integer.parseInt(scan.nextLine());

        System.out.print("\nEnter the bandwidth size: ");
        bandWidth = Integer.parseInt(scan.nextLine());

        System.out.print("\nEnter the simulation duration: ");
        duration = Integer.parseInt(scan.nextLine());

        System.out.println();

        // Initializing routers
        dispatcher = new Router(maxBufferSize);
        routers = new Router[intRouter];
        // Intermediate routers
        for(int i = 0; i < routers.length; i++){
            routers[i] = new Router(maxBufferSize);
        }
    }

    public double simulate(int currentTime) throws Exception{
        System.out.println("Time: " + currentTime);
        
        int dispatcherStart = dispatcher.size();

        generatePackets(currentTime);
        
        if(dispatcher.size() == dispatcherStart){
            System.out.println("No packets arrived.");
        }

        dispatcherToIntermediate();

        decrementPackets(currentTime);

        for(int i = 0; i < routers.length; i++){
            System.out.println(routers[i]);
        }

        System.out.println();

        return 0;
    }

    public void generatePackets(int currentTime){
        for(int i = 0; i < MAX_PACKETS; i++){
            if(generateArrival(arrivalProb)){
                int packetSize = randInt(minPacketSize, maxPacketSize);
                Packet newPacket = new Packet(packetSize, currentTime);
                System.out.println("Packet " + newPacket.getId() + " arrives at the dispatcher with size " + newPacket.getPacketSize() + ".");
                dispatcher.enqueue(newPacket);
            }
        }
    }
    public void dispatcherToIntermediate() throws Exception{
        while(!dispatcher.isEmpty()){
            int targetRouter = Router.sendPacketTo(routers);
            Packet targetPacket = dispatcher.dequeue();
            routers[targetRouter].enqueue(targetPacket);
            System.out.println("Packet " + targetPacket.getId() + " sent to Router " + (targetRouter + 1) + ".");
        }
    }

    public void decrementPackets(int currentTime) throws Exception{
        int destinationCount = 0;
        for(int i = 0; i < routers.length; i++){
            if(!routers[i].isEmpty()){
                Packet target = routers[i].peek();
                target.setTimeToDest(target.getTimeToDest() - 1);
                if(target.getTimeToDest() == 0 && destinationCount < bandWidth){
                    try{
                        Packet temp = routers[i].dequeue();
                        System.out.println("Packet " + temp.getId() + " has successfully reached its destination: +" + (currentTime - temp.getTimeArrive()));
                        destinationCount += 1;
                    }
                    catch(EmptyStackException e){
                        System.out.println("Empty stack, Cannot dequeue.");
                    }
                }
            }
        }
    }

    public boolean generateArrival(double arrivalProb){
        return Math.random() < arrivalProb;
    }

    private int randInt(int minVal, int maxVal){
        return (int) (Math.random() * (maxVal - minVal + 1)) + minVal;
    }

    public static void main(String[] args) throws Exception{
        Scanner scan = new Scanner(System.in);

        String selection = "y";

        while(selection != "n"){
            

            Simulator simulator = new Simulator();

            for(int i = 1; i <= duration; i++){
                simulator.simulate(i);
            }

            System.out.print("Do you want to try another simulation? (y/n): ");
            selection = scan.nextLine();
            System.out.println();
        
        }
    }
}
