package game.item;
import game.entity.*;
import static util.MathUtil.*;
import static java.lang.Math.*;
import util.BmpRes;
import game.world.World;

public class Book extends Item{
	private static final long serialVersionUID=1844677L;
	private static BmpRes bmp=new BmpRes("Item/Book");
	public BmpRes getBmp(){return bmp;}
	
	@Override
	public Item clickAt(double tx,double ty,Agent w){
		if(abs(tx-w.x)>4||abs(ty-w.y)>4||!(w instanceof Player))return this;
		for(Agent a:World.cur.getNearby(tx,ty,0.1,0.1,false,false,true).agents){
			if(a instanceof Human){
				Human h=(Human)a;
				if(h.name==null){
					((Player)w).sendText((s)->{
						if(s.length()>15)s=s.substring(0,15);
						if(s.length()>0)h.name=s;
					});
					return null;
				}
			}
		}
		return this;
	}

	@Override
	public void onAttack(Entity e,Source src){
		if(e instanceof Human){
			Human hu=(Human)e;
			if(hu.name==null)hu.name="wuy";//"N"+rndi(0,9999);
		}
	}
}
