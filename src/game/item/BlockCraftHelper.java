package game.item;

import game.world.World;
import game.block.BlockAt;
import game.entity.DroppedItem;
import static util.MathUtil.*;

public class BlockCraftHelper implements CraftHelper,java.io.Serializable{
	private static final long serialVersionUID=1844677L;
	BlockAt ba;
	EnergyProvider ep;
	Craft cur_craft;
	long finish_time;
	public boolean free(){
		return cur_craft==null;
	}
	public BlockCraftHelper(BlockAt _ba,EnergyProvider _ep){
		ba=_ba;
		ep=_ep;
	}

	public float getCraftProcess(){
		if(free())return -1;
		return 1-(finish_time-World._.time)*1f/cur_craft.cost.time;
	}
	public int getEnergy(){
		return ep.getEnergy();
	}
	public void loseEnergy(int v){
		ep.loseEnergy(v);
	}
	public void upd(){
		if(!free()){
			if(World._.time>=finish_time){
				cur_craft.finish(this);
				cur_craft=null;
			}
			World._.checkBlock(ba.x,ba.y);
		}
	}
	public void gain(SingleItem item){
		new DroppedItem(ba.x+rnd(0.2,0.8),ba.y+1.2,item).add();
	}
	public void interrupt(){
		if(!free())cur_craft.interrupt(this);
	}
	public boolean startCraft(Craft craft){
		if(cur_craft==null){
			cur_craft=craft;
			finish_time=World._.time+craft.cost.time;
			World._.checkBlock(ba.x,ba.y);
			return true;
		}
		return false;
	}
}
