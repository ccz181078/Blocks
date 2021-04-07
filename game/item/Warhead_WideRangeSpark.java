package game.item;

import util.BmpRes;
import static java.lang.Math.*;
import static util.MathUtil.*;
import game.entity.*;
import game.block.Block;
import game.block.MineBlock;
import game.block.SmokeBlock;
import game.world.World;


public class Warhead_WideRangeSpark extends Warhead{
	//1 CarbonPowdder = 25 Spark Unit
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/Warhead_WideRangeSpark");
	public BmpRes getBmp(){return bmp;}
	protected game.entity.RPG getRPG(){return (game.entity.RPG)new game.item.RPG_WideRangeSpark().toEnt();}
	public int getBallCnt(){return 75;}
	public Entity getBall(){return new Spark(0,0,true).setHpScale(4);}
};