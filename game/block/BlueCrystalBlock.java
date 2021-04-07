package game.block;

import util.BmpRes;
import static util.MathUtil.*;

public class BlueCrystalBlock extends PureStoneType{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/BlueCrystalBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 300;}
	public void onDestroy(int x,int y){
		new game.item.BlueCrystal().drop(x,y,rndi(14,16));
	}
	@Override
	protected int crackType(){return 1;}
};
