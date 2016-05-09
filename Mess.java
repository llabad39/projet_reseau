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
	this.idm=give_idm(ent);
    }
    
    public String give_idm(Entity ent){
	/*Date maDate=new Date();
	String[] arr=maDate.toString().split(" ");
	String[] arr2=arr[3].split("\\:");
	String dat=""+arr2[1]+arr2[2];
	Long ll=13L*ent.port_udp.hashCode()+ent.ip.hashCode()+23L*((int)Math.floor(Math.random()*1000000000));
	Long l=Long.parseLong(ll+dat);
	byte[] b=longToBytes(l);
	String st=new String(b);*/
	String id="";
	int i;
	for(i=0; i<8; i++){
	    id+=(int)Math.floor(Math.random()*10);
	}
	return id;
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
		envoyer("APPL "+idm+" DIFF#### "+fill(size_mess)+" "+cmd.substring(5));
	    }else{
		System.out.println("message trop gros : maximum 485 charactères");
	    }
	    break;
	case "quizz" : 
	    envoyer("APPL "+idm+" QUIZZ### ASK "+ent.id);
	    break;
	case "que" : 
	    int size_que=cmd.length()-4;
	    envoyer("APPL "+idm+" QUIZZ### QUE "+ ent.id+" "+fill(size_que)+" "+cmd.substring(4));
	    break;
	case "rep" : 
	    int size_rep=cmd.length()-4;
	    envoyer("APPL "+idm+" QUIZZ### REP "+ent.id+" "+ent.ip+" "+ent.port_udp+" "+ fill(size_rep)+" "+cmd.substring(4));
	    break;
	case "win" : 
	    envoyer("APPL "+idm+" QUIZZ### WIN "+arr[1]+" "+arr[2]+" "+arr[3]);
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
    public String fill(int size){
	if(size<10){ return "00"+size;}
	else{if(size<100){ return "0"+size;}
	    else{return ""+size; }
	}
    }
    public static byte[] longToBytes(long l) {
	byte[] b = new byte[8];
	for (int i = 7; i >= 0; i--) {
	    b[i] = (byte)(l & 0xFF);
	    l >>= 8;
	}
	return b;
    }
}

