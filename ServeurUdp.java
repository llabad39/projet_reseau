import java.io.*;
import java.net.*;
import java.util.*;

public class ServeurUdp extends Serveur{
    boolean test=false;
    ArrayList<String> idmess;
    String reponse;
    public ServeurUdp(Entity e){
	super(e);
	this.idmess=new ArrayList<String>();

    }
    public void runServ(int run){
	try{
	    int port_udp=Integer.parseInt(ent.port_udp);
	    DatagramSocket dso=new DatagramSocket(port_udp);
	    byte[]data=new byte[4096];
	    DatagramPacket paquet=new DatagramPacket(data,data.length);
	    boolean quizz=false;
	    boolean b=true;
	    while(b){
		dso.receive(paquet);
		String st=new String(paquet.getData(),0,paquet.getLength());
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
			    this.ent.ip_next2 = arr[4];
			    this.ent.port_udp_next2 = arr[5];
			}else{
			    transferer( st, idm);
			}
		    }
		    break;
		case "EYBG" : 
		    b=false;
		    ent=null;
		    dso.close();
		    System.out.println("vous etes déconnectés");
		    break;
		case "WHOS" :
		    System.out.println("whooo");
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
			transferer(st, idm);
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
			switch(arr[3]){
			case "ASK" :
			if(index==-1){
			    ent.quizzask=true;
			    System.out.println(arr[4]+" vous propose un quizz, voulez vous jouer ?   (O/N)");
			    transferer( st, idm);
			}else{
			    ent.quizzque=true;
			    System.out.println("Question : ");
			    idmess.remove(index);
			}
			    break;
			case "QUE" : 
			    if(index==-1){
				if(ent.quizzplay){
				    System.out.println("question de "+arr[4]+" : "+st.substring(40));
				}
				transferer(st, idm);
			    }else{
				idmess.remove(index);
			    }
			    break;
			case "REP" : 
			     if(index==-1){
				 if(!ent.quizzque){
				     if(ent.quizzplay){
					 System.out.println(arr[4]+" : "+st.substring(61));
				     }
				 }else{
				     if(st.substring(61).equals(reponse)){
					 ent.quizzque=false;
					 ent.quizzplay=true;
					 m=new Mess("win "+arr[4]+" "+arr[5]+" "+arr[6], ent, this);
					 m.send_mess();
				     }  
				 }
				 transferer(st, idm);
			     }else{
				 idmess.remove(index);
			    }
			    break;
			case "WIN" : 
			    if(index==-1){
				if(ent.quizzplay){
				    if(arr[5].equals(ent.ip) && arr[6].equals(ent.port_udp)){
					System.out.println("vous avez gagné !");
					ent.quizzque=true;
					System.out.println("Question : ");
				    }else{
					System.out.println(arr[4]+" a gagné");
				    }
				}
				transferer(st, idm);
			    }else{
				idmess.remove(index);
			    }
			    break;
			}
			break;
		    }
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
	    //down
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
}
