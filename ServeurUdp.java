import java.io.*;
import java.net.*;
import java.util.*;
import java.io.FileWriter;
public class ServeurUdp extends Serveur{
    boolean test=false;
    ArrayList<String> idmess;
    String reponse;
    long compteurTrans;
    long maxMess;
    String name;
    Quizz q;
    public ServeurUdp(Entity e,Quizz q){
	super(e);
	this.idmess=new ArrayList<String>();
	this.compteurTrans=0;
	this.maxMess=0;
	this.name= " ";
	this.q=q;
 }
    public void runServ(int run){
	try{
	    int port_udp=Integer.parseInt(ent.port_udp);
	    DatagramSocket dso=new DatagramSocket(port_udp);
	    byte[]data=new byte[4096];
	    DatagramPacket paquet=new DatagramPacket(data,data.length);
	    boolean quizz=false;
	    boolean b=true;
	    while(b){
		dso.receive(paquet);
		String st=new String(paquet.getData(),0,paquet.getLength());
		System.out.println(st);
		String[] arr = st.split(" ");
		Mess m;
		String idm=arr[1];
		int index=idmess.indexOf(idm);
		switch (arr[0]){
		case "GBYE" : 
		    if(arr[2].equals(ent.ip_next) && arr[3].equals(ent.port_udp_next)){
			m=new Mess("eybg1", ent, this);
			m.send_mess();	
			this.ent.ip_next = arr[4];
			this.ent.port_udp_next = arr[5];		
		    }else{
			if(arr[2].equals(ent.ip_next2) && arr[3].equals(ent.port_udp_next2)){
			    m=new Mess("eybg2", ent, this);
			    m.send_mess();
			    if(arr[4]==ent.ip && arr[5]==ent.port_udp){
				ent.ip_next=ent.ip_next2;
				ent.port_udp_next=ent.port_udp_next2;
				ent.ip_next2 =null;
				ent.port_udp_next2 =null;
			    }else{
				ent.ip_next2 = arr[4];
				ent.port_udp_next2 = arr[5];
			    }
			}else{
			    transferer( st, idm);
			}
		    }
		    break;
		case "EYBG" : 
		    b=false;
		    ent=null;
		    dso.close();
		    System.out.println("vous etes déconnectés");
		    break;
		case "WHOS" :
		    System.out.println("whooo");
		    if(index==-1){
			m=new Mess("memb", ent, this);
			m.send_mess();
		        transferer(st,idm);
		    }else{
			idmess.remove(index);
		    }
		    break;
		
		case "MEMB" : 
		    if(index==-1){
			transferer( st, idm);
			System.out.println(arr[2]+" "+arr[3]+" "+arr[4]+" "+idm);
		    }else{
			idmess.remove(index);
		    }
		    break;
		case "TEST" : 
		    if(index==-1){
			transferer(st, idm);
		    }else{
			test=false;
			idmess.remove(index);
		    }
		    break;
		case "APPL" : 
		    switch(arr[2]){
		    case "DIFF####" : 
			if(index==-1){
			    transferer( st, idm);
			    System.out.println(st.substring(27));
			}else{
			    idmess.remove(index);
			}
			break;
		    case "QUIZZ###" : 
			ent.quizz=true;
			q.recu(st, index);
			break;
			
		    case "TRANS###" :
			if(arr[3].equals("REQ")){
			    if(index==-1){
				    File f = new File(arr[5]);
				    if(f.exists()){
					String id_trans = Fonction.giveUniqueId();
					long nm = (f.length()-1)/463+1;
					String nummess = Fonction.long_to_little(nm);
					st="APPL "+idm+" TRANS### "+"ROK "+id_trans+" "+arr[4]+" "+arr[5]+" "+nummess;
					transferer(st,idm);	 
					try{
					    FileInputStream fi = new FileInputStream(f);
					    byte [] cont = new byte[463];
					    int lect;
					    String contenu;
					    long size_rest = f.length();
					    for(long i =0;i<nm;i++){
						fi.read(cont);
						if(size_rest>=463){
						    contenu = new String(cont);
						    size_rest=size_rest-463;
						}
						else{
						    contenu = new String(cont,0,(int)size_rest);
						}
						String no_mess = Fonction.long_to_little(i);
			       
						String size_cont = Fonction.fill(3,contenu.length());
						st ="APPL "+idm+" TRANS### "+"SEN "+id_trans+" "+no_mess+" "+size_cont+" "+contenu;
						transferer(st,idm);
					    }
					    fi.close();
					}
					catch(Exception e){
					    System.out.println("can't read file");
					}
				    }
				    else{
					transferer(st ,idm);
				    }
				}
				else{
				    System.out.println("le fichier n'est pas sur l'anneau");
				    idmess.remove(index);
				}
			    }
			    else if(arr[3].equals("ROK")){
				if(index!=-1){
				    this.maxMess = Fonction.little_to_long(arr[7]);
				    this.name=arr[6];
				}
				else{
				    transferer(st,idm);
				}
				
			    }
			    else if(arr[3].equals("SEN")){
				if(index!=-1){
				    try{
					
					FileWriter fw = new FileWriter(this.name,true);
					System.out.println("sa écrit");
					System.out.println(arr[7]);
					fw.write(arr[7]);
					fw.close();
					this.compteurTrans++;
					if(this.compteurTrans==this.maxMess){
					    this.maxMess=0;
					    this.compteurTrans=0;
					    idmess.remove(index);
					}
				    }
				    catch(Exception e){
					System.out.println("can't write file");
				    }
				}
				else{
				    transferer(st,idm);
				}
			    }
			}
			break;

			
		default :
		    System.out.println("défaut");
                    transferer( st, idm);	
		    break;
		}
	    }
	} catch(Exception e){
	    e.printStackTrace();
	}
    }


    public void add_list(String l){
	idmess.add(l);
    }
    public void quizz(String reponse){
	this.reponse=reponse;
    }
    public void test1(){
	test=true;
    }
    public void test2(){
	try{
	    Thread.sleep(2000);
	}catch(InterruptedException e){
	    e.printStackTrace();
	}
	if(!test){
	    System.out.println("test réussi");
	}else{
	    SendMulticast sm = new SendMulticast(this.ent);
	    sm.send("l'anneau est cassé");
	}
    }
    public void transferer(String s, String idm){
	ClientUdp cl=new ClientUdp(s);
	if(ent.ip_next2==null){
	    cl.send(ent.ip_next, ent.port_udp_next);
	}else{
	    idmess.add(idm);
	    cl.send(ent.ip_next, ent.port_udp_next);
	    cl.send(ent.ip_next2, ent.port_udp_next2);	
	} 
    }
   
}
