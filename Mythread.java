import java.lang.*;
import java.io.*;

public class Mythread implements Runnable{
    ServeurTcp serv;
    public Mythread(ServeurTcp s){
	this.serv = s;
    }
    public void run(){
	try{
	    this.serv.runServ();
	}
	catch(Exception e){
	    System.out.println("can't run server");
	}
    }
}
