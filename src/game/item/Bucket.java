package game.item;

import util.BmpRes;
import game.block.Block;
import static util.MathUtil.*;
import game.block.LavaBlock;

public class Bucket extends Tool{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/Bucket");
	public BmpRes getBmp(){return bmp;}
	protected double toolVal(){return 0;}
	public int shovelVal(){return 1;}
	public void onDesBlock(Block b){
		if(b.getClass()==LavaBlock.class)damage+=30;
		else ++damage;
	}
	public int maxDamage(){return 3000;}
	public void onBroken(double x,double y){
		new Iron().drop(x,y,rndi(1,3));
	}
}
