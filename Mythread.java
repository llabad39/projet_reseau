import java.lang.*;
import java.io.*;
import java.net.Socket;
import java.net.*;
public class Mythread implements Runnable{
    Serveur serv;
    Integer r;
    ServerSocket server ;
    public Mythread(Serveur s){
	this.serv = s;
	
	r=0;
    }
    public void run(){
	try{
	    this.serv.runServ(0);
	}
	catch(Exception e){
	    System.out.println("can't run server");
	}
    }
    /* public void arret(){
	
	try{
	    System.out.println("vas y arrete toi");
	    Socket s = new Socket(serv.ent.ip,Integer.parseInt(serv.ent.port_tcp));
	}
	catch(IOException e){
	    System.out.println("can't close");
	}
	
    }*/
}
