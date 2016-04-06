import java.net.*;
import java.io.*;

public class ClientTcp{

    public Entity ent;
    public String port;
    public String ip;

    public ClientTcp(Entity _ent,String _port,String _ip){
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
		
		InetAddress ia = InetAddress.getLocalHost();
		pw.print("NEWC "+ia.getHostAddress()+" "+this.ent.port_udp+" \n");
		pw.flush();
		String ackc = br.readLine();
		pw.close();
		br.close();
		socket.close();
		if(!ackc.equals("ACKC\n")){
		    System.err.println("Erreur d'acceptation, message repondu: "+ackc);
		    return -1;
		}
		// welc[0]="WELC " / welc[1]=ip / welc[2]=port / welc[3]=ip-diff / welc[4]=port-diff
		this.ent.setIpNext(welc[1]);
		this.ent.setPortUdpNext(welc[2]);
		this.ent.setIpDiff(welc[3]);
		this.ent.setPortDiff(welc[4]);		
		break;
	    case "duplic" : 
		break;
	    }
	   
	}catch(Exception e){
	    System.out.println(e);
	    e.printStackTrace();
	    return -1;
	}
	return 0;
    }
}
