import java.io.*;
import java.net.*;

public class SendMulticast{
    Entity ent;
    public SendMulticast(Entity e){
	this.ent = e;
    }

    public void send(String text){
	try{
	    DatagramSocket dso=new DatagramSocket();
	    byte[] buf = text.getBytes();
	    InetSocketAddress ia=new InetSocketAddress(this.ent.ip_diff,Integer.parseInt(this.ent.port_diff));
	    DatagramPacket paquet=new DatagramPacket(buf,buf.length,ia);
	    dso.send(paquet);
	    dso.close();
	}
	catch(Exception e){
	    e.printStackTrace();
	}
    }
}
