import java.io.*;
import java.net.*;

public class ServMulticast extends Serveur{
    ServeurUdp u;
    public ServMulticast(Entity e, ServeurUdp u){
	super(e);
	this.u=u;
    }
    public void runServ(int run){
	
	    int p = Integer.parseInt(ent.port_diff);
	    try{
		MulticastSocket mso=new MulticastSocket(p);
		mso.joinGroup(InetAddress.getByName(ent.ip_diff));
		byte[] buff = new byte[10];
		DatagramPacket paquet=new DatagramPacket(buff,buff.length);
		while(run!=-1){
		    mso.receive(paquet);
		    String s=new String(paquet.getData(),0,paquet.getLength());
		    if(s.equals("DOWN")){
			u.quit();
		    }
		}
	    }
	catch(IOException e){
	    e.printStackTrace();
	}
    }
}
