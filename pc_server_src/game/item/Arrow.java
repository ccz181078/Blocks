package game.item;

import util.BmpRes;
import static util.MathUtil.*;

public class Arrow extends Item{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/Arrow"),bmps[]=BmpRes.load("Item/Arrow_",3);
	public BmpRes getBmp(){return bmp;}
	public BmpRes getBmp(boolean on_fire){
		if(on_fire)return bmps[rndi(0,2)];
		return bmp;
	}
	
};
