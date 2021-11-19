package game.block;

import static util.MathUtil.*;
import util.BmpRes;
import game.item.IronOre;

public class IronOreBlock extends StoneBlock implements OreBlockType{
	static BmpRes[] bmp=BmpRes.load("Block/IronOreBlock_",4);
	public BmpRes getBmp(){return bmp[tp];}
	public int tp;
	int maxDamage(){return 100;}
	public IronOreBlock(int _tp){tp=_tp;}

	public boolean cmpType(game.item.Item b){
		if(b.getClass()==IronOreBlock.class)return tp==((IronOreBlock)b).tp;
		return false;
	}
	
	public boolean hasStone(){
		return tp<3;
	}
	public void dropAt(int x,int y){
		if(tp<3){
			new IronOre().drop(x,y,tp==0?1:tp==1?3:5);
		}else{
			new IronOre().drop(x,y,rndi(14,16));
		}
	}
};
