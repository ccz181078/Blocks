package game.item;

import util.BmpRes;
import game.block.Block;
import static util.MathUtil.*;
import static java.lang.Math.*;
import game.entity.*;

public class EnergyDrill extends EnergyTool{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/EnergyDrill");
	public BmpRes getBmp(){return bmp;}
	public int maxDamage(){return 10000;}
	public double toolVal(){return 8;}
	public int pickaxVal(){return hasEnergy(1)?rf2i(toolVal()):1;}
	public int axVal(){return hasEnergy(1)?rf2i(toolVal()*0.8):1;}
	public int shovelVal(){return hasEnergy(1)?rf2i(toolVal()*0.6):1;}
	public int swordVal(){return hasEnergy(1)?rf2i(toolVal()):1;}
	public void onDesBlock(Block b){
		if(hasEnergy(1)&&rnd()<0.3)loseEnergy(1);
		++damage;
	}
	public void onAttack(Agent a){
		if(hasEnergy(1))loseEnergy(1);
		++damage;
	}
	public void onBroken(double x,double y){
		//...
		super.onBroken(x,y);
	}
	@Override
	public boolean autoUse(Human h,Agent a){
		findEnergyCell(h,1);
		return false;
	}
}

