
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Transfert {

    ServeurUdp serv;//le serveur sur le quel on reçoit le message
    String s;
    long compteurTrans;
    long maxMess;
    String name;
    FileOutputStream fw;//le fichier sur lequel on va écrire le fichier transferer

    public Transfert(ServeurUdp su) {
        this.maxMess = 0;
        this.compteurTrans = 0;
        this.serv = su;
    }

    public Transfert(ServeurUdp a, String id, String sa) {
        this.serv = a;
        this.s = sa;
    }
    //fonction principal de transfert qui gère les différents cas receveur ou envoyeur de fichier
    public void transfert(byte[] data,String arr[], int index, String idm) { 
	switch (arr[3]) {
            case "REQ":
                req(arr[4], arr[5], index, idm);
                break;
            case "ROK":
                rok(arr[6], arr[7], index, idm);
                break;
            case "SEN":
                sen(data,arr[6], index, idm);
                break;
        }
    }
    //gere une de mande de requete de fichier
    public void req(String size_name, String n, int index, String idm) {
        String st;
        if (index == -1) {//vérifie qu'on est pas sur le serveur qui à envoyé la demande
            File f = new File(n);
            if (f.exists()) {//regarde si le fichier existe
                String id_trans = Fonction.giveUniqueId();
                long nm = (f.length() - 1) / 463 + 1;
                String nummess = Fonction.long_to_little(nm);
                st = "APPL " + idm + " TRANS### " + "ROK " + id_trans + " " + size_name + " " + n + " " + nummess;
                serv.transferer(st, idm);//envoie le message rok qui permet de savoir que le fichier est sur l'anneau et le nombre de message attendu
                try {
                    FileInputStream fi = new FileInputStream(f);//ouvre le fichier à envoyer
                    int lect;
                    long size_rest = f.length();
		    String size_cont;
		    int size;//correspond à la taille du message
                    for (long i = 0; i < nm; i++) {//décompose le fichier en plusieur message 
			if(size_rest>=463){
			    size = 462;
			    size_rest=size_rest-462;
			}
			else
			    size =(int)size_rest;
			size_cont=Fonction.fill(3,size);//écrit la taille sur 3 octets si nécessaire
			String no_mess = Fonction.long_to_little(i);
			st = "APPL " + idm + " TRANS### " + "SEN " + id_trans + " " + no_mess + " " + size_cont + " ";
			byte  [] prot =st.getBytes();//recupere le message du protocole sous forme d'octet
			byte [] cont= new byte[size];
			fi.read(cont,0,size);//recupère une partie du fichier à envoyer dans le tableau d'octet cont
			byte [] fin = new byte[prot.length+cont.length];
			System.arraycopy(prot, 0, fin, 0, prot.length);
			System.arraycopy(cont, 0, fin, prot.length, cont.length);//concatène les deux tableaux d'octet pour envoyer le message
			serv.transfererB(fin, idm);
                    }
		    
                    fi.close();
                } catch (Exception e) {		    
                    System.out.println("can't read file");
                }
            } else {
                serv.transferer(this.s, idm);//transfere le message REQ à l'entité suivante
            }
        } else {
            System.out.println("le fichier n'est pas sur l'anneau");//le message REQ est revenue à l'envoyeur le fichier n'est pas sur l'anneau
            serv.idmess.remove(index);
        }
    }

    public void rok(String n, String mM, int index, String idm) {
        String st;
        if (index != -1) {
            this.maxMess = Fonction.little_to_long(mM);//recupère le nombre de message que l'on doit recevoir
            File f = new File(n);
            int i = 1;
            while (f.exists()) {//verifie qu'il n'y pas un fichier du même nom
                String new_name = "";
                String[] names = n.split("\\.");
                names[0] = names[0] + i;
                for (int j = 0; j < names.length; j++) {
		    if(j!=names.length-1)
			new_name = new_name+names[j]+".";
		    else
		    new_name = new_name+names[j];
                }
                f = new File(new_name);//redonne un nouveau nom avec le prochaine index avant l'extension
                i++;
            }
            try {
                fw = new FileOutputStream(f);//ouvre le fichier dans lequel on va copier le fichier récuperé
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    

	else {
	    serv.transferer(this.s, idm);//transfere le message si ce n'est pas l'entité demandeur du fichier

        }
    }

    public void sen(byte[] contenu,String taille, int index, String idm) {	
	int t = Fonction.taille(taille);//recupère la taille du contenu
        if (index != -1) {
            try {
                fw.write(contenu,49,t);//ecrit le contenu du fichier envoyer
                this.compteurTrans++;
                if (this.compteurTrans == this.maxMess) {//une fois le bon nombre de message reçu on réinitialise les données de transfert
                    this.maxMess = 0;
                    this.compteurTrans = 0;
                    serv.idmess.remove(index);
                    fw.close();//ferme le fichier copier
                }
            } catch (Exception e) {
                System.out.println("can't write file");
            }
        } else {
            serv.transferer(this.s, idm);
        }
    }
}
