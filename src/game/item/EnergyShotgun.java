package game.item;

import util.BmpRes;
import game.block.Block;
import static util.MathUtil.*;
import static java.lang.Math.*;
import game.entity.Human;
import game.entity.Entity;

public class EnergyShotgun extends EnergyGun{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/EnergyShotgun");
	public BmpRes getBmp(){return bmp;}
	public int maxDamage(){return 1000;}
	public int energyCost(){return 2;}
	double speed(){return 0.3;}

	public void shoot(double s,Launcher w){
		for(int t=0;t<4&&shootCond();++t)super.shoot(max(-0.7,min(0.7,s))+rnd(-0.3,0.3),w);
	}
}

