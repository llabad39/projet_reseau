import java.io.*;

public class Transfert{
    ServeurUdp serv;
    String idm;
    int index;
    String s;
    public Transfert(ServeurUdp a,String id,int ind,String sa){
	this.serv = a;
	this.idm = id;
	this.index=ind;
	this.s=sa;
    }
    public void transfert(String[] arr){
	switch(arr[3]){
	case "REQ":
	    req(arr[4],arr[5]);
	    break;
	case "ROK":
	    rok(arr[6],arr[7]);
	case "SEN":
	    sen(arr[7]);
	}
    }
    public void req(String size_name,String name){
	String st;
	if(this.index==-1){
	    File f = new File(name);
	    if(f.exists()){
		String id_trans = Fonction.giveUniqueId();
		long nm = (f.length()-1)/463+1;
		String nummess = Fonction.long_to_little(nm);
	        st="APPL "+idm+" TRANS### "+"ROK "+id_trans+" "+size_name+" "+name+" "+nummess;
		serv.transferer(st,this.idm);	 
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
			st ="APPL "+this.idm+" TRANS### "+"SEN "+id_trans+" "+no_mess+" "+size_cont+" "+contenu;
			serv.transferer(st,this.idm);
		    }
		    fi.close();
		}
		catch(Exception e){
		    System.out.println("can't read file");		}
	    }
	    else{
		serv.transferer(this.s ,this.idm);
	    }
	}
	else{
	    System.out.println("le fichier n'est pas sur l'anneau");
	    serv.idmess.remove(index);
	}
    }
    public void rok(String name,String maxMess){
	if(index!=-1){
	    serv.maxMess = Fonction.little_to_long(maxMess);
	    serv.name=name;
	}
	else{
	    serv.transferer(this.s,this.idm);
	}
    }
    public void sen(String contenu){
	if(this.index!=-1){
	    try{
		
		FileWriter fw = new FileWriter(serv.name,true);
		fw.write(contenu);
		fw.close();
		serv.compteurTrans++;
		if(serv.compteurTrans==serv.maxMess){
		    serv.maxMess=0;
		    serv.compteurTrans=0;
		    serv.idmess.remove(index);
		}
	    }
	    catch(Exception e){
		System.out.println("can't write file");
	    }
	}
	else{
	    serv.transferer(this.s,idm);
	}
    }
}
