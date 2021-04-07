package game.item;

import util.BmpRes;
import static java.lang.Math.*;
import static util.MathUtil.*;
import game.entity.*;
import game.block.Block;
import game.block.MineBlock;
import game.block.SmokeBlock;
import game.world.World;

public class Warhead_HE extends Warhead{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/Warhead_HE");
	public BmpRes getBmp(){return bmp;}
	protected game.entity.RPG getRPG(){return (game.entity.RPG)new game.item.RPG_HE().toEnt();}
	public int getBallCnt(){return 140;}
	public Entity getBall(){return new game.entity.FireBall();}
	@Override
	protected boolean explodeDirected(){return false;}
};