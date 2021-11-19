package game.item;

import util.BmpRes;

public class ShockWaveLauncher extends EnergyLauncher{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/ShockWaveLauncher");
	public BmpRes getBmp(){return bmp;}
	@Override
	public int energyCost(){return 20;}
	public double mv2(){return 0.01*0.6*0.6;}
	@Override
	public game.entity.Entity getBall(){return new game.entity.ShockWave().setHpScale(2);}
}
