/*Zhenbin Lin, 114866923, Recitation section 04*/

import java.nio.BufferOverflowException;
import java.util.EmptyStackException;
import java.util.Scanner;

/**
 * This class contains the main method which is a menu driven application.
 * The program prompts the user for simulation parameters to run the router
 * simulation on. The user can also input "n" to terminate the program
 * @author zhenb
 */
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

    /**
     * The constructor for the Simulator class. The constructor prompts the user for inputs and initializes the dispatcher and intermediate routers accordingly
     */
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

        if(maxPacketSize < minPacketSize){
            throw new IllegalArgumentException("\nMaximum size cannot be less than minimum size. Please try again.\n");
        }

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

    /**
     * This method simulates the router simulation for a set amount of simulation time units
     * @return The average time packets sent to the destination spent in the router network
     * @throws Exception Methods used within this method contains exceptions
     */
    public double simulate() throws Exception{
        for(int currentTime = 1; currentTime <= duration; currentTime++){
            decrementPackets();
            System.out.println("Time: " + currentTime);
            
            int dispatcherStart = dispatcher.size();

            generatePackets(currentTime);
            
            if(dispatcher.size() == dispatcherStart){
                System.out.println("No packets arrived.");
            }

            dispatcherToIntermediate();

            sentToDestination(currentTime);

            for(int i = 0; i < routers.length; i++){
                System.out.println(routers[i]);
            }

            System.out.println();
        }

        return ((double) totalServiceTime)/totalPacketsArrived;
    }

    /**
     * Generates the packet and sends them to the dispatcher router. Each time unit can only generate up ot three packets
     * @param currentTime The time unit in the simulation. This parameter is used to get the generated packet's arrival time
     * @throws Exception If the dispatcher is full, the generated packet will be dropped and an IllegalStateException will be thrown
     */
    public void generatePackets(int currentTime) throws Exception{
        for(int i = 0; i < MAX_PACKETS; i++){
            if(generateArrival(arrivalProb)){
                int packetSize = randInt(minPacketSize, maxPacketSize);
                Packet newPacket = new Packet(packetSize, currentTime);
                System.out.println("Packet " + newPacket.getId() + " arrives at the dispatcher with size " + newPacket.getPacketSize() + ".");
                dispatcher.enqueue(newPacket);
            }
        }
    }

    /**
     * Sends the all the packets within the dispatcher to the intermediate router with the least buffer
     */
    public void dispatcherToIntermediate() throws Exception{
        while(!dispatcher.isEmpty()){
            int targetRouter = 0;
            Packet targetPacket = dispatcher.dequeue();

            try{
                targetRouter = Router.sendPacketTo(routers);
                routers[targetRouter].enqueue(targetPacket);
                System.out.println("Packet " + targetPacket.getId() + " sent to Router " + (targetRouter + 1) + ".");
            }
            catch(IllegalStateException e){
                System.out.println("Network is congested. Packet " + targetPacket.getId() + " is dropped.");
                packetsDropped += 1;
            }
            catch(BufferOverflowException e){
                System.out.println("Network is congested. Packet " + targetPacket.getId() + " is dropped.");
                packetsDropped += 1;
            }
        }
    }

    /**
     * Goes through the first packets in all the intermediate routers and decrements the processing time by 1
     */
    public void decrementPackets(){
        for(int i = 0; i < routers.length; i++){
            if(!routers[i].isEmpty()){
                Packet target = routers[i].peek();
                target.setTimeToDest(target.getTimeToDest() - 1);
            }
        }
    }

    /**
     * Checks if any packets in the intermediate routers are ready to be sent to the destination router. Only a max of "bandWidth" can be sent
     * during each time unit
     * @param currentTime The current time unit in the simulation
     */
    public void sentToDestination(int currentTime) throws Exception{
        int destinationCount = 0;
        for(int i = 0; i < routers.length; i++){
            if(!routers[i].isEmpty()){
                Packet target = routers[i].peek();
                if(target.getTimeToDest() <= 0 && destinationCount < bandWidth){
                    try{
                        Packet temp = routers[i].dequeue();
                        System.out.println("Packet " + temp.getId() + " has successfully reached its destination: +" + (currentTime - temp.getTimeArrive()));
                        totalServiceTime += currentTime - temp.getTimeArrive();
                        totalPacketsArrived += 1;
                        destinationCount += 1;
                    }
                    catch(EmptyStackException e){
                        System.out.println("Empty stack, Cannot dequeue.");
                    }
                }
            }
        }
    }

    /**
     * Determines whether a packet should be generated or not. True means a packet will be generated, and false means a packet won't be generated
     * @param arrivalProb The probability to compare Math.random() to
     * @return True of Math.random() is less than arrivalProb, false otherwise
     */
    public boolean generateArrival(double arrivalProb){
        return Math.random() < arrivalProb;
    }

    /**
     * Generates a random integer within minVal and maxVal
     * @param minVal The lower bound of the number generation
     * @param maxVal The upper bound of the number generation
     * @return A random integer within minval and maxVal
     */
    private int randInt(int minVal, int maxVal){
        return (int) (Math.random() * (maxVal - minVal + 1)) + minVal;
    }

    /**
     * Prints out the summary statement of the simulation
     * @param averageServeTime The average time packets that were successfully processed spent in the router network
     */
    private void printEnd(double averageServeTime){
        System.out.println("Simulation ending...");
        System.out.println("Total service time: " + totalServiceTime);
        System.out.println("Total packets served: " + totalPacketsArrived);
        System.out.println("Average service time per packet: " + String.format("%.2f", averageServeTime));
        System.out.println("Total packets dropped: " + packetsDropped);
        System.out.println();
    }

    public static void main(String[] args) throws Exception{
        Scanner scan = new Scanner(System.in);

        String selection = "y";

        while(!selection.equals("n")){

            try{
                Simulator simulator = new Simulator();
                double averageServeTime = simulator.simulate();

                simulator.printEnd(averageServeTime);
            }
            catch(IllegalArgumentException e){
                System.out.println(e.getMessage());
            }


            System.out.print("Do you want to try another simulation? (y/n): ");
            selection = scan.nextLine();
            System.out.println();
        
        }

        scan.close();
    }
}
