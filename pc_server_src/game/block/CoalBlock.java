package game.block;

import util.BmpRes;
import static util.MathUtil.*;

public class CoalBlock extends StoneType{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/CoalBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 80;}
	public int fuelVal(){return 1600;}
	public void onDestroy(int x,int y){
		new game.item.Coal().drop(x,y,rndi(14,16));
	}
};
