package game.item;

import util.BmpRes;
import graphics.Canvas;

public class EnergyCell extends Item implements EnergyProvider,EnergyReceiver{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/EnergyCell");
	public BmpRes getBmp(){return bmp;}
	public final int maxAmount(){return 1;}
	public static EnergyCell full(){
		EnergyCell ec=new EnergyCell();
		ec.energy=ec.maxEnergy();
		return ec;
	}
	public int maxEnergy(){return 100;}
	
	public int resCap(){return maxEnergy()-energy;}
	public void gainEnergy(int v){energy+=v;}
	
	public int getEnergy(){return energy;}
	public void loseEnergy(int v){energy-=v;}
	
	public int energy=0;
	public void drawInfo(Canvas cv){
		game.ui.UI.drawProgressBar(cv,0xff00ffff,0xff007f7f,energy*1f/maxEnergy(),-0.4f,-0.4f,0.4f,-0.33f);
	}
};
