package game.block;

import game.item.*;
import game.entity.DroppedItem;
import game.world.World;

public abstract class EnergyMachineBlock extends IronBasedType implements EnergyContainer{
	private static final long serialVersionUID=1844677L;
	protected SpecialItem<EnergyCell> ec;
	public void onPlace(int x,int y){
		ec=new SpecialItem<EnergyCell>(EnergyCell.class);
	}
	public int getEnergy(){
		if(ec.isEmpty())return 0;
		return ec.get().getEnergy();
	}
	public void loseEnergy(int v){
		if(v!=0)ec.get().loseEnergy(v);
	}
	public int resCap(){
		if(ec.isEmpty())return 0;
		return ec.get().resCap();
	}
	public void gainEnergy(int v){
		if(v!=0)ec.get().gainEnergy(v);
	}
	public void onDestroy(int x,int y){
		DroppedItem.dropItems(ec,x+0.5,y+0.5);
		ec=null;
		super.onDestroy(x,y);
	}
	public boolean isDeep(){return true;}
	@Override
	protected int crackType(){return 0;}
}

