package game.item;

import util.BmpRes;
import static util.MathUtil.*;
import game.block.Block;
import game.entity.*;

public class RPG_Block extends RPG_Guided implements DefaultItemContainer{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/RPG_Block");
	public int maxAmount(){return 1;}
	
	public NonOverlapSpecialItem<Block> block=new NonOverlapSpecialItem<>(Block.class);
	public ShowableItemContainer getItems(){return block;}
	
	/*public RPG_Block clone(){
		return (RPG_Block)util.SerializeUtil.deepCopy(this);
	}*/
	@Override
	protected Entity toEnt(){
		return new game.entity.RPG_Block(this);
	}
};
