import java.io.*;
import java.net.*;
import java.util.*;

class Quizz{
    Entity ent;
    ServeurUdp u;
    boolean quizzask;    
    boolean quizzplay;
    boolean quizzque;
    boolean quizzgo;
    boolean quizzdoubleur;
    String reponse;
    int points;
    String ok;


    public Quizz( Entity ent){
	this.ent=ent;
	this.u=u;
	this.quizzask=false;
	this.quizzplay=false;
	this.quizzque=false;
	this.quizzgo=false;
	this.quizzdoubleur=false;
	this.points=0;
    }

    public void play(String c){
	Scanner scanner = new Scanner (System.in);
	Mess m;
	boolean quit=false;
	String cmd=c;
	while (!quit){
	    if(cmd.equals("quit")){
		quit=true;
		ent.quizz=false;
		this.quizzask=false;
		this.quizzplay=false;
		this.quizzque=false;
		this.quizzgo=false;
		this.quizzdoubleur=false;
		break;
	    }
	    if(quizzque && reponse==null){
		m=new Mess("que "+cmd, ent, u);
		System.out.println("Reponse : ");
		reponse = scanner.nextLine();
		m.send_mess();
	    }else{
		if(quizzask){
		    if(cmd.equals("o") || cmd.equals("O")){
			System.out.println("QUIZZ !");
			if(ok!=null){
			    String[] arr = ok.split(" ");
			    u.transferer(ok, arr[1]);
			    ok=null;
			    quizzask=false;
			    quizzplay=true;
			}else{
			    if(quizzgo){
				quizzask=false;
				quizzplay=true;
			    }
			    quizzgo=true;
			}
		    }else{
			if(cmd.equals("n") || cmd.equals("N")){
			    ent.quizz=false; 
			    if(ok!=null){
				String[] arr = ok.split(" ");
				u.transferer(ok, arr[1]);
				ok=null;
				quizzask=false;
			    }else{
				if(quizzgo){
				    quizzask=false;
				}
				quizzgo=true;
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
		if(!(quizzplay || quizzque)){
		    ent.quizz=true;
		    quizzask=true;
		    System.out.println(arr[4]+" vous propose un quizz, voulez vous jouer ?   (O/N)");
		}		    
		u.transferer( st, idm);
	    }else{
		u.idmess.remove(index);
	    }
	    break;
	case "OK!" :
	    System.out.println(st);
 	    if(index==-1){
		if(!(quizzplay || quizzque)){
		    if(quizzgo){
			u.transferer( st, idm);
			quizzgo=false;
			quizzplay=true;
		    }else{
			ok=st;
		    }
		}else{
		    m=new Mess("rej "+ent.id, ent, u);
		    m.send_mess();
		}
	    }else{
		if(ent.ip_next2!=null){
		    if(quizzdoubleur){
			quizzque=true;
			System.out.println("Question : ");
			quizzdoubleur=false;
		    }else{
			quizzdoubleur=true;
		    }
		}else{
		    quizzque=true;
		    System.out.println("Question : ");
		}
	    }
	    break;
	case "REJ" : 
	    if(index==-1){
		if(!(quizzplay || quizzask || quizzque)){
		    System.out.println("Un quizz est en cour, voulez vous le rejoindre ? (O/N)");
		    ent.quizz=true;
		    quizzask=true;
		    quizzgo=true;
		}
	    }else{
		u.idmess.remove(index);
	    }
	    break;
	case "QUE" : 
	    if(index==-1){
		if(quizzplay){
		    System.out.println("question de "+arr[4]+" : "+st.substring(40));
		}
		u.transferer(st, idm);
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
		    System.out.println(arr[4]+" : "+st.substring(61));
		    if(st.substring(61).equals(reponse)){
			quizzque=false;
			quizzplay=true;
			m=new Mess("win "+arr[4]+" "+arr[5]+" "+arr[6], ent, u);
			m.send_mess();
			reponse=null;
		    }  
		}
		u.transferer(st, idm);
	    }else{
		u.idmess.remove(index);
	    }
	    break;
	case "WIN" : 
	    if(index==-1){
		if(quizzplay){
		    if(arr[5].equals(ent.ip) && arr[6].equals(ent.port_udp)){
			points+=1;
			if(points==5){
			    m=new Mess("gg! "+ent.id, ent, u);
			    m.send_mess();
			    points=0;
			    System.out.println("vous avez gagné le quizz!");
			}else{
			    System.out.println("gagné !");
			}
			quizzque=true;
			System.out.println("Question : ");
		    }else{
			System.out.println(arr[4]+" a gagné");
		    }
		}
		u.transferer(st, idm);
	    }else{
		u.idmess.remove(index);
	    }
	    break;
	case "GG!" : 
	    if(index==-1){
		if(quizzplay){
		    System.out.println(arr[4]+" a gagné le quizz !!");
		    points=0;
		}
		u.transferer(st, idm);
	    }else{
		u.idmess.remove(index);
	    }
	    break;
	}
    }
}
