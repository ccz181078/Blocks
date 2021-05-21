package game.item;

import util.BmpRes;
import static util.MathUtil.*;

public class HeatEnergyCellGroup extends EnergyCell{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/HeatEnergyCellGroup");
	public BmpRes getBmp(){return bmp;}
	int damage=0;
	public int maxEnergy(){
		return 8*super.maxEnergy();
	}
	public static HeatEnergyCellGroup full(){
		HeatEnergyCellGroup h=new HeatEnergyCellGroup();
		h.setFull();
		return h;
	}
	public int heatingTime(boolean in_furnace){return in_furnace?5:1000000000;}
	public Item heatingProduct(boolean in_furnace){
		if(!in_furnace)return null;
		if(rnd()<0.00001){
			++damage;
			if(damage>=8)return null;
		}
		HeatEnergyCellGroup ec=(HeatEnergyCellGroup)clone();
		ec.setEnergy(getEnergy()+1);
		return ec;
	}
};
