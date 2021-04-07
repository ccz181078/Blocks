package game.block;

import static util.MathUtil.*;
import util.BmpRes;
import game.item.Coal;

public class BloodStoneBlock extends StoneBlock{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/BloodStoneBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 100;}
	public int fuelVal(){return 40;}
	public void onDestroyByFire(int x,int y){
		if(rnd()<0.5){
			StoneBlock w=new StoneBlock();
			w.damage=damage;
			game.world.World.cur.place(x,y,w);
		}else{
			super.onDestroy(x,y);
		}
	}
	public void onDestroy(int x,int y){
		super.onDestroy(x,y);
		new game.entity.BloodMonster(x+0.5,y+0.5).add();
	}
};
