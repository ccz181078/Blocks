package game.item;

import util.BmpRes;
import static util.MathUtil.rnd;

public class HeatEnergyCell extends EnergyCell{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/HeatEnergyCell");
	public BmpRes getBmp(){return bmp;}
	public static HeatEnergyCell full(){
		HeatEnergyCell h=new HeatEnergyCell();
		h.energy=h.maxEnergy();
		return h;
	}
	public int heatingTime(boolean in_furnace){return in_furnace?5:1000000000;}
	public Item heatingProduct(boolean in_furnace){
		if(!in_furnace||rnd()<0.0001)return null;
		HeatEnergyCell ec=(HeatEnergyCell)clone();
		ec.energy=Math.min(energy+1,maxEnergy());
		return ec;
	}
};
