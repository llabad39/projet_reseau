import java.io.*;
import java.net.*;
import java.util.*;

public class ServeurUdp extends Serveur{
    //Entity e;
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
		Mess m;
		switch (arr[0]){
		case "GBYE" : 
		    System.out.println("GBYE");
			System.out.println(arr[2]+" "+ent.ip_next);

		    if(arr[2].equals(ent.ip_next)){
			System.out.println(arr[2]+" "+ent.ip_next);
			m=new Mess("eybg", ent);
			m.send_mess();
		    }
		    break;
		case "EYBG" : 
		    //if(!run){
			b=false;
			ent=null;
			dso.close();
			System.out.println("vous etes déconnectés");
			//}
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
