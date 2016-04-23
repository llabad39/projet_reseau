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
	System.out.println( Long.parseLong(dat+ent.ip.hashCode()));
	return Long.parseLong(dat+ent.ip.hashCode());
    }


}

