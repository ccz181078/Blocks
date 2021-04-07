package game.block;

import util.BmpRes;
import game.item.Item;
import static util.MathUtil.*;
import game.world.World;

public class EnergySandBlock extends DirtType{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/EnergySandBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 40;}
	public void onLight(int x,int y,double v){}
};
