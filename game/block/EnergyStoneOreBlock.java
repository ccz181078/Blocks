package game.block;

import static util.MathUtil.*;
import util.BmpRes;
import game.item.EnergyStoneOre;
import game.item.Item;
import game.item.EnergyStone;

public class EnergyStoneOreBlock extends StoneBlock{
private static final long serialVersionUID=1844677L;
	static BmpRes[] bmp=BmpRes.load("Block/EnergyStoneOreBlock_",3);
	public BmpRes getBmp(){return bmp[tp];}
	int tp;
	int maxDamage(){return 160;}
	public EnergyStoneOreBlock(int _tp){tp=_tp;}
	public void onDestroy(int x,int y){
		super.onDestroy(x,y);
		Item it;
		if(tp<2)it=new EnergyStoneOre(tp);
		else it=new EnergyStone();
		it.drop(x,y,rndi(2,4));
	}
	
	@Override
	public boolean cmpType(game.item.Item b){
		if(b.getClass()==getClass())return ((EnergyStoneOreBlock)b).tp==tp;
		return false;
	}
	
};
