package game.item;

import util.BmpRes;
import game.block.Block;
import static util.MathUtil.*;

public class Scissors extends Tool{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/Scissors");
	public BmpRes getBmp(){return bmp;}
	protected double toolVal(){return 0;}
	public void onDesBlock(Block b){++damage;}
	public int maxDamage(){return 1000;}
	public void onBroken(double x,double y){
		new Iron().drop(x,y,rndi(1,3));
	}
}
