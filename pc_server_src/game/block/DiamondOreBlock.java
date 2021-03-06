package game.block;

import static util.MathUtil.*;
import static java.lang.Math.*;
import util.BmpRes;
import game.item.Diamond;
import game.world.World;

public class DiamondOreBlock extends StoneBlock{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/DiamondOreBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 100;}
	public int fuelVal(){return 20;}
	public void onDestroyByFire(int x,int y){
		if(rnd()<0.8){
			StoneBlock w=new StoneBlock();
			w.damage=damage;
			World.cur.place(x,y,w);
		}else{
			super.onDestroy(x,y);
		}
	}
	public void onDestroy(int x,int y){
		super.onDestroy(x,y);
		new Diamond().drop(x,y,rndi(2,4));
	}
};
