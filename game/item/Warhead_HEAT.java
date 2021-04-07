package game.item;

import util.BmpRes;
import static java.lang.Math.*;
import static util.MathUtil.*;
import game.entity.*;
import game.block.Block;
import game.block.MineBlock;
import game.block.SmokeBlock;
import game.world.World;

public class Warhead_HEAT extends Warhead{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/Warhead_HEAT");
	public BmpRes getBmp(){return bmp;}
	protected game.entity.RPG getRPG(){return (game.entity.RPG)new game.item.RPG_HEAT().toEnt();}
	public int getBallCnt(){return 30;}
	public Entity getBall(){return new HE_FireBall();}
};