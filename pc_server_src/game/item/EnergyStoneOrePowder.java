package game.item;

import util.BmpRes;

public class EnergyStoneOrePowder extends Item{
	private static final long serialVersionUID=1844677L;
	static BmpRes[] bmp=BmpRes.load("Item/EnergyStoneOrePowder_",2);
	public BmpRes getBmp(){return bmp[tp];}
	int tp;
	public EnergyStoneOrePowder(int _tp){tp=_tp;}
	boolean cmpType(Item it){
		if(it.getClass()==getClass())return ((EnergyStoneOrePowder)it).tp==tp;
		return false;
	}
};
