package game.item;

import static util.MathUtil.*;
import util.BmpRes;
import game.entity.*;

public class StoneHammer extends HammerType{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/StoneHammer");
	public BmpRes getBmp(){return bmp;}
	public double toolVal(){return 1.5;}
	public int maxDamage(){return 800;}
	public void onBroken(double x,double y){
		if(rnd()<0.5)new DroppedItem(x,y,new Stick().setAmount(1)).add();
		new Stone().drop(x,y,rndi(2,5));
		super.onBroken(x,y);
	}
	public double hardness(){return game.entity.NormalAttacker.STONE;}
}
