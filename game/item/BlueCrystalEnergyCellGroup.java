package game.item;

import util.BmpRes;
import graphics.Canvas;

public class BlueCrystalEnergyCellGroup extends BlueCrystalEnergyCell{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/BlueCrystalEnergyCellGroup");
	public BmpRes getBmp(){return bmp;}
	public int maxEnergy(){return super.maxEnergy()*8;}
	public static BlueCrystalEnergyCellGroup full(){
		BlueCrystalEnergyCellGroup b=new BlueCrystalEnergyCellGroup();
		b.setFull();
		return b;
	}
};
