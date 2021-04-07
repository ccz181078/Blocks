package game.item;

import game.entity.Agent;
import util.BmpRes;
import static java.lang.Math.*;

public class HighEnergyShockWaveLauncher extends ShockWaveLauncher{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/HighEnergyShockWaveLauncher");
	public BmpRes getBmp(){return bmp;}

	@Override
	public int maxDamage(){
		return super.maxDamage()*10;
	}
	
	@Override
	public void shoot(double s, Agent w){
		for(int i=-4;i<=4&&shootCond();++i)super.shoot(s+i*0.05,w);
	}
}

