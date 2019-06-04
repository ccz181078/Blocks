package game.item;

import util.BmpRes;

public class DarkEnergyCell extends EnergyCell{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/DarkEnergyCell");
	public BmpRes getBmp(){return bmp;}
	public static DarkEnergyCell full(){
		DarkEnergyCell ec=new DarkEnergyCell();
		ec.energy=ec.maxEnergy();
		return ec;
	}
	public int maxAmount(){return 2;}
	boolean cmpType(Item it){return this==it;}
	public Item clone(){return this;}
}
