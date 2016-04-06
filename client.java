import java.net.*;
import java.io.*;

public class client{

    public void clientTCP(Entity ent, String port, String addr,String mode){
	try{
	    Socket socket = new Socket(addr,Integer.parseInt(port));
	    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	    PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

	    switch (mode){
	    case "connect" : 
		String mess = br.readLine();
		String[] welc = mess.split(" "); 
		
		InetAddress ia = InetAddress.getLocalHost();
		pw.print("NEWC "+ia.getHostAddress()+" "+ent.port_udp+" \n");
		pw.flush();
		String ackc = br.readLine();
		pw.close();
		br.close();
		socket.close();
		if(!ackc.equals("ACKC\n")){
		    System.err.println("Erreur d'acceptation, message repondu: "+ackc);
		    return;
		}
		// welc[0]="WELC " / welc[1]=ip / welc[2]=port / welc[3]=ip-diff / welc[4]=port-diff
		ent.setIpNext(welc[1]);
		ent.setPortUdpNext(welc[2]);
		ent.setIpDiff(welc[3]);
		ent.setPortDiff(welc[4]);		
		break;
	    case "duplic" : 
		break;
	    }
	   
	}catch(Exception e){
	    System.out.println(e);
	    e.printStackTrace();
	    //return -1 ??
	}
    }
}
