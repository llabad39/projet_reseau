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

	while(arg){
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

	while(arg){
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
 
	Entity me=new Entity();


	try{
	    Enumeration<NetworkInterface> listNi=NetworkInterface.getNetworkInterfaces();
	    NetworkInterface nic=listNi.nextElement();
	    Enumeration<InetAddress> listIa=nic.getInetAddresses();
	    InetAddress iac=listIa.nextElement();
	    iac=listIa.nextElement();
	    String[] arr = iac.toString().split("/");
	    ip=me.fill_ip(arr[1]);
	    System.out.println(ip);
	} catch(Exception e){
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

	    while(!is_connected){
		switch (arr[0]){
		case "connect":
			if(arr.length==3){			    
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
			System.out.println("multi diff adress ?");
			String multi_diff = scanner.nextLine();
			System.out.println("multi diff port ?");
			String port_diff = scanner.nextLine();
			me = new Entity(ip, id, port_udp, port_tcp, multi_diff, port_diff);
			is_connected=true;
		    break;
		case "dupl":
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
		default : 
		    System.out.println("use connect, create or dupl");
		    break;
		}
	    }
	    ServeurTcp s = new ServeurTcp(me);
	    ServeurUdp u =  new ServeurUdp(me);
	    Mythread mt1 = new Mythread(s);
	    Mythread mt2 = new Mythread(u);
	    Thread t1=new Thread(mt1,"Bob");
	    System.out.println("avant");
	    Thread t2=new Thread(mt2,"Bob");
	    t1.start();
	    System.out.println("milieu");
	    t2.start();
		  System.out.println("fin");
	  System.out.println("ok1");	
	    while (is_connected){
  System.out.println("ok2");
		cmd = scanner.nextLine();
		arr = cmd.split(" ");
		switch (arr[0]){
		case "quit ring" :
		    System.out.println("ok");
		    is_connected=false;
		    me=null;
		    m=new Mess("gbye", me);
		    mt2.arret();
		    mt1.arret();
		    m.send_mess();
		    //serveurs=null
		    break;
		default :
		    m=new Mess(cmd, me);
		    m.send_mess();
		    break;
		}
	    }
	}
    }
}

