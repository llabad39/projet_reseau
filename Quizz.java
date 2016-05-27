import java.io.*;
import java.net.*;
import java.util.*;

class Quizz{
    Entity ent;
    ServeurUdp u;
    boolean quizzask;    
    boolean quizzplay;
    boolean quizzque;
    boolean doubleur;
    String reponse;
    int points;


    public Quizz( Entity ent){
	this.ent=ent;
	this.u=u;
	this.quizzask=false;
	this.quizzplay=false;
	this.quizzque=false;
	this.doubleur=false;
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
		quit();
		break;
	    }
	
	    if(quizzque){
		int temps=0;
		System.out.println("Reponse : ");
		reponse = scanner.nextLine();
		System.out.println("Combien de secondes ? ");
		boolean bo=true;
		while(bo){
		    bo=false;
		    try {
			temps = Integer.parseInt(scanner.nextLine());
		    } catch (NumberFormatException e) {
			bo=true;
			System.out.println("vous devez rentrer un temps en secondes.");
		    }
		}
		m=new Mess("que "+temps+" "+cmd, ent, u);
		m.send_mess();
		while(temps!=0 && quizzque){
		    try{
			Thread.sleep(1000);
		    }catch(InterruptedException e){
			e.printStackTrace();
		    }
		    temps-=1;
		}
		if(temps==0){ 
		    System.out.println("temps écoulé.");
		    m=new Mess("tim ", ent, u);
		    m.send_mess();
		    System.out.println("Voulez vous poser une autre question ? (O/N)");
		    while(true){
			String rep = scanner.nextLine();
			if(rep.equals("o") || rep.equals("O")){
			    System.out.println("Question : ");
			    reponse=null;
			    break;
			}else{
			    if(rep.equals("n") || rep.equals("N")){
				quit();
				quit=true;
				break;
			    }
			    System.out.println("(O/N)");
			}
		    }
		}		
	    }else{
		if(quizzask){
		    if(cmd.equals("o") || cmd.equals("O")){
			System.out.println("QUIZZ !");
			quizzask=false;
			quizzplay=true;
		    }else{
			if(cmd.equals("n") || cmd.equals("N")){
			    ent.quizz=false; 
			    quizzask=false;
			    quit=true;
			    break;
			}else{
			    System.out.println("(O/N)");
			}
		    }
		}else{
		    if(quizzplay){
			m=new Mess("rep "+cmd, ent, u);
			m.send_mess();
		    }
		}
	    }
	    if(ent.quizz){
		cmd = scanner.nextLine();
	    }
	}
    }

    public void quit(){
	Mess m=new Mess("qit ", ent, u);
	m.send_mess();
	ent.quizz=false;
	quizzask=false;
	quizzplay=false;
	quizzque=false;
	doubleur=false;
	System.out.println("vous avez quité le quizz.");
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
		    u.transferer( st, idm);
		}else{
		    m=new Mess("rej "+ent.id, ent, u);
		    m.send_mess();
		}		    
	    }else{
		if(ent.ip_next2!=null){
		    if(doubleur){
			quizzque=true;
			System.out.println("Question : ");
			doubleur=false;
		    }else{
			doubleur=true;
		    }
		}else{
		    quizzque=true;
		    System.out.println("Question : ");
		}
		u.idmess.remove(index);
	    }
	    break;
	case "REJ" : 
	    if(index==-1){
		    System.out.println("Un qui");
		if(ent.quizz && !(quizzplay || quizzask || quizzque)){
		    System.out.println("Un quizz est en cour, voulez vous le rejoindre ? (O/N)");
		    quizzask=true;
		}
		u.transferer(st, idm);
	    }else{
		u.idmess.remove(index);
	    }
	    break;
	case "QUE" : 
	    if(index==-1){
		if(quizzplay){
		    System.out.println("question de "+arr[4]+" : "+st.substring(41+arr[5].length())+" ; vous avez "+arr[5]+" secondes");
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
		    if(reponse!=null){
			if(st.substring(61).equals(reponse)){
			    quizzque=false;
			    quizzplay=true;
			    m=new Mess("win "+arr[4]+" "+arr[5]+" "+arr[6], ent, u);
			    m.send_mess();
			    reponse=null;
			}  
		    }
		}
		u.transferer(st, idm);
	    }else{
		u.idmess.remove(index);
	    }
	    break;
	case "TIM" : 
	    if(index==-1){
		if(quizzplay){
		    System.out.println("temps écoulé.");
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
			    System.out.println("Question : ");
			}else{
			    System.out.println("gagné !!! Avous de poser une question.");
			}
			quizzque=true;
		    }else{
			System.out.println(arr[4]+" a gagné");
		    }
		}
		u.transferer(st, idm);
	    }else{
		System.out.println(arr[4]+" a gagné");
		u.idmess.remove(index);
	    }
	    break;
	case "GG!" : 
	    if(index==-1){
		if(quizzplay || quizzque){
		    System.out.println(arr[4]+" a gagné le quizz !!");
		    points=0;
		}
		u.transferer(st, idm);
	    }else{
		u.idmess.remove(index);
	    }
	    break;
	case "QIT" : 
	    if(index==-1){
		if(quizzplay  || quizzque){
		    System.out.println(arr[4]+" a quité le quizz");
		}
		u.transferer(st, idm);
	    }else{
		u.idmess.remove(index);
	    }
	    break;
	}
    }
}
