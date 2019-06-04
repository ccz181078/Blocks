package game.block;

import static util.MathUtil.*;
import util.BmpRes;

public class QuartzOreBlock extends StoneBlock{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/QuartzOreBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 160;}
	public void onDestroy(int x,int y){
		super.onDestroy(x,y);
		new game.item.Quartz().drop(x,y,rndi(3,5));
		if(rnd()<0.1)new game.item.Crystal().drop(x,y,1);
	}
};
