import java.util.*;
import java.net.InetAddress; 

public class Main{

    public static void main (String[] args){
	Scanner scanner = new Scanner (System.in);
	System.out.println("what's your name ?");
	String id = scanner.nextLine();
	System.out.println("port TCP ?");
	String port_tcp = scanner.nextLine();
	System.out.println("port UDP ?");
	String port_udp = scanner.nextLine();
	InetAddress IA = InetAddress.getLocalHost(); 
	String ip = IA.getHostAddress();
	String cmd = scanner.nextLine();
	String[] arr = cmd.split(" ");
	Entity me;
	switch (arr[0]){
	case "connect":
	    me = new Entity(id, port_udp, port_tcp);
	    ClientTcp cl=new ClientTcp(me, port_udp, ip);
	    int a=cl.clientTCP("connect");
	    if(a==0){
		//serveur tcp
		//serveur udp
		boolean quit = true;
		while(quit){
		    String cm = scanner.nextLine();

		}
	    }
	    break;
	case "create":
	    System.out.println("multi diff adress ?");
	    String multi_diff = scanner.nextLine();
	    System.out.println("multi diff port ?");
	    String port_diff = scanner.nextLine();
	    me = new Entity(id, port_udp, port_tcp, multi_diff, port_diff);
	    //serveur udp
	    //serveur tcp
	    break;
	case "dupl":
	    me = new Entity(id, port_udp, port_tcp);
	    
	    break;
	default : 
	    System.out.println("incompatible command");
	}
    }
}

