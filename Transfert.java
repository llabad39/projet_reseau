
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Transfert {

    ServeurUdp serv;
    String s;
    long compteurTrans;
    long maxMess;
    String name;
    FileOutputStream fw;

    public Transfert(ServeurUdp su) {
        this.maxMess = 0;
        this.compteurTrans = 0;
        this.serv = su;
    }

    public Transfert(ServeurUdp a, String id, String sa) {
        this.serv = a;
        this.s = sa;
    }

    public void transfert(byte[] data,String arr[], int index, String idm) {
	switch (arr[3]) {
            case "REQ":
                req(arr[4], arr[5], index, idm);
                break;
            case "ROK":
                System.out.println("we are in the rock");
                rok(arr[6], arr[7], index, idm);
                break;
            case "SEN":
                sen(data,arr[6], index, idm);
                break;
        }
    }

    public void req(String size_name, String n, int index, String idm) {
        String st;
        System.out.println("call req");
        if (index == -1) {
            File f = new File(n);
            if (f.exists()) {
                String id_trans = Fonction.giveUniqueId();
                long nm = (f.length() - 1) / 463 + 1;
                String nummess = Fonction.long_to_little(nm);
                st = "APPL " + idm + " TRANS### " + "ROK " + id_trans + " " + size_name + " " + n + " " + nummess;
                serv.transferer(st, idm);
                try {
                    FileInputStream fi = new FileInputStream(f);
                    int lect;
                    long size_rest = f.length();
		    String size_cont;
		    int size;
                    for (long i = 0; i < nm; i++) {
			if(size_rest>463){
			    size = 463;
			    size_rest=size_rest-463;
			}
			else
			    size =(int)size_rest;
			System.out.println(size);
			size_cont=Fonction.fill(3,size);
			String no_mess = Fonction.long_to_little(i);
			st = "APPL " + idm + " TRANS### " + "SEN " + id_trans + " " + no_mess + " " + size_cont + " ";
			byte  [] prot =st.getBytes();
			byte [] cont= new byte[size];
			fi.read(cont);
			byte [] fin = new byte[prot.length+cont.length];
			System.arraycopy(prot, 0, fin, 0, prot.length);
			System.arraycopy(cont, 0, fin, prot.length, cont.length);
			System.out.println("ceci est mon string"+new String(fin));
			serv.transfererB(fin, idm);
                    }
                    fi.close();
                } catch (Exception e) {
		    e.printStackTrace();
		    System.out.println(e);
                    System.out.println("can't read file");
                }
            } else {
                serv.transferer(this.s, idm);
                System.out.println(" pas concerner");
            }
        } else {
            System.out.println("le fichier n'est pas sur l'anneau");
            serv.idmess.remove(index);
        }
    }

    public void rok(String n, String mM, int index, String idm) {
        String st;
        if (index != -1) {
            this.maxMess = Fonction.little_to_long(mM);
            //this.name = n;
            File f = new File(n);
            System.out.println(n);
            int i = 1;
            while (f.exists()) {
                String new_name = "";
                String[] names = n.split("\\.");
                names[0] = names[0] + i;
                for (int j = 0; j < names.length; j++) {
                    new_name = new_name+names[j]+".";
                }
                f = new File(new_name);
                i++;
            }
            try {
                fw = new FileOutputStream(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    

else {
            serv.transferer(this.s, idm);

        }
    }

    public void sen(byte[] contenu,String taille, int index, String idm) {
        System.out.println(index);
	//int t = Fonction.taille(taille);
        if (index != -1) {
            try {
                fw.write(contenu,49,contenu.length-49);
                this.compteurTrans++;
                if (this.compteurTrans == this.maxMess) {
                    this.maxMess = 0;
                    this.compteurTrans = 0;
                    serv.idmess.remove(index);
                    fw.close();
                }
            } catch (Exception e) {
                System.out.println("can't write file");
            }
        } else {
            serv.transferer(this.s, idm);
        }
    }
}
