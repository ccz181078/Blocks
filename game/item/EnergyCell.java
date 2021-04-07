package game.item;

import util.BmpRes;
import graphics.Canvas;
import static java.lang.Math.*;
import static util.MathUtil.*;
import game.world.StatMode.StatResult;
import game.entity.*;

public class EnergyCell extends Item implements EnergyContainer{
	private static final long serialVersionUID=1844677L;
	
	@Override
	public double getPrice(StatResult result){
		return super.getPrice(result)+result.getEnergyPrice()*(maxEnergy()-resCap());
	}
	public double hardness(){return game.entity.NormalAttacker.IRON;}

	
	static BmpRes bmp=new BmpRes("Item/EnergyCell");
	public BmpRes getBmp(){return bmp;}
	public int maxAmount(){return 1;}
	public static EnergyCell full(){
		EnergyCell ec=new EnergyCell();
		ec.setEnergy(ec.maxEnergy());
		return ec;
	}
	public int maxEnergy(){return 200;}
	
	public int resCap(){return maxEnergy()-energy;}
	public void gainEnergy(int v){energy+=v;}
	
	public int getEnergy(){return energy;}
	public void loseEnergy(int v){energy-=v;}
	public void setEnergy(int x){
		energy=max(0,min(maxEnergy(),x));
	}
	public void setFull(){
		setEnergy(maxEnergy());
	}
	
	private int energy=0;
	public void drawInfo(Canvas cv){
		game.ui.UI.drawProgressBar(cv,0xff00ffff,0xff007f7f,getEnergy()*1f/maxEnergy(),-0.4f,-0.4f,0.4f,-0.33f);
	}
	public boolean autoUse(final Human h,final Agent a){
		Armor ar=h.armor.get();
		if(ar instanceof EnergyArmor){
			((EnergyArmor)ar).insert(h.getCarriedItem());
			return false;
		}
		return false;
	}
};
