import java.io.*;
import java.net.*;
import java.util.*;
import java.io.FileWriter;
public class ServeurUdp extends Serveur{
    boolean test=false;
    ArrayList<String> idmess;
    String reponse;
    long compteurTrans;
    long maxMess;
    String name;
    Transfert trf;
    Quizz q;
    boolean b;
    public ServeurUdp(Entity e,Quizz q){
	super(e);
	this.idmess=new ArrayList<String>();
	this.compteurTrans=0;
	this.maxMess=0;
	this.name= " ";
	trf= new Transfert(this);
	this.q=q;
	this.b=true;
 }
    public void runServ(int run){
	try{
	    int port_udp=Integer.parseInt(ent.port_udp);
	    DatagramSocket dso=new DatagramSocket(port_udp);
	    byte[]data=new byte[512];
	    DatagramPacket paquet=new DatagramPacket(data,data.length);
	    boolean quizz=false;
	    while(b){
		dso.receive(paquet);
		String st=new String(paquet.getData(),0,paquet.getLength());
		//System.out.println(st);
		String[] arr = st.split(" ");
		Mess m;
		String idm=arr[1];
		int index=idmess.indexOf(idm);
		switch (arr[0]){
		case "GBYE" : 
		    if(arr[2].equals(ent.ip_next) && arr[3].equals(ent.port_udp_next)){
			m=new Mess("eybg1", ent, this);
			m.send_mess();	
			this.ent.ip_next = arr[4];
			this.ent.port_udp_next = arr[5];		
		    }else{
			if(arr[2].equals(ent.ip_next2) && arr[3].equals(ent.port_udp_next2)){
			    m=new Mess("eybg2", ent, this);
			    m.send_mess();
			    if(arr[4]==ent.ip && arr[5]==ent.port_udp){
				ent.ip_next=ent.ip_next2;
				ent.port_udp_next=ent.port_udp_next2;
				ent.ip_next2 =null;
				ent.port_udp_next2 =null;
			    }else{
				ent.ip_next2 = arr[4];
				ent.port_udp_next2 = arr[5];
			    }
			}else{
			    transferer( st, idm);
			}
		    }
		    break;
		case "EYBG" : 
		    dso.close();
		    quit();
		    System.out.println("vous etes déconnectés");
		    break;
		case "WHOS" :
		    if(index==-1){
			m=new Mess("memb", ent, this);
			m.send_mess();
		        transferer(st,idm);
		    }else{
			idmess.remove(index);
		    }
		    break;
		
		case "MEMB" : 
		    if(index==-1){
			transferer( st, idm);
			System.out.println(arr[2]+" "+arr[3]+" "+arr[4]+" "+idm);
		    }else{
			idmess.remove(index);
		    }
		    break;
		case "TEST" : 
		    if(index==-1){
			System.out.println(arr[2]+" "+ent.ip_diff+" "+arr[3]+" "+ent.port_diff);
			if((arr[2].equals(ent.ip_diff) && arr[3].equals(ent.port_diff)) ||(arr[2].equals(ent.ip_diff2) && arr[3].equals(ent.port_diff2)) ){
			    transferer(st, idm);
			}
		    }else{
			test=false;
			idmess.remove(index);
		    }
		    break;
		case "APPL" : 
		    switch(arr[2]){
		    case "DIFF####" : 
			if(index==-1){
			    transferer( st, idm);
			    System.out.println(st.substring(27));
			}else{
			    idmess.remove(index);
			}
			break;
		    case "QUIZZ###" : 
			q.recu(st, index);
			break;
			
		    case "TRANS###" :
                        this.trf.s=st;
			this.trf.transfert(paquet.getData(),arr,index,idm);
			break;
			}
			break;

			
		default :
		    System.out.println("défaut");
                    transferer( st, idm);	
		    break;
		}
	    }
	} catch(Exception e){
	    e.printStackTrace();
	}
    }


    public void add_list(String l){
	idmess.add(l);
    }
    public void quizz(String reponse){
	this.reponse=reponse;
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
	if(!test){
	    System.out.println("test réussi");
	}else{
	    SendMulticast sm = new SendMulticast(this.ent);
	    sm.send("DOWN");
	}
    }
    public void transferer(String s, String idm){
	ClientUdp cl=new ClientUdp(s);
	if(ent.ip_next2==null){
	    cl.send(ent.ip_next, ent.port_udp_next);
	}else{
	    idmess.add(idm);
	    cl.send(ent.ip_next, ent.port_udp_next);
	    cl.send(ent.ip_next2, ent.port_udp_next2);	
	} 
    }
    public void transfererB(byte[]data,String idm){
	System.out.println("on transfert un bail de cette taille "+data.length);
	ClientUdp cl = new ClientUdp(data);
        if(ent.ip_next2==null){
	    cl.send(ent.ip_next, ent.port_udp_next);
	}else{
	    idmess.add(idm);
	    cl.send(ent.ip_next, ent.port_udp_next);
	    cl.send(ent.ip_next2, ent.port_udp_next2);	
	} 
    }
    public void quit(){
	b=false;
	ent=null;
    }
}
