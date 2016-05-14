import java.util.*;
import java.lang.*;

public class Mess{
    ServeurUdp u;
    String cmd;
    Entity ent;
    String idm;

    public Mess(String cmd, Entity ent, ServeurUdp u){
	this.u=u;
	this.cmd=cmd;
	this.ent=ent;
	this.idm=Fonction.giveUniqueId();
    }
    

    public void send_mess(){
	ClientUdp cl;
	String[] arr = cmd.split(" ");
	switch (arr[0]){
	case "whos" : 
	    envoyer( "WHOS "+idm );
	    Mess m=new Mess("memb", ent, u);
	    m.send_mess();
	    break;
	case "memb" :
	    envoyer("MEMB "+idm+" "+ent.id+" "+ent.ip+" "+ent.port_udp );
	    //System.out.println(ent.id+" "+ent.ip+" "+ent.port_udp+" "+idm);
	    break;
	case "eybg1" : 
	    cl=new ClientUdp( "EYBG "+idm );
	    cl.send(ent.ip_next, ent.port_udp_next);
	    break;
	case "eybg2" : 
	    cl=new ClientUdp( "EYBG "+idm );
	    cl.send(ent.ip_next2, ent.port_udp_next2);
	    break;
	case "gbye" :
	    envoyer( "GBYE "+idm+" "+ent.ip+" "+ent.port_udp+" "+ent.ip_next+" "+ent.port_udp_next);
	    break;
	case "test" :
	    u.test1();
	    envoyer("TEST "+idm+" "+ent.ip_diff+" "+ent.port_diff);
	    u.test2();
	    break;
	case "diff" : 
	    int size_mess=cmd.length()-5;
	    if(size_mess<486){
		envoyer("APPL "+idm+" DIFF#### "+Fonction.fill(3,size_mess)+" "+cmd.substring(5));
	    }else{
		System.out.println("message trop gros : maximum 485 charactères");
	    }
	    break;
	case "quizz" : 
	    envoyer("APPL "+idm+" QUIZZ### ASK "+ent.id);
	    break;
	case "que" : 
	    int size_que=cmd.length()-4;
	    envoyer("APPL "+idm+" QUIZZ### QUE "+ ent.id+" "+Fonction.fill(3,size_que)+" "+cmd.substring(4));
	    break;
	case "rep" : 
	    int size_rep=cmd.length()-4;
	    envoyer("APPL "+idm+" QUIZZ### REP "+ent.id+" "+ent.ip+" "+ent.port_udp+" "+ Fonction.fill(3,size_rep)+" "+cmd.substring(4));
	    break;
	case "win" : 
	    envoyer("APPL "+idm+" QUIZZ### WIN "+arr[1]+" "+arr[2]+" "+arr[3]);
	    break;
	case "transfert":
	    String size_nom = Fonction.fill(2,arr[1].length());
	    System.out.println("sa se lance");
	    envoyer("APPL "+idm+" TRANS###"+"REQ "+" "+size_nom+" "+arr[1]);
	    break;
	}
    }
    public void envoyer(String s){
	ClientUdp cl=new ClientUdp(s);
	u.add_list(idm);
	cl.send(ent.ip_next, ent.port_udp_next);
	if(ent.ip_next2!=null){
	    u.add_list(idm);
	    cl.send(ent.ip_next2, ent.port_udp_next2);	    
	} 
    }   
}

