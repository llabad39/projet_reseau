import java.util.*;
import java.net.InetAddress; 
import java.net.UnknownHostException;
import java.lang.*;
import java.net.*;
import java.io.*;

public class Main{

    public static void main (String[] args){

	Scanner scanner = new Scanner (System.in);
	//boolean arg = true;
	String id="";
	String ip="";
	String port_tcp="";
	String port_udp="";
	String port_diff="";
	String multi_diff="";
	while(true){
	    System.out.println("\nVeuillez entrer un id (8 char max)");
	    id = scanner.nextLine();
	    if(id.length()>8){
		System.out.println("id trop, 8 char max");
	    }else{
		break;
	    }
	}
	System.out.println("Utilisation: create, dupl ou connect");
 
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
		
		case "connect":
		    System.out.println("\n\t----------CONNECT----------\n");
		    if(arr.length==3){
			port_udp=askUdp();
		        port_tcp=askTcp(port_udp);
			me = new Entity(ip, id, port_udp, port_tcp);
			cl=new ClientTcp(me,arr[1], arr[2]);
			a=cl.clientTCP("connect");
			if(a==0){
			    is_connected=true;
			}else{
			    System.out.println("Erreur, utilisation : create ip port");
			}
		    }else{
			    System.out.println("Erreur, utilisation : create ip port");
		    }
		    System.out.println("\n\t----------FIN CONNECT----------\n");
		    break;
		case "create":
		    System.out.println("\n\t----------CREATE----------\n");
		    port_udp=askUdp();
		    port_tcp=askTcp(port_udp);
		    port_diff=askPortDiff();
		    multi_diff=askDiff(port_diff);
		    me = new Entity(ip, id, port_udp, port_tcp, multi_diff, port_diff);
		    is_connected=true;
		    System.out.println("\n\t----------FIN CREATE----------\n");
		    break;
		case "dupl":
		    System.out.println("\n\t----------DUPLICATION----------\n");
		    if(arr.length==3){
			port_udp=askUdp();
		        port_tcp=askTcp(port_udp);
			port_diff=askPortDiff();
			multi_diff=askDiff(port_diff);
			try{
			    String ipf=me.fill_ip(arr[1]);
			    me = new Entity(ip, id, port_udp, port_tcp, multi_diff, port_diff);
			    cl=new ClientTcp(me,ipf, arr[2]);
			    a=cl.clientTCP("dupl");
			    if(a==0){
				is_connected=true;
			    }else{
				System.out.println("Erreur, utilisation : dupl ip port\n");
				b=false;
			    }
			}catch(IpException e){
			    System.out.println(e);
			    e.printStackTrace();  
			}
		    }else{
			System.out.println("Erreur, utilisation : dupl ip port");
		    }	
		    System.out.println("\n\t----------FIN DUPLICATION----------\n");
		    break;
		default : 
		    System.out.println("Utilisation: connect, create ou dupl");
		    break;
		}
	    }
	    if(!quit){
	
		Quizz q=new Quizz(me);
		ServeurTcp s = new ServeurTcp(me);
		Thread t1 = new Thread(s);
		ServeurUdp u =  new ServeurUdp(me,q);
		q.u=u;
		Mythread mt2 = new Mythread(u);
		Thread t2=new Thread(mt2);
		ServMulticast sm = new ServMulticast(me,true);
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
			    System.out.println("id : "+me.id);
			    System.out.println("port udp : "+me.port_udp);
			    System.out.println("port tcp : "+me.port_tcp);
			    System.out.println("adresse de multidiff : "+me.ip_diff);
			    System.out.println("port udp next : "+me.port_udp_next);
			    System.out.println("port udp next 2 : "+me.port_udp_next2);
			    System.out.println("quizz ? "+me.quizz);
			   
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
		    ServerSocket server = new ServerSocket(Integer.parseInt(port_tcp));
		    server.close();
		    break;
		}
		catch(Exception e){
		    System.out.println("Mauvais port tcp, réessayer");
		}
	    }else{
		System.out.println("Mauvais port tcp, réessayer");
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
		System.out.println("Mauvais port udp, réessayer");
	    }
	}
	return port_udp;
    }
    
    public static String askPortDiff(){
	String port_diff;
	Scanner scanner = new Scanner (System.in);
	while(true){
	    System.out.println("multi diff port ?");
	    port_diff = scanner.nextLine();
	    try{
		MulticastSocket mso=new MulticastSocket(Integer.parseInt(port_diff));
		mso.close();
		break;
	    }
	    catch(Exception e){
		System.out.println("Mauvais port de diffusion, réessayer");
	    }	    
	}
	return port_diff;
    }

 public static String askDiff(String port){
	String multi_diff;
	Scanner scanner = new Scanner (System.in);
	while(true){
	    System.out.println("multi diff adress ?");
	    multi_diff = scanner.nextLine();
	    try{
		MulticastSocket mso=new MulticastSocket(Integer.parseInt(port));
		mso.joinGroup(InetAddress.getByName(multi_diff));
		mso.close();
		break;
	    }
	    catch(Exception e){
		System.out.println("Mauvaise adresse de multidiffusion, réessayer");
	    }	    
	}
	return multi_diff;
    }
}
