package game.item;

import static util.MathUtil.*;
import util.BmpRes;

public class CrystalHammer extends HammerType{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/CrystalHammer");
	public BmpRes getBmp(){return bmp;}
	public double toolVal(){return 4;}
	public int maxDamage(){return 8000;}
	public double hardness(){return game.entity.NormalAttacker.QUARTZ;}
	public void onBroken(double x,double y){
		if(rnd()<0.5)new Stick().drop(x,y);
		new Crystal().drop(x,y,rndi(2,5));
		super.onBroken(x,y);
	}
}
