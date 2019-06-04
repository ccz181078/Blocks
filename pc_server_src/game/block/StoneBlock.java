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
		for(int t=0;t<2;++t)
			new Stone().drop(x,y,rndi(2,3));
	}
	public Block toStone(){
		Block b=new StoneBlock();
		b.damage=damage;
		return b;
	}
};
