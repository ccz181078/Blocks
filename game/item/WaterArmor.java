package game.item;

import static java.lang.Math.*;
import static util.MathUtil.*;
import game.entity.*;
import util.BmpRes;
import static game.ui.UI.drawProgressBar;

public class WaterArmor extends IronArmor{
	private static final long serialVersionUID=1844677L;
	private static final double PAR1=200;
	public int maxDamage(){return 1000;}
	protected double toolVal(){return 0.5;}
	
	private static BmpRes bmp_armor=new BmpRes("Item/WaterArmor");
	public BmpRes getBmp(){return bmp_armor;}
	@Override
	public double light(){return 1;}
	
	@Override
	public void onUpdate(Human hu){
		damage+=f2i(last_attacked_val*0.01);
		last_attacked_val*=0.99;
	}
	double last_attacked_val=0;
	public Attack transform(Attack a){
		if(a instanceof FireAttack){
			last_attacked_val+=a.val;
			a.val*=last_attacked_val/(last_attacked_val+PAR1);
		}
		return super.transform(a);
	}
	public void onBroken(double x,double y){
		new EnergyStone().drop(x,y,rndi(1,3));
		super.onBroken(x,y);
	}
	public void drawLeftInfo(graphics.Canvas cv){
		super.drawLeftInfo(cv);
		float damage=(float)(last_attacked_val/(last_attacked_val+PAR1));
		drawProgressBar(cv,0x80ff8000,0,damage,2.1f,0.1f,3.9f,0.3f);
	}
}
