package game.block;

import util.BmpRes;
import static util.MathUtil.*;

public class CrystalBlock extends StoneType{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/CrystalBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 360;}
	public void onDestroy(int x,int y){
		new game.item.Crystal().drop(x,y,rndi(14,16));
	}
};
