import java.io.*;
import java.net.*;

public class ClientUdp {
    String mess;

    public ClientUdp(String mess){
	this.mess=mess;
    }
    public void send(String ip, String port){
	try{
	    DatagramSocket dso=new DatagramSocket();
	    byte[]data;
	    data=mess.getBytes();
	    DatagramPacket paquet=new DatagramPacket(data,data.length,InetAddress.getByName(ip),Integer.parseInt(port));
	    dso.send(paquet);
	} catch(Exception e){
	    e.printStackTrace();
	}
    }
}
