import java.util.*;
import java.net.InetAddress; 
import java.net.UnknownHostException;
import java.lang.*;
import java.net.*;
import java.io.*;
    
public class Entity{
    String ip;
    String id;
    String port_udp;
    String port_tcp;
    String ip_next;
    String port_udp_next;
    String ip_diff;
    String port_diff;

    String ip_next2;
    String port_udp_next2;
    String port_diff2;
    String ip_diff2;

    boolean quizzask;    
    boolean quizzplay;
    boolean quizzque;
    boolean quizzok;

    public Entity(){
    }
    
    public Entity(String _ip,String _id,String _port_udp,String _port_tcp){
	this.ip=_ip;
	this.id = fillid(_id);
	this.port_udp = _port_udp;
	this.port_tcp = _port_tcp;
	this.ip_next2 = null;
	this.port_udp_next2 = null;
	this.quizzask=false;
	this.quizzplay=false;
	this.quizzque=false;
	this.quizzok=false;
    }
    
    public Entity(String _ip,String _id,String _port_udp,String _port_tcp,String _ip_diff,String _port_diff){
	this.ip=_ip;
	this.id = fillid(_id);
	this.ip_next = ip;
	this.port_udp_next = _port_udp;
	this.port_udp = _port_udp;
	this.port_tcp = _port_tcp;
	this.ip_diff = _ip_diff;
	this.port_diff = _port_diff;
	this.ip_next2 = null;
	this.port_udp_next2 = null;  
	this.quizzask=false;
	this.quizzplay=false;
	this.quizzque=false;
	this.quizzok=false;
    }

    public String getId(){
	return this.id;
    }

    public void setPortUdpNext(String _port_udp_next){
	this.port_udp_next = _port_udp_next;
    }

    public String getPortUdpNext(){
	return this.port_udp_next;
    }

    public String getIpNext(){
	return this.ip_next;
    }

    public void setIpNext(String _ip_next){
	this.ip_next = _ip_next;
    }

    public String getIpDiff(){
	return this.ip_diff;
    }

    public void setIpDiff(String _ip_diff){
	this.ip_diff = _ip_diff;
    }

    public String getPortDiff(){
	return this.port_diff;
    }

    public void setPortDiff(String _port_diff){
	this.port_diff = _port_diff;
    }

    public String getIpNext2(){
	return this.ip_next2;
    }

    public void setIpNext2(String _ip_next2){
	this.ip_next2 = _ip_next2;
    }

    public String getPortUdpNext2(){
	return this.port_udp_next2;
    }

    public void setPortUdpNext2(String _port_udp_next2){
	this.port_udp_next2 = _port_udp_next2;
    }

    public String fillid(String id){
	String space1="";
	String space2="";
	int i;
	for(i=0; i<(8-id.length())/2; i++){
	    space1+="_";
	}
	for(i=(8-id.length())/2; i<8-id.length(); i++){
	    space2+="_";
	}
	return space1+id+space2;
    }

    public String fill_ip(String ip) throws IpException{
	String[] arr = ip.split("\\.");
	String ip2="";
	if(arr.length==4){
	    int i;
	    for(i=0; i<4; i++){
		switch (arr[i].length()){
		case 0 : ip2+="000"+arr[i]; break;
		case 1 : ip2+="00"+arr[i]; break;
		case 2 : ip2+="0"+arr[i]; break;
		case 3 : ip2+=arr[i]; break;
		default : throw new IpException();
		}
		if(i!=3){ ip2 += ".";}
	    }
	}else{
	    throw new IpException();
	}
	return ip2;
    }    
    public String give_ip(){
    	try{
	    Enumeration<NetworkInterface>
		listNi=NetworkInterface.getNetworkInterfaces();
	    while(listNi.hasMoreElements()){
		NetworkInterface nic=listNi.nextElement();
		Enumeration<InetAddress> listIa=nic.getInetAddresses();
		while(listIa.hasMoreElements()){
		    InetAddress iac=listIa.nextElement();
		    if(iac instanceof Inet4Address){
			String[] ar=iac.toString().split("/");
			System.out.println(ar[1]);
			return ar[1];
		    }
		}
	    }
	} catch(Exception e){
	    e.printStackTrace();
	}
	return "";
    }
}
