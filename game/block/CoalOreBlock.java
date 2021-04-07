package game.block;

import static util.MathUtil.*;
import util.BmpRes;
import game.item.Coal;
import game.world.World;
import static java.lang.Math.*;

public class CoalOreBlock extends StoneBlock{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/CoalOreBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 90;}
	public int fuelVal(){return 100;}
	public void onDestroy(int x,int y){
		super.onDestroy(x,y);
		new Coal().drop(x,y,rndi(1,2));
	}
	public void onDestroyByFire(int x,int y){
		if(rnd()<0.3){
			StoneBlock w=new StoneBlock();
			w.damage=damage;
			World.cur.place(x,y,w);
		}else{
			super.onDestroy(x,y);
		}
	}
};
