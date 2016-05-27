public class Fonction{

    public static String fill(int nb_octet,int nb){//renvoie un String du nombre avec le nombre d'octet souhaité


	String s= ""+nb;
	for(int i=s.length();i<nb_octet;i++){
	    s="0"+s;//complète les octets manquant avec des zéros avant le chiffre passé en argument
	}
	System.out.println(s+"on a fill");
	return s;
    }
    public static int taille(String a){//renvoie la valeur d'un eniter d'une chaine de caractère
	
	for(int i=0;i<a.length();i++){
	    if(a.charAt(i)!='0'){
		return Integer.parseInt(a.substring(i));
	    }
	}
	
	return 0;
    }
	
    public static String giveUniqueId(){//renvoie un String id qui est unique
	String id="";
	int i;
	for(i=0; i<8; i++){
	    id+=(int)Math.floor(Math.random()*10);
	}
	return id;
    }
    public static String long_to_little(long x){//convertit un long en String en little Indian
	char []t=new char[8];
	for(int i=0;i<8;i++){
		long b = x%256;
		t[i]=new Character((char)b);
		System.out.println(t[i]);
		x=x/256;
	    
	    
	}
	 return new String(t);
    }
    public static long little_to_long(String a){//convertit un String en little Indian en long
	long ind = 1;
	long f=0;
	
	for(int i =0;i<8;i++){
	    f = f+(long)a.charAt(i)*ind;
	    ind=ind*256;
	}
	return f;
    }
<
    
}
       


