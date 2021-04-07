package game.item;

import game.entity.Agent;
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
	public int maxDamage(){return 200;}
	EnergyShotgun(){
		bullet=new NonOverlapSpecialItem<Bullet>(Bullet.class,32);
	}
	public int energyCost(){return 4;}
	double mv2(){return super.mv2()*rnd(0.2,0.3);}
	@Override
	public void shoot(double s, Agent w){
		for(int t=0;t<4&&shootCond();++t)super.shoot(s+rnd(-0.3,0.3),w);
	}
}

