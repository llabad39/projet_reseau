import java.io.*;
import java.net.*;
import java.util.*;

public class ServeurUdp extends Serveur{
    Entity e;
    ArrayList<Long> idmess;
    public ServeurUdp(Entity e){
	super(e);
	this.idmess=new ArrayList<Long>();
	System.out.println((e.id));

    }
    public void runServ(boolean run){
	//System.out.println((e.id));
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
		Mess m;
		switch (arr[0]){
		case "GBYE" : 
		    System.out.println("GBYE");
		    System.out.println((e.id));
		    if(arr[1].equals(e.ip_next)){
			m=new Mess("eybg", e);
			m.send_mess();
		    }
		    break;
		case "EYBG" : 
		    if(!run){
			b=false;
			//	e=null;
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
