public class Fonction{

    public static String fill(int nb_octet,int nb){
	String s= ""+nb;
	for(int i=s.length();i<nb_octet;i++){
	    s="0"+s;
	}
	return s;
    }
    public static byte[] longToBytes(long l) {
	byte[] b = new byte[8];
	for (int i = 7; i >= 0; i--) {
	    b[i] = (byte)(l & 0xFF);
	    l >>= 8;
	}
	return b;
    }
    public static String giveUniqueId(){
	String id="";
	int i;
	for(i=0; i<8; i++){
	    id+=(int)Math.floor(Math.random()*10);
	}
	return id;
    }
    public static String long_to_little(long x){
	char []t=new char[8];
	for(int i=0;i<8;i++){
		long b = x%256;
		t[i]=new Character((char)b);
		System.out.println(t[i]);
		x=x/256;
	    
	    
	}
	 return new String(t);
    }
    public static long little_to_long(String a){
	long ind = 1;
	long f=0;
	
	for(int i =0;i<8;i++){
	    f = f+(long)a.charAt(i)*ind;
	    ind=ind*256;
	}
	return f;
    }
    public static void main(String[]args){
	int b = 3215;
	String a = long_to_little((long)b);
	System.out.println(a);
	Long c = little_to_long(a);
	System.out.println(c);
    }
}
