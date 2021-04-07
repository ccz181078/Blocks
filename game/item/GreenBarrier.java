package game.item;

import util.BmpRes;
import game.block.WireBlock;
import game.world.World;
import game.block.Block;
import static util.MathUtil.*;
import static java.lang.Math.*;
import game.entity.*;

public class GreenBarrier extends Item{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/GreenBarrier");
	public BmpRes getBmp(){return bmp;}
	@Override
	public int maxAmount(){return 4;}
	@Override
	public void onLaunchAtPos(game.entity.Agent a,int dir,double x,double y,double slope,double mv2){
		a.throwEntAtPos(new game.entity.GreenBall().setHpScale(8),dir,x,y,slope,mv2);
	}
};
