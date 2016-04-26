import java.lang.*;
import java.io.*;

public class Mythread implements Runnable{
    Serveur serv;
    boolean r;
    public Mythread(ServeurTcp s){
	this.serv = s;
	r=true;
    }
    public void run(){
	try{
	    this.serv.runServ(r);
	}
	catch(Exception e){
	    System.out.println("can't run server");
	}
    }
    public void arret(){
	r =false;
    }
}
