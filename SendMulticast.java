import java.io.*;
import java.net.*;

public class SendMulticast{
    Entity ent;
    public SendMulticast(Entity e){
	this.ent = e;
    }

    public void send(String text){
	DatagramSocket dso=new DatagramSocket();
	byte[] buf = text.getBytes();
	InetSocketAddress ia=new InetSocketAddress(this.ent.ip_diff,Integer.parseInt(this.ent.port_diff));
	/*	if(this.ent.ip_next2 != null){
	    int p2 = Integer.parseInt(this.ent.port_diff2);
	    InetSocketAdress ia2=new InetSocketAddress(this.ent.ip_diff2,p2);
	    DatagramPacket paquet2=new DatagramPacket(buf,buf.length,ia2);
	    dso.send(paquet2);
	}
	*/
	DatagramPacket paquet=new DatagramPacket(buf,buf.length,ia);
	dso.send(paquet);
    }
}
