package game.block;

import util.BmpRes;
import static util.MathUtil.*;

public class ImprovedStoneBlock extends StoneBlock{
	static BmpRes bmp=new BmpRes("Block/ImprovedStoneBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return super.maxDamage()*4;}
	public void onDestroy(int x,int y){
		super.onDestroy(x,y);
		if(rnd()*4<1)new game.item.Iron().drop(x,y);
	}
};
