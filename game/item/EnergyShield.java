package game.item;

import game.entity.*;
import graphics.Canvas;
import util.BmpRes;
import static java.lang.Math.*;
import static util.MathUtil.*;

public class EnergyShield extends IronShield implements DefaultEnergyContainer,DefaultItemContainer{
	public Attack transform(Attack a){
		int v=rf2i(a.val);
		if(hasEnergy(v)){
			loseEnergy(v);
			//damage+=rf2i(a.val*0.1);
			a.val*=0.2;
			return a;
		}
		return super.transform(a);
	}
	/*extends EnergyTool*/
	SpecialItem<EnergyCell> ec=new SpecialItem<EnergyCell>(EnergyCell.class);
	public ShowableItemContainer getItems(){return ec;}
	public EnergyContainer getEnergyContainer(){return ec.get();}
	public void drawInfo(Canvas cv){
		super.drawInfo(cv);
		EnergyCell e=ec.get();
		if(e!=null)game.ui.UI.drawProgressBar(cv,0xff00ffff,0xff007f7f,e.getEnergy()*1f/e.maxEnergy(),-0.4f,-0.3f,0.4f,-0.23f);
	}
	/*end*/
	
	public void onBroken(double x,double y){
		new Iron().drop(x,y,util.MathUtil.rndi(4,6));
		super.onBroken(x,y);
	}
}
