package game.item;

import util.BmpRes;
import graphics.Canvas;
import game.block.Block;
import game.entity.*;
import game.world.World;
import game.item.Item;
import static java.lang.Math.*;
import static util.MathUtil.*;
import static game.ui.UI.drawProgressBar;

public class EnergyArmor extends IronArmor implements DefaultEnergyContainer,DefaultItemContainer{
	private static final long serialVersionUID=1844677L;
	public static final double E0=EnergyTool.E0;
	long last_fix_time=0;
	@Override
	public void drawLeftInfo(graphics.Canvas cv){
		super.drawLeftInfo(cv);
		float c1=0,c2=1;
		EnergyCell e=ec.get();
		if(e!=null){
			c1=e.getEnergy();
			c2=resCap()+c1;
		}
		float c=c1/c2;
		drawProgressBar(cv,0xa000ffff,0,c,2.1f,0.2f,3.9f,0.3f);
	}
	@Override
	public double light(){return hasEnergy(1)?1:0;}
	/*public void onUse(Human a){//按下使用按钮
		a.items.getSelected().insert(this);
		if((a instanceof Player)){
			((Player)a).openDialog(new game.ui.UI_Item(this,getItems()));
		}
	}
	public BmpRes getUseBmp(){//获取使用按钮的贴图
		return use_btn;
	}*/
	
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
	
	protected void onBrokenTip(){
		World.showText(">>> "+getName()+" 被破坏了");
	}
	public void onBroken(double x,double y){
		onBrokenTip();
		new Iron().drop(x,y,util.MathUtil.rndi(1,3));
		super.onBroken(x,y);
	}
}
