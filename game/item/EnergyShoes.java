package game.item;

import util.BmpRes;
import game.world.World;
import game.entity.*;
import static util.MathUtil.*;
import static game.ui.UI.drawProgressBar;
import graphics.Canvas;

public abstract class EnergyShoes extends Shoes implements DefaultEnergyContainer,DefaultItemContainer{
	public static final double E0=EnergyTool.E0;
	
	public void drawLeftInfo(Canvas cv){
		super.drawLeftInfo(cv);
		float damage=1-this.damage*1f/maxDamage();
		float c1=0,c2=1;
		EnergyCell e=ec.get();
		if(e!=null){
			c1=e.getEnergy();
			c2=resCap()+c1;
		}
		float c=c1/c2;
		drawProgressBar(cv,0xa000ffff,0,c,4.1f,0.2f,4.5f,0.3f);
	}
	
	@Override
	public double light(){return hasEnergy(1)?1:0;}
	public void onUse(Human a){//按下使用按钮
		a.items.getSelected().insert(this);
		if((a instanceof Player)){
			((Player)a).openDialog(new game.ui.UI_Item(this,getItems()));
		}
	}
	public BmpRes getUseBmp(){//获取使用按钮的贴图
		return use_btn;
	}
	
	/*extends EnergyTool*/
	public SpecialItem<EnergyCell> ec=new SpecialItem<EnergyCell>(EnergyCell.class);
	public double toolVal(){return 0;}
	public ShowableItemContainer getItems(){return ec;}
	public EnergyContainer getEnergyContainer(){return ec.get();}

	public void drawInfo(Canvas cv){
		super.drawInfo(cv);
		EnergyCell e=ec.get();
		if(e!=null)game.ui.UI.drawProgressBar(cv,0xff00ffff,0xff007f7f,e.getEnergy()*1f/e.maxEnergy(),-0.4f,-0.3f,0.4f,-0.23f);
	}
	/*end*/
};
