package game.item;

import util.BmpRes;
import game.entity.*;
import game.block.Block;

public class BlocksBall extends BigIronBall implements DefaultItemContainer{
private static final long serialVersionUID=1844677L;
	public static BmpRes bmp=new BmpRes("Item/BlocksBall");
	public BmpRes getBmp(){return bmp;}
	NonOverlapSpecialItem<Block> block=new NonOverlapSpecialItem<Block>(Block.class,16);
	public ShowableItemContainer getItems(){return block;}
	public int maxAmount(){return 1;}
	public Entity toEnt(){
		return new game.entity.BlocksBall(block);
	}
};
