import java.util.*;
import java.net.InetAddress; 
import java.net.UnknownHostException;


public class Main{

    public static void main (String[] args){
	Scanner scanner = new Scanner (System.in);
	boolean arg = true;
	while(arg){
	    System.out.println("what's your name ?");
	    String id = scanner.nextLine();
	    System.out.println("port TCP ?");
	    String port_tcp = scanner.nextLine();
	    System.out.println("port UDP ?");
	    String port_udp = scanner.nextLine();
	    try{
		InetAddress IA = InetAddress.getLocalHost(); 
		String ip = IA.getHostAddress();
		Entity me;
		ClientTcp cl;
		int a;
		boolean is_connected=false;
		boolean b=true;
		while(b){
		    String cmd = scanner.nextLine();
		    String[] arr = cmd.split(" ");
		    switch (arr[0]){
		    case "connect":
			if(arr.size()==3){
			    me = new Entity(ip, id, port_udp, port_tcp);
			    cl=new ClientTcp(me, arr[1], arr[2]);
			    a=cl.clientTCP("connect");
			    if(a==0){
				//serveur tcp
				//serveur udp
				is_connected=true;
			    }else{
				System.out.println("wrong arguments");
				System.out.println();
				b=false;
			    }
			}else{
			    System.out.println("wrong arguments");
			}
			break;
		    case "create":
			System.out.println("multi diff adress ?");
			String multi_diff = scanner.nextLine();
			System.out.println("multi diff port ?");
			String port_diff = scanner.nextLine();
			me = new Entity(ip, id, port_udp, port_tcp, multi_diff, port_diff);
			//serveur udp
			//serveur tcp
			//tests : wrong args b=false
			is_connected=true;
			break;
		    case "dupl":
			if(arr.size()==3){
			    me = new Entity(ip, id, port_udp, port_tcp);
			    cl=new ClientTcp(me, arr[1], arr[2]);
			    a=cl.clientTCP("dupl");
			    if(a==0){
				//serveur tcp
				//serveur udp
				is_connected=true;
			    }else{
				System.out.println("wrong arguments");
				System.out.println();
				b=false;
			    }
			}else{
			    System.out.println("wrong arguments");
			}
			break;
		    case "who's" :
			if(is_connected=true){
			    //blabla
			}else{
			    System.out.println("use connect, create or dupl");
			}
			break;
		    case "quit ring" :
			is_connected=false;
			me=null;
			//serveurs=null
			break;
		    case "quit all" : 
			b=false;
			arg=false;
			break;
		    default : 
			System.out.println("incompatible command");
		    }
		}
	     }
	    catch(UnknownHostException e){
		System.out.println(e);
		e.printStackTrace();
	    }
	}
    }
}
