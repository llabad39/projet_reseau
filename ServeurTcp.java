import java.net.*;
import java.io.*;


public class ServeurTcp extends Serveur{
    public ServeurTcp(Entity e){
	super(e);
    } 
    public void runServ(boolean run){
	try{
	    int port_tcp = Integer.parseInt(ent.port_tcp);
	    ServerSocket server = new ServerSocket(port_tcp);
	    while(run){
		if(this.ent.ip_next2==null){

		    Socket socket=server.accept();
		    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		    PrintWriter pw=new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
		    String text = "WELC "+ent.ip_next+" "+ent.port_udp_next+" "+ent.ip_diff+" "+ent.port_diff+"\n";
		    pw.print(text);
		    pw.flush();
		    
		    text=br.readLine();
		    String[] token =text.split(" ");
		    if(token[0].equals("NEWC")){
			ent.ip_next = token[1];
			ent.port_udp_next = token[2];
			pw.print("ACKC\n");
			pw.flush();
		    }
		    else if(token[0].equals("DUPL")){
			ent.ip_next2 = token[1];
			ent.port_udp_next2 = token[2];
			ent.ip_diff2  = token[3];
			ent.port_diff2 = token[4];
			text = "ACKD "+ent.port_udp+"\n";
			pw.print(text);
			pw.flush();
		    }
		    socket.close();
		}
		   
	    }
	    server.close();
	}
	
	catch(Exception e){
	    System.out.println(e);
	    e.printStackTrace();
	}
    }
}
