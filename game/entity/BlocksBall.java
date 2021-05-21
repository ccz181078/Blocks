package game.entity;

import util.BmpRes;
import static util.MathUtil.*;
import static java.lang.Math.*;
import game.block.Block;
import game.item.*;

public class BlocksBall extends BigIronBall{
private static final long serialVersionUID=1844677L;
	NonOverlapSpecialItem<Block> block=new NonOverlapSpecialItem<Block>(Block.class,16);
	public BlocksBall(SpecialItem<Block> block){
		super();
		hp=200;
		if(!Entity.is_test)this.block.insert(block);
	}
	public double mass(){return 1;}
	void touchBlock(int x,int y,Block b){
		super.touchBlock(x,y,b);
		if(!b.isCoverable())hp-=3;
	}
	void onKill(){
		Fragment.gen(x,y,width(),height(),4,4,8,getBmp());
		Source ex=SourceTool.explode(this);
		while(!block.isEmpty()){
			Block b=block.popItem();
			if(!b.fallable())break;
			double xd=rnd_gaussion()*0.5;
			double yd=rnd_gaussion()*0.5;
			new FallingBlock(0,0,b).initPos(x+xd,y+yd,xv+xd,yv+yd,ex).setHpScale(4).add();
		}
		if(!block.isEmpty())DroppedItem.dropItems(block,x,y);
	}
	public BmpRes getBmp(){return game.item.BlocksBall.bmp;}
}
