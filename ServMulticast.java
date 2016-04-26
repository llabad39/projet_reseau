import java.io.*;
import java.net.*;

public class ServMulticast extends Serveur{
    public ServMulticast(Entity e){
	super(e);
    }
    public void runServ(boolean run){
	
	    int p = Integer.parseInt(ent.port_diff);
	    try{
		MulticastSocket mso=new MulticastSocket(9999);
		mso.joinGroup(InetAddress.getByName(ent.ip_diff));
		byte[] buff = new byte[10];
		DatagramPacket paquet=new DatagramPacket(buff,buff.length);
		while(run){
		    mso.receive(paquet);
		    String s=new String(paquet.getData(),0,paquet.getLength());
		    System.out.println("Le message de multidiffusion est :"+s);
		}
	    }
	catch(IOException e){
	    e.printStackTrace();
	}
    }
}
