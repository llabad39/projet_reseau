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
	    }else{
		break;
	    }
	}

	while(arg){
	    System.out.println("port UDP ?");
	    port_udp = scanner.nextLine();
	    try{
		DatagramSocket dso=new DatagramSocket(Integer.parseInt(port_udp));
		break;
	    }	
	    catch(Exception e){
		System.out.println("Mauvais port udp");
	    }
	}

	while(arg){
	    System.out.println("port TCP ?");
	    port_tcp = scanner.nextLine();
	    if(!port_tcp.equals(port_udp)){
		try{
		    ServerSocket server=new ServerSocket(Integer.parseInt(port_tcp));
		    break;
		}
		catch(Exception e){
		    System.out.println("Mauvais port tcp");
		}
	    }else{
		System.out.println("Mauvais port tcp");
	    }
	}
 
	Entity me=new Entity();
	try{
	    InetAddress IA = InetAddress.getLocalHost(); 
	    ip =me.fill_ip(IA.getHostAddress());
	    System.out.println(ip);
	    //System.out.println(ip.hashCode());

	}
	catch(UnknownHostException e){
	    System.out.println(e);
	    e.printStackTrace();
	}
	catch(IpException e){
	    System.out.println(e);
	    e.printStackTrace();
	}
	ClientTcp cl;
	ServeurUdp su=new ServeurUdp(me);
	int a;
	boolean is_connected=false;
	boolean b=true;
	while(b){
	    String cmd = scanner.nextLine();
	    String[] arr = cmd.split(" ");
	    Mess m;
	    switch (arr[0]){
	    case "connect":
		if(is_connected==false){
		    if(arr.length==3){

			me = new Entity(ip, id, port_udp, port_tcp);
			cl=new ClientTcp(me,arr[1], arr[2]);
			a=cl.clientTCP("connect");
			if(a==0){
			    ServeurTcp s = new ServeurTcp(me);
			    Mythread mt = new Mythread(s);
			    mt.run();
			    is_connected=true;
			}else{
			    System.out.println("wrong arguments");
			}
		    }else{
			System.out.println("wrong arguments");
		    }
		}else{
		    System.out.println("You are already login");
		}
		break;
	    case "create":
		if(is_connected=false){
		    System.out.println("multi diff adress ?");
		    String multi_diff = scanner.nextLine();
		    System.out.println("multi diff port ?");
		    String port_diff = scanner.nextLine();
		    me = new Entity(ip, id, port_udp, port_tcp, multi_diff, port_diff);
		    //serveur udp
		    //serveur tcp
		    //tests : wrong b=false
		    is_connected=true;
		}else{
		    System.out.println("You are already login");
		}
		break;
	    case "dupl":
		if(is_connected=false){
		    if(arr.length==3){
			System.out.println("multi diff adress ?");
			String multi_diff2 = scanner.nextLine();
			System.out.println("multi diff port ?");
			String port_diff2 = scanner.nextLine();
			try{
			    String ipf=me.fill_ip(arr[1]);
			    me = new Entity(ip, id, port_udp, port_tcp, multi_diff2, port_diff2);
			    cl=new ClientTcp(me,ipf, arr[2]);
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
			}catch(IpException e){
			    System.out.println(e);
			    e.printStackTrace();  
			}
		    }else{
			System.out.println("wrong arguments");
		    }
		}else{
		    System.out.println("You are already login");
		}	
		break;
	    case "quit ring" :
		is_connected=false;
		me=null;
		m=new Mess("gbye", me);
		su.quit();
		m.send_mess();
		//serveurs=null
		break;
	    default :
		if(is_connected=true){
		    m=new Mess(cmd, me);
		    m.send_mess();
		}else{
		    System.out.println("use connect, create or dupl");
		}
		break;
	    }
	}
    }
}
