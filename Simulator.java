import java.util.Scanner;

public class Simulator {
    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);
        System.out.println("Starting simulator...\n");

        System.out.print("Enter the number of Intermediate routers: ");
        int intRouter = Integer.parseInt(scan.nextLine());

        System.out.print("\nEnter the arrival probablity of a packet: ");
        double arrivalProb = Double.parseDouble(scan.nextLine());

        System.out.print("\nEnter the maximum buffer size of a router: ");
        int bufferSize = Integer.parseInt(scan.nextLine());

        System.out.print("\nEnter the minimum size of a packet: ");
        int minPacketSize = Integer.parseInt(scan.nextLine());

        System.out.print("\nEnter the maximum size of a packet: ");
        int maxPacketSize = Integer.parseInt(scan.nextLine());

        System.out.print("\nEnter the bandwidth size: ");
        int bandwidthSize = Integer.parseInt(scan.nextLine());

        System.out.print("\nEnter the simulation duration: ");
        int duration = Integer.parseInt(scan.nextLine());

        System.out.println();
    }
}
