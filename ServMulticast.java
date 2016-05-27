import java.io.*;
import java.net.*;

public class ServMulticast extends Serveur{
    boolean diff1;
    public ServMulticast(Entity e, boolean b){
	super(e);
	this.diff1=b;
    }
    public void runServ(int run){
	int p;
	String ip;
	if(diff1){
	    p = Integer.parseInt(ent.port_diff);
	    ip=ent.ip_diff;
	}else{
	    p = Integer.parseInt(ent.port_diff2);
	    ip=ent.ip_diff2;
	}
	    try{
		MulticastSocket mso=new MulticastSocket(p);
		mso.joinGroup(InetAddress.getByName(ip));
		byte[] buff = new byte[10];
		DatagramPacket paquet=new DatagramPacket(buff,buff.length);
		while(run!=-1){
		    mso.receive(paquet);
		    String s=new String(paquet.getData(),0,paquet.getLength());
		    if(s.equals("DOWN")){
			if(ent.ip_next2==null){
			    System.out.println("anneau cassé, déconnection.");
			    System.exit(0);
			}else{
			    System.out.println("anneau cassé, déconnection de l'anneau "+ip);
			    if(diff1){
				ent.ip_diff=ent.ip_diff2;
				ent.port_diff=ent.port_diff2;
				ent.ip_next=ent.ip_next2;
				ent.port_udp_next=ent.port_udp_next2;
			    }
			    ent.ip_diff2=null;
			    ent.port_diff2=null;
			    ent.ip_next2=null;
			    ent.port_udp_next2=null;			    
			}
		    }
		}
	    }
	catch(IOException e){
	    e.printStackTrace();
	}
    }
}
