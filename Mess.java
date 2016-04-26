import java.util.*;

public class Mess{
    String cmd;
    Entity ent;
    long idm;

    public Mess(String cmd, Entity ent){
	this.cmd=cmd;
	this.ent=ent;
	this.idm=give_idm(ent);
    }
    
    public long give_idm(Entity ent){
	Date maDate=new Date();
	String[] arr=maDate.toString().split(" ");
	String[] arr2=arr[3].split("\\:");
	String dat=""+arr2[0]+arr2[1]+arr2[2];
	return Long.parseLong(ent.ip.hashCode()+dat);
    }

    public void send_mess(){
	String[] arr = cmd.split(" ");
	switch (arr[0]){
	case "whos" : 
	    if(arr.length==1){
		//envoyer "WHOS "+idm
		// Mess m=new Mess("memb", ent);
		//  m.send_mess();
	    }else{
		System.out.println("commande mal formée : whos ne prend pas d'argument");
	    }
	    break;
	case "memb" : 
	    //envoyer "MEMB "+idm+" "+ent.id+" "+ent.ip+" "+ent.port_udp;
	    break;
	case "gbye" : 
	    // envoyer "GBYE "+idm+" "+ent.ip+" "+ent.port_udp+" "+ent.ip_next+" "+ent.port_udp_next;
	}
    }

}

