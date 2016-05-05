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
	Date maDate=new Date();
	String[] arr=maDate.toString().split(" ");
	String[] arr2=arr[3].split("\\:");
	String dat=""+arr2[0]+arr2[1]+arr2[2];
	Long l=Long.parseLong(ent.ip.hashCode()+dat+(int)Math.floor(Math.random()*99));
	byte[] b=longToBytes(l);
	String st=new String(b);
	return st;
    }

    public void send_mess(){
	String[] arr = cmd.split(" ");
	ClientUdp cl;
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
	case "eybg" : 
	    envoyer( "EYBG "+idm );
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

