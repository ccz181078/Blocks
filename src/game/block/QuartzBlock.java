package game.block;

import util.BmpRes;
import static util.MathUtil.*;

public class QuartzBlock extends StoneType{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/QuartzBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 240;}
	public void onDestroy(int x,int y){
		new game.item.Quartz().drop(x,y,rndi(14,16));
	}
};
