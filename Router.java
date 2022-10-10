import java.nio.BufferOverflowException;
import java.util.EmptyStackException;

public class Router {
    private static int routerCount = -1;

    private Packet[] router;

    private int id;
    private int rear;
    private int front;
    private int capacity;

    public Router(int capacity){
        routerCount += 1;
        id = routerCount;
        router = new Packet[capacity];
        this.capacity = capacity;
        rear = -1;
        front = -1;
    }

    public void enqueue(Packet p){
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
        // System.out.println("Front: " + front + " | Rear: " + rear);
        return temp;
    }

    public Packet peek(){
        return router[front];
    }

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

    public boolean isEmpty(){
        return front == -1;
    }

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
