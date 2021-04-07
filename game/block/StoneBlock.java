package game.block;

import static util.MathUtil.*;
import util.BmpRes;
import game.item.Stone;
import graphics.Canvas;
import game.world.World;

public class StoneBlock extends StoneType{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/StoneBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 100;}
	public void onDestroy(int x,int y){
		new Stone().drop(x,y,rndi(2,3)+rndi(2,3));
		game.entity.Fragment.gen(x+0.5,y+0.5,0.5,0.5,4,4,6,getBmp());
	}
	public Block toStone(){
		Block b=new StoneBlock();
		b.damage=damage;
		return b;
	}
	public void onLight(int x,int y,double v){
		if(rnd()<0.001*v*v&&getClass()==StoneBlock.class){
			World.cur.set(x,y,new GravelBlock());
		}
	}
};
