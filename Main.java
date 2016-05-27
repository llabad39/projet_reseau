import java.util.*;
import java.net.InetAddress; 
import java.net.UnknownHostException;
import java.lang.*;
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
 
	Entity me=new Entity();
	try{
	    ip=me.fill_ip(me.give_ip());
	}catch(IpException e){
	    System.out.println(e);
	    e.printStackTrace();  
	}
	ClientTcp cl;
	int a;
	boolean is_connected=false;
	boolean b=true;
	boolean quit=false;

	while(b && !quit){
	    Mess m;
	    while(!is_connected && !quit){
		String tcp;
		String udp;
		String cmd = scanner.nextLine();
		String[] arr = cmd.split(" ");
		switch (arr[0]){
		case "quit" : 
		    quit=true;
		    break;
		case "connect":
		    if(arr.length==3){
			port_udp=askUdp();
		        port_tcp=askTcp(port_udp);
			me = new Entity(ip, id, port_udp, port_tcp);
			cl=new ClientTcp(me,arr[1], arr[2]);
			a=cl.clientTCP("connect");
			if(a==0){
				is_connected=true;
			}else{
			    System.out.println("wrong arguments");
			    }
		    }else{
			System.out.println("wrong arguments");
		    }
		    break;
		case "create":
		    port_udp=askUdp();
		    port_tcp=askTcp(port_udp);
		    System.out.println("multi diff adress ?");
		    String multi_diff = scanner.nextLine();
		    System.out.println("multi diff port ?");
			String port_diff = scanner.nextLine();
			me = new Entity(ip, id, port_udp, port_tcp, multi_diff, port_diff);
			is_connected=true;
			break;
		case "dupl":
		    if(arr.length==3){
			port_udp=askUdp();
		        port_tcp=askTcp(port_udp);
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
		    break;
		case "quit":
		    System.exit(0);
		    break;
		default : 
		    System.out.println("use connect, create or dupl");
		    break;
		}
	    }
	    if(!quit){
		System.out.println("vous etes connectés");
		System.out.println("port next : "+me.port_udp_next);
		
		Quizz q=new Quizz(me);
		ServeurTcp s = new ServeurTcp(me);
		Thread t1 = new Thread(s);
		ServeurUdp u =  new ServeurUdp(me,q);
		q.u=u;
		Mythread mt2 = new Mythread(u);
		Thread t2=new Thread(mt2);
		ServMulticast sm = new ServMulticast(me, u);
		Mythread mt3 = new Mythread(sm);
		Thread t3 = new Thread(mt3);
		t1.start();
		t2.start();
		t3.start();

		while (is_connected && !quit){
		    String cmd = scanner.nextLine();
		    if(me.quizz){
			q.play(cmd);
		    }else{
			String[] arr = cmd.split(" ");
			switch (arr[0]){
			case "info" : 
			    System.out.println(me.id);
			    System.out.println(me.port_udp_next);
			    System.out.println(me.port_udp_next2);
			    System.out.println(me.quizz);
			    System.out.println(q.quizzque);
			    System.out.println(q.quizzask);
			    System.out.println(q.quizzplay);

			    break;
			case "quit" : 
			    quit=true;
			    m=new Mess("gbye", me, u);
			    s.stop();
			    u.add_list(m.idm);
			    m.send_mess();
			    break;
			case "quit_ring" :
			    is_connected=false;
			    m=new Mess("gbye", me, u);
			    s.stop();
			    u.add_list(m.idm);
			    m.send_mess();
			    break;
			default :
			    m=new Mess(cmd, me, u);
			    m.send_mess();
			    break;
			}
		    }
		}
	    }
	}
    }
    public static String askTcp(String port_udp){
	String port_tcp;
	while(true){
	    Scanner scanner = new Scanner (System.in);
	    System.out.println("port TCP ?");
	     port_tcp = scanner.nextLine();
	    if(!port_tcp.equals(port_udp)){
		try{
		    ServerSocket server=new ServerSocket(Integer.parseInt(port_tcp));
		    server.close();
		    break;
		}
		catch(Exception e){
		    System.out.println("Mauvais port tcp");
		}
	    }else{
		System.out.println("Mauvais port tcp");
	    }
	}
	return port_tcp;
    }
    public static String askUdp(){
	String port_udp;
	Scanner scanner = new Scanner (System.in);
	while(true){
	    System.out.println("port UDP ?");
	    port_udp = scanner.nextLine();
	    try{
		DatagramSocket dso=new DatagramSocket(Integer.parseInt(port_udp));
		dso.close();
	    	break;
	    }	
	    catch(Exception e){
		System.out.println("Mauvais port udp");
	    }
	}
	return port_udp;
    }
    
    
}



