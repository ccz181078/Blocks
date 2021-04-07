package game.item;

import util.BmpRes;
import graphics.Canvas;

public class InfiniteEnergyCell extends EnergyCell{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/InfiniteEnergyCell");
	public BmpRes getBmp(){return bmp;}

	@Override
	public int maxEnergy(){return 1000000000;}	
	@Override
	public int resCap(){return 0;}
	@Override
	public void gainEnergy(int v){}	
	@Override
	public void loseEnergy(int v){}
	@Override
	public int getEnergy(){return 1000000000;}
	
	public static InfiniteEnergyCell full(){
		InfiniteEnergyCell b=new InfiniteEnergyCell();
		b.setFull();
		return b;
	}
	@Override
	public boolean isCreative(){return true;}
};
