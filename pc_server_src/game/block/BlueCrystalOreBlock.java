package game.block;

import static util.MathUtil.*;
import static java.lang.Math.*;
import util.BmpRes;
import game.item.BlueCrystal;
import game.world.World;

public class BlueCrystalOreBlock extends StoneBlock{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/BlueCrystalOreBlock");
	public BmpRes getBmp(){return bmp;}
	public void onDestroy(int x,int y){
		super.onDestroy(x,y);
		new BlueCrystal().drop(x,y,rndi(2,4));
	}
};
