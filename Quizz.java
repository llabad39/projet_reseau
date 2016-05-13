import java.io.*;
import java.net.*;
import java.util.*;

class Quizz{
    Entity ent;
    ServeurUdp u;
    boolean quizzask;    
    boolean quizzplay;
    boolean quizzque;
    boolean quizzok;
    String reponse;

    public Quizz( Entity ent){
	this.ent=ent;
	this.u=u;
	this.quizzask=false;
	this.quizzplay=false;
	this.quizzque=false;
	this.quizzok=false;
    }

    public void play(String c){
	System.out.println("kuyg");
	Scanner scanner = new Scanner (System.in);
	Mess m;
	boolean quit=false;
	String cmd=c;
	while (!quit){
	    if(quizzque){
		m=new Mess("que "+cmd, ent, u);
		System.out.println("Reponse : ");
		reponse = scanner.nextLine();
		m.send_mess();
	    }else{
		if(quizzask){
		    if(cmd.equals("o") || cmd.equals("O")){
			quizzplay=true;
			quizzask=false;
			System.out.println("QUIZZ !");
			if(quizzok){
			    m=new Mess("ok!", ent, u);
			    m.send_mess();
			}
		    }else{
			if(cmd.equals("n") || cmd.equals("N")){
			    ent.quizz=false; 
			    if(quizzok){
				m=new Mess("ok!", ent, u);
				m.send_mess();
			    }
			}else{
			    System.out.println("(O/N)");
			}
		    }
		}else{
		    if(quizzplay){
			m=new Mess("rep "+cmd, ent, u);
			m.send_mess();
		    }else{
		    }
		}
	    }
	    cmd = scanner.nextLine();
	}
    }

    public void recu(String st,  int index){
	String[] arr = st.split(" ");
	Mess m;
	String idm=arr[1];
	switch(arr[3]){
	case "ASK" :
	    if(index==-1){
		ent.quizz=true;
		quizzask=true;
		System.out.println(arr[4]+" vous propose un quizz, voulez vous jouer ?   (O/N)");
		transferer( st, idm);
	    }else{
		quizzque=true;
		System.out.println("Question : ");
		u.idmess.remove(index);
	    }
	    break;
	case "OK!" : 
	    if(quizzask){
		quizzok=true;
	    }else{
		transferer( st, idm);
	    }
	    break;
	case "QUE" : 
	    if(index==-1){
		if(quizzplay){
		    System.out.println("question de "+arr[4]+" : "+st.substring(40));
		}
		transferer(st, idm);
	    }else{
		u.idmess.remove(index);
	    }
	    break;
	case "REP" : 
	    if(index==-1){
		if(!quizzque){
		    if(quizzplay){
			System.out.println(arr[4]+" : "+st.substring(61));
		    }
		}else{
		    if(st.substring(61).equals(reponse)){
			quizzque=false;
			quizzplay=true;
			m=new Mess("win "+arr[4]+" "+arr[5]+" "+arr[6], ent, u);
			m.send_mess();
		    }  
		}
		transferer(st, idm);
	    }else{
		u.idmess.remove(index);
	    }
	    break;
	case "WIN" : 
	    if(index==-1){
		if(quizzplay){
		    if(arr[5].equals(ent.ip) && arr[6].equals(ent.port_udp)){
			System.out.println("vous avez gagné !");
			quizzque=true;
			System.out.println("Question : ");
		    }else{
			System.out.println(arr[4]+" a gagné");
		    }
		}
		transferer(st, idm);
	    }else{
		u.idmess.remove(index);
	    }
	    break;
	}
    }
    public void transferer(String s, String idm ){
	ClientUdp cl=new ClientUdp(s);
	if(ent.ip_next2==null){
	    cl.send(ent.ip_next, ent.port_udp_next);
	}else{
	    u.idmess.add(idm);
	    cl.send(ent.ip_next, ent.port_udp_next);
	    cl.send(ent.ip_next2, ent.port_udp_next2);	
	} 
    }
}
