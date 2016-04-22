import java.io.*;
import java.net.*;

public class ServeurUdp {
    Entity ent;
    public ServeurUdp(Entity e){
	this.ent = e;
    }
    public void runServ(){
	try{
	    int port_udp=Integer.parseInt(ent.port_udp);
	    DatagramSocket dso=new DatagramSocket(port_udp);
	    byte[]data=new byte[100];
	    DatagramPacket paquet=new DatagramPacket(data,data.length);
	    while(true){
		dso.receive(paquet);
		String st=new 
		    String(paquet.getData(),0,paquet.getLength());
		//blabla...
	    }
	} catch(Exception e){
	    e.printStackTrace();
	}
    }
}
