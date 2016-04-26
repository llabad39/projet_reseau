import java.io.*;
import java.net.*;
import java.util.*;

public class ServeurUdp {
    Entity ent;
    ArrayList<Long> idmess;
    boolean quit = false;
    public ServeurUdp(Entity e){
	this.ent = e;
	this.idmess=new ArrayList<Long>();
    }
    public void runServ(){
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
		    if(quit){
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
    public void quit(){
	quit=true;
    }
}
