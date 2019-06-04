package game.item;

import util.BmpRes;
import graphics.Canvas;

public class BlueCrystalEnergyCell extends EnergyCell{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/BlueCrystalEnergyCell");
	public BmpRes getBmp(){return bmp;}
	public int maxEnergy(){return 6400;}
	public static BlueCrystalEnergyCell full(){
		BlueCrystalEnergyCell b=new BlueCrystalEnergyCell();
		b.energy=b.maxEnergy();
		return b;
	}
};
