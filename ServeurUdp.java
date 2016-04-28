import java.io.*;
import java.net.*;
import java.util.*;

public class ServeurUdp extends Serveur{
    ArrayList<Long> idmess;
    public ServeurUdp(Entity e){
	super(e);
	this.idmess=new ArrayList<Long>();
    }
    public void runServ(boolean run){
	try{
	    int port_udp=Integer.parseInt(ent.port_udp);
	    DatagramSocket dso=new DatagramSocket(port_udp);
	    byte[]data=new byte[4096];
	    DatagramPacket paquet=new DatagramPacket(data,data.length);
	    boolean b=true;
	    while(b){
		dso.receive(paquet);
		String st=new String(paquet.getData(),0,paquet.getLength());
		String[] arr = st.split(" ");
		switch (arr[0]){
		case "EYBG" : 
		    if(!run){
			b=false;
		    }else{
			//transférer
		    }
		    break;
		}
	    }
	} catch(Exception e){
	    e.printStackTrace();
	}
    }


    public void add_list(Long l){
	idmess.add(l);
    }
}
