package game.item;

import util.BmpRes;
import game.entity.*;

public class CrossBow extends Bow{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/CrossBow");
	public BmpRes getBmp(){return bmp;}
	public int maxDamage(){return 100;}
	public int energyCost(){return super.energyCost()*2;}
	@Override
	public void onCarried(game.entity.Agent a){
		if(cd>0)--cd;
		super.onCarried(a);
	}
};
