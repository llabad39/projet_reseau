import java.net.*;
import java.io.*;


public class ServeurTcp implements Runnable{
    Entity ent;
    ServerSocket ss;
    boolean run;
    public ServeurTcp(Entity e){
	this.ent = e;
	int port_tcp = Integer.parseInt(ent.port_tcp);
	run=true;
	try{
	    this.ss= new ServerSocket(port_tcp);
	}
	catch(IOException ie){
	    System.out.println("can't run TCP server");
	}
	
    } 
    public void run(){
	try{
	    while(run){
		if(ss!=null){
		    Socket socket=this.ss.accept();
		    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		    PrintWriter pw=new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
		    String text = "WELC "+ent.ip_next+" "+ent.port_udp_next+" "+ent.ip_diff+" "+ent.port_diff+"\n";
		    pw.print(text);
		    pw.flush();
		    text=br.readLine();
		    String[] token =text.split(" ");
		    if(token[0].equals("NEWC")){ //cas d'une connexion
			ent.ip_next = token[1];
			ent.port_udp_next = token[2];
			pw.print("ACKC\n");
			pw.flush();
		    }
		    else if(token[0].equals("DUPL")){ // cas d'une duplication
			if(ent.ip_next2==null){
			    ent.ip_next2 = token[1];
			    ent.port_udp_next2 = token[2];
			    ent.ip_diff2  = token[3];
			    ent.port_diff2 = token[4];
			    ServMulticast sm = new ServMulticast(ent,false);
			    Mythread mt3 = new Mythread(sm);
			    Thread t3 = new Thread(mt3);
			    t3.start();
			    text = "ACKD "+ent.port_udp+"\n";
			    pw.print(text);
			    pw.flush();
			}else{
			    text = "NOTC";
			    pw.print(text);
			    pw.flush();
			}
		    }
		    socket.close();
		      
			
		}
	    }
		   
	    this.ss.close();
	    System.out.println("bien fermé");
	}
	
	catch(Exception e){
	    System.out.println("serveur Tcp arrete");
	}
	
    }
    public void stop(){
	try{
	    run=false;
	    this.ss.close();
	}
	catch(IOException e){
	    System.out.println("server can't close");
	}
    }
}
