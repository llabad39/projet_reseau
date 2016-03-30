import java.util.*

public class Entity{
	String id;
	String port_udp;
	String port_tcp;
	String ip_next;
	String port_udp_next;
	String addr_diff;
	String port_diff;
	//String ip; l'addresse de la machine locale

    public Entity(String _id,String _port_udp,String _port_tcp){
	this.id = _id;
	this.port_udp = _port_udp;
	this.port_tcp = _port_tcp;
    }

    public Entity(String _id,String _port_udp,String _port_tcp,String _addr_diff,String _port_diff){
	this.id = _id;
	this.port_udp = _port_udp;
	this.port_tcp = _port_tcp;
	this.addr_diff = _addr_diff;
	this.port_diff = _port_diff;    
}
}
