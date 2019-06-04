package game.block;

import game.item.*;
import game.entity.DroppedItem;
import game.world.World;

public abstract class CraftHelperBlock extends StoneType implements EnergyProvider{
	private static final long serialVersionUID=1844677L;
	protected SpecialItem<EnergyCell> ec;
	protected BlockCraftHelper ch;
	public void onPlace(int x,int y){
		ec=new SpecialItem<EnergyCell>(EnergyCell.class);
		ch=new BlockCraftHelper(new BlockAt(x,y,this),this);
	}
	public boolean onCheck(int x,int y){
		ch.upd();
		return super.onCheck(x,y);
	}
	public int getEnergy(){
		if(ec.isEmpty())return 0;
		return ec.get().energy;
	}
	public void loseEnergy(int v){
		ec.get().energy-=v;
	}

	public void onDestroy(int x,int y){
		ch.interrupt();
		DroppedItem.dropItems(ec,x+0.5,y+0.5);
		ec=null;
		super.onDestroy(x,y);
	}
}

