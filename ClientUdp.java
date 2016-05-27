import java.io.*;
import java.net.*;

public class ClientUdp {
    byte[] data;

    public ClientUdp(byte[] mess){
	this.data=mess;
    }
    public ClientUdp(String s){
        this.data=s.getBytes();
    }
    
    public void send(String ip, String port){
	try{
	    DatagramSocket dso=new DatagramSocket();
	    DatagramPacket paquet=new DatagramPacket(data,data.length,InetAddress.getByName(ip),Integer.parseInt(port));
	    dso.send(paquet);
	    System.out.println(paquet.getLength()+" mon paquet");
	} catch(Exception e){
	    e.printStackTrace();
	}
    }
}
