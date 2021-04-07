package game.item;

import util.BmpRes;

public class EnergyStoneOre extends Item{
	private static final long serialVersionUID=1844677L;
	static BmpRes[] bmp=BmpRes.load("Item/EnergyStoneOre_",2);
	public BmpRes getBmp(){return bmp[tp];}
	int tp;
	public EnergyStoneOre(int _tp){tp=_tp;}
	public double hardness(){return game.entity.NormalAttacker.STONE;}

	public boolean cmpType(Item it){
		if(it.getClass()==getClass())return ((EnergyStoneOre)it).tp==tp;
		return false;
	}
	
};
