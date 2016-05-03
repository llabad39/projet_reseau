import java.util.*;

public class Mess{
    ServeurUdp u;
    String cmd;
    Entity ent;
    long idm;

    public Mess(String cmd, Entity ent, ServeurUdp u){
	this.u=u;
	this.cmd=cmd;
	this.ent=ent;
	this.idm=give_idm(ent);
    }
    
    public long give_idm(Entity ent){
	Date maDate=new Date();
	String[] arr=maDate.toString().split(" ");
	String[] arr2=arr[3].split("\\:");
	String dat=""+arr2[0]+arr2[1]+arr2[2];

	return Long.parseLong(ent.ip.hashCode()+dat+(int)Math.floor(Math.random()*99));
    }

    public void send_mess(){
	String[] arr = cmd.split(" ");
	ClientUdp cl;
	switch (arr[0]){
	case "whos" : 
	    envoyer( "WHOS "+idm );
	    envoyer("MEMB "+idm+" "+ent.id+" "+ent.ip+" "+ent.port_udp );
	    break;
	case "memb" :
	    envoyer("MEMB "+idm+" "+ent.id+" "+ent.ip+" "+ent.port_udp );
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
	}

    }
    public void envoyer(String s){
	ClientUdp cl=new ClientUdp(s);
	u.add_list(idm);
	System.out.println(idm);
	cl.send(ent.ip_next, ent.port_udp_next);
	if(ent.ip_next2!=null){
	    cl.send(ent.ip_next2, ent.port_udp_next2);	    
	} 
    }
}

