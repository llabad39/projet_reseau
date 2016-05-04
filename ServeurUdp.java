import java.io.*;
import java.net.*;
import java.util.*;

public class ServeurUdp extends Serveur{
    boolean test=false;
    ArrayList<Long> idmess;
    public ServeurUdp(Entity e){
	super(e);
	this.idmess=new ArrayList<Long>();

    }
    public void runServ(int run){
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
		Long idm=Long.parseLong(arr[1]);
		int index=idmess.indexOf(idm);
		switch (arr[0]){
		case "GBYE" : 
		    if(arr[2].equals(ent.ip_next)){
			this.ent.ip_next = arr[4];
			this.ent.port_udp_next = arr[5];
			m=new Mess("eybg", ent, this);
			m.send_mess();
			
		    }else{
		        transferer( st, idm);
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
		case "WHOS" :
		    if(index==-1){
			m=new Mess("memb", ent, this);
			m.send_mess();
		        transferer(st,idm);
		    }else{
			//System.out.println(index);
			idmess.remove(index);
		    }
		    break;
		
		case "MEMB" : 
		    if(index==-1){
			transferer( st, idm);
			System.out.println(arr[2]+" "+arr[3]+" "+arr[4]+" "+idm);
		    }else{
			System.out.println(idm);
			idmess.remove(index);
		    }
		    break;
		case "TEST" : 
		    if(index==-1){
			transferer(st, idm);
		    }else{
			test=false;
			idmess.remove(index);
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

    public void test1(){
	test=true;
    }
    public void test2(){
	try{
	    Thread.sleep(2000);
	}catch(InterruptedException e){
	    e.printStackTrace();
	}
	//System.out.println("test");
	if(!test){
	    System.out.println("test réussi");
	}else{
	    //down
	}
    }
    public void transferer(String s, Long idm){
	ClientUdp cl=new ClientUdp(s);
	if(ent.ip_next2==null){
	    cl.send(ent.ip_next, ent.port_udp_next);
	}else{
	    idmess.add(idm);
	    cl.send(ent.ip_next, ent.port_udp_next);
	    //System.out.println(idm);
	    cl.send(ent.ip_next2, ent.port_udp_next2);	
	} 
    }
}
