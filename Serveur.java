import java.io.*;
import java.util.*;

public abstract class Serveur{//class abstraite des serveurs
    Entity ent;
    public Serveur(Entity e){
	this.ent = e;
    }
    public abstract void runServ(int run);
    
}
