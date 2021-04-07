package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import graphics.Canvas;
import game.world.World;
import game.block.Block;
import game.item.NonOverlapSpecialItem;
import util.BmpRes;

public class RPG_Block extends RPG_Guided{
	private static final long serialVersionUID=1844677L;
	public BmpRes getBmp(){return rpg.getBmp();}
	public double width(){return 0.4;}
	public double height(){return 0.4;}
	NonOverlapSpecialItem<Block> block=new NonOverlapSpecialItem<>(Block.class);
	public RPG_Block(game.item.RPG_Block a){
		super(a);
		if(!Entity.is_test)block.insert(a.block);
	}
	public void explode(){
		Source ex=SourceTool.explode(this);
		while(!block.isEmpty()){
			Block b=block.popItem();
			if(!b.fallable())break;
			new FallingBlock(0,0,b).initPos(x,y,xv,yv,ex).kill();
		}
		if(!block.isEmpty())DroppedItem.dropItems(block,x,y);
		super.explode();
	}
}
