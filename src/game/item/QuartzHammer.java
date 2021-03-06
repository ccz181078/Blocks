package game.item;

import static util.MathUtil.*;
import util.BmpRes;

public class QuartzHammer extends HammerType{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/QuartzHammer");
	public BmpRes getBmp(){return bmp;}
	public double toolVal(){return 3;}
	public int maxDamage(){return 5000;}
	public void onBroken(double x,double y){
		if(rnd()<0.5)new Stick().drop(x,y);
		new Quartz().drop(x,y,rndi(2,5));
	}
}
