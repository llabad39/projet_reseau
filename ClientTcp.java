import java.net.*;
import java.io.*;

public class ClientTcp{

    public Entity ent;
    public String port;
    public String ip;

    public ClientTcp(Entity _ent,String _ip,String _port){
	this.ent = _ent;
	this.port = _port;
	this.ip = _ip;
    }

    public int clientTCP(String mode){
	try{
	    Socket socket = new Socket(this.ip,Integer.parseInt(this.port));
	    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	    PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

	    switch (mode){
	    case "connect" : 
		String mess = br.readLine();
		String[] welc = mess.split(" ");
		pw.print("NEWC "+this.ent.ip+" "+this.ent.port_udp+" \n");
		pw.flush();
		String ackc = br.readLine();
		pw.close();
		br.close();
		socket.close();
		if(!ackc.equals("ACKC")){
		    System.err.println("Erreur d'acceptation, message repondu: "+ackc);
		    return -1;
		}
		this.ent.setIpNext(welc[1]);
		this.ent.setPortUdpNext(welc[2]);
		this.ent.setIpDiff(welc[3]);
		this.ent.setPortDiff(welc[4]);	
		break;
	    case "dupl" : 
		String mess2 = br.readLine();
		String[] welc2 = mess2.split(" ");
		pw.print("DUPL "+this.ent.ip+" "+this.ent.port_udp+" "+this.ent.ip_diff+" "+this.ent.port_diff+" \n");
		pw.flush();
		String ackd = br.readLine();
		pw.close();
		br.close();
		socket.close();
		switch (ackd){
		case "ACKD" : 
		    this.ent.setIpNext(welc2[1]);
		    this.ent.setPortUdpNext(welc2[2]);
		    break;
		case "NOTC" :
		    System.out.println("L'entitée est déjà doubleur");	
		    return -1;
		default : 
		    System.err.println("Erreur d'acceptation, message repondu: "+ackd);
		    return -1;
		}	
	    }
	}catch(Exception e){
	    System.out.println(e);
	    e.printStackTrace();
	    return -1;
	}
	return 0;
    }
}
