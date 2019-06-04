package game.item;

import game.world.World;
import game.block.BlockAt;
import game.entity.DroppedItem;
import static util.MathUtil.*;
import game.block.Block;
import game.block.AutoCraftBlock;

public class BlockCraftHelper implements CraftHelper,java.io.Serializable{
	private static final long serialVersionUID=1844677L;
	BlockAt ba;
	EnergyProvider ep;
	Craft cur_craft;
	long finish_time;
	public boolean free(){
		Block b=World.cur.get(ba.x,ba.y+1);
		if(b instanceof AutoCraftBlock&&!(((AutoCraftBlock)b).ch.free())&&!(ba.block instanceof AutoCraftBlock))return false;
		return cur_craft==null;
	}
	public BlockCraftHelper(BlockAt _ba,EnergyProvider _ep){
		ba=_ba;
		ep=_ep;
	}

	public float getCraftProcess(){
		if(free())return -1;
		return 1-(finish_time-World.cur.time)*1f/cur_craft.cost.time;
	}
	public int getEnergy(){
		return ep.getEnergy();
	}
	public void loseEnergy(int v){
		ep.loseEnergy(v);
	}
	public void upd(){
		if(!free()&&cur_craft!=null){
			if(World.cur.time>=finish_time){
				cur_craft.finish(this);
				cur_craft=null;
			}
			World.cur.checkBlock(ba.x,ba.y);
		}
	}
	public void gain(SingleItem item){
		new DroppedItem(ba.x+rnd(0.31,0.69),ba.y+1.26,item).add();
	}
	public void interrupt(){
		if(!free())cur_craft.interrupt(this);
	}
	public boolean startCraft(Craft craft){
		if(cur_craft==null){
			cur_craft=craft;
			finish_time=World.cur.time+craft.cost.time;
			World.cur.checkBlock(ba.x,ba.y);
			return true;
		}
		return false;
	}
}
