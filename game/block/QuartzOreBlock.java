package game.block;

import static util.MathUtil.*;
import util.BmpRes;
import game.world.World;

public class QuartzOreBlock extends StoneBlock implements OreBlockType{
	static BmpRes bmp=new BmpRes("Block/QuartzOreBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 160;}
	public void dropAt(int x,int y){
		new game.item.Quartz().drop(x,y,rndi(3,5));
		if(rnd()<0.1)new game.item.Crystal().drop(x,y,1);
	}
};
