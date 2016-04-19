import java.util.*;
import java.net.InetAddress; 
import java.net.UnknownHostException;
import java.lang.Object;
import java.net.*;
import java.io.*;

public class Main{

    public static void main (String[] args){
	Scanner scanner = new Scanner (System.in);
	boolean arg = true;
	String id="";
	String ip="";
	String port_tcp="";
	String port_udp="";
	while(arg){
	    System.out.println("what's your name ?");
	    id = scanner.nextLine();
	    if(id.length()>8){
		System.out.println("id too long, 8 char max");
		break;
	    }
	    try{
		InetAddress IA = InetAddress.getLocalHost(); 
		ip = IA.getHostAddress();
	    }
	    catch(UnknownHostException e){
		System.out.println(e);
		e.printStackTrace();
	    }
	    System.out.println("port TCP ?");
	    port_tcp = scanner.nextLine();
	   
	    System.out.println("port UDP ?");
	    port_udp = scanner.nextLine();
	    try{
		ServerSocket server=new ServerSocket(Integer.parseInt(port_tcp));
	    }
	    catch(Exception e){
		System.out.println("Mauvais port tcp");
		break;
	    }
	    arg=false;
	}
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
		if(arr.length==3){
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
		//tests : wrong b=false
		is_connected=true;
		break;
	    case "dupl":
		if(arr.length==3){
		    System.out.println("multi diff adress ?");
		    String multi_diff2 = scanner.nextLine();
		    System.out.println("multi diff port ?");
		    String port_diff2 = scanner.nextLine();
		    me = new Entity(ip, id, port_udp, port_tcp, multi_diff2, port_diff2);
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
	    case "whos" :
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
	    default : 
		System.out.println("incompatible command");
	    }
	}
    }
}
