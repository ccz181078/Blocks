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
		game.entity.Fragment.gen(x+0.5,y+0.5,0.5,0.5,4,4,6,getBmp());
		if(this instanceof OreBlockType){
			OreBlockType o=((OreBlockType)this);
			if(o.hasStone()){
				new Stone().drop(x,y,rndi(2,3)+rndi(2,3));
			}
			o.dropAt(x,y);
		}else{
			new Stone().drop(x,y,rndi(2,3)+rndi(2,3));
		}
	}
	public Block toStone(){
		Block b=new StoneBlock();
		b.damage=damage;
		return b;
	}
	public void onLight(int x,int y,double v){
		if(rnd()<0.01*v){
			if(this instanceof OreBlockType){
				OreBlockType o=((OreBlockType)this);
				if(o.hasStone())World.cur.set(x,y,new GravelBlock());
				else World.cur.setAir(x,y);
				o.dropAt(x,y);
			}else if(getClass()==StoneBlock.class){
				World.cur.set(x,y,new GravelBlock());
			}
		}
	}
};

interface OreBlockType{
	public void dropAt(int x,int y);
	default public boolean hasStone(){
		return true;
	}
}
