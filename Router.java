import java.util.EmptyStackException;

public class Router {
    private final int CAPACITY = 10;

    private Packet[] router;
    private int rear;
    private int front;

    public Router(){
        router = new Packet[CAPACITY];
        rear = -1;
        front = -1;
    }

    public void enqueue(Packet p){
        if((rear + 1) % CAPACITY == front){
            throw new IllegalStateException("Full queue.");
        }

        if(front == -1){
            front = 0;
            rear = 0;
        }

        else{
            rear = (rear + 1)%CAPACITY;
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

        front = (front + 1)%CAPACITY;
        return temp;
    }

    public Packet peek(){
        return router[rear];
    }

    public int size(){
        if(front <= rear){
            return (rear + 1) - front;
        }
        else{
            return (rear + CAPACITY  + 1) - front;
        }
    }

    public boolean isEmpty(){
        return front == -1;
    }

    public String toString(){
        String ans = "R";
        return ans;
    }

    // public static int sendPacketTo(Collection routers){
    //     int mostFree = 0;

    //     for (int i = 0; i < routers.size(); i++){
            
    //     }
    // }
}
