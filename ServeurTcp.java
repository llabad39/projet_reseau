import java.net.*;
import java.io.*;


public class ServeurTcp{
    Entity ent;
    public ServeurTcp(Entity e){
	this.ent = e;
    } 
    public void runServ(){
	try{
	    int port_tcp = Integer.parseInt(ent.port_tcp);
	    ServerSocket server = new ServerSocket(port_tcp);
	    while(true){
		Socket socket=server.accept();
		BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter pw=new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
		String text = "WELC "+ent.ip_next+" "+ent.port_udp_next+" "+ent.ip_diff+" "+ent.port_diff;
		pw.print(text);
		pw.flush();
		text=br.readLine();
		String[] token =text.split(" ");
		if(token[0].equals("NEWC")){
		    ent.ip_next = token[1];
		    ent.port_udp_next = token[2];
		    pw.print("ACKC\n");
		}
	    }
	}
	catch(Exception e){
	    System.out.println(e);
	    e.printStackTrace();
	}
    }
}
