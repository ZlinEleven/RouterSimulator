/*Zhenbin Lin, 114866923, Recitation section 04*/

import java.nio.BufferOverflowException;
import java.util.EmptyStackException;

/**
 * This class represents a router object. The router is a circular queue that takes in packets and processes them
 * @author zhenb
 */
public class Router {
    private static int routerCount = -1;

    private Packet[] router;

    private int id;
    private int rear;
    private int front;
    private int capacity;

    /**
     * Constructor for the router object. Initializes an array with capacity: capacity.
     * @param capacity The length to initialize the array to
     */
    public Router(int capacity){
        routerCount += 1;
        id = routerCount;
        router = new Packet[capacity];
        this.capacity = capacity;
        rear = -1;
        front = -1;
    }

    /**
     * Adds a packet object at the rear of the array. 
     * @throws Exception If the router is full, an IllegalStateException will be thrown.
     * @param p The packet object to be added
     */
    public void enqueue(Packet p) throws Exception{
        if((rear + 1) % capacity == front){
            throw new IllegalStateException();
        }

        if(front == -1){
            front = 0;
            rear = 0;
        }

        else{
            rear = (rear + 1)%capacity;
        }

        router[rear] = p;
    }

    /**
     * Moves the front pointer of the array forward, the previous packet pointed by the object will be ignored
     * @return The packet object the front pointer was previouslt pointing to
     * @throws Exception If router is empty, an EmptyStackException will be thrown
     */
    public Packet dequeue() throws Exception{
        if(isEmpty()){
            throw new EmptyStackException();
        }

        Packet temp = router[front];
        if(front == rear){
            front = -1;
            rear = -1;
        }
        else{
            front = (front + 1)%capacity;
        }
        return temp;
    }

    /**
     * Gives the packet object referenced by the front pointer
     * @return The packet object referenced by the front pointer
     */
    public Packet peek(){
        return router[front];
    }

    /**
     * The amount of packets recognized within the router
     * @return The amount of packets recognized
     */
    public int size(){
        if(front == -1){
            return 0;
        }
        else if(rear >= front){
            return rear + 1 - front;
        }
        else{
            return rear + capacity  + 1- front;
        }
    }

    /**
     * Shows whether the router contains a packet or not
     * @return A true if the router contains at least one packet, false otherwise
     */
    public boolean isEmpty(){
        return front == -1;
    }

    /**
     * A string representation of the router as well as all the packets within it
     */
    public String toString(){
        String ans = "R" + id + ": {";
        if(!isEmpty()){
            ans += router[front];
        }
        for(int i = 1; i < size(); i++){
            ans += ", " + router[(front + i)%capacity];
        }
        return ans + "}";
    }

    /**
     * A static method used to send packets from the dispatcher to an intermediate router with the least buffer
     * @param routers An array of all the intermediate routers
     * @return The index of the intermediate router with the least buffer
     * @throws Exception If none of the intermediate routers have any buffer left, a BufferOverflowException will be thrown
     */
    public static int sendPacketTo(Router[] routers)throws Exception{
        int mostFree = -1;
        int leastBuffer = routers[0].capacity;

        for (int i = 0; i < routers.length; i++){
            // System.out.println("Router " + (i + 1) + " size: " + routers[i].size());
            if(routers[i].size() < leastBuffer && (mostFree == -1 || routers[i].size() < routers[mostFree].size())){
                mostFree = i;
                // System.out.println("Most free: " + mostFree);
                leastBuffer = routers[i].size();
            }
        }

        if(mostFree == -1){
            throw new BufferOverflowException();
        }

        return mostFree;
    }
}
