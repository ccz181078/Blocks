package game.item;

import util.BmpRes;
import game.block.Block;
import static util.MathUtil.*;
import static java.lang.Math.*;
import game.entity.*;
import game.world.World;

public class EnergySubmachineGun extends EnergyGun{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/EnergySubmachineGun");
	public BmpRes getBmp(){return bmp;}
	public int maxDamage(){return 100;}
	public EnergySubmachineGun(){}
	public double mv2(){return super.mv2()*rnd(0.6,1);}
	@Override
	public Item clickAt(double x,double y,Agent w){
		w.dir=(x-w.x>0?1:-1);
		if(!ready())return this;
		y-=w.y;
		x-=w.x+w.dir*w.width();
		if(x*w.dir>0&&shootCond()){
			Bullet b=bullet.get();
			shoot(y/x+rnd_gaussion()*0.08,w);
			int cd=2;
			if(b instanceof FlakBullet || b instanceof FireBullet || b instanceof PathBullet || b instanceof GuidedBullet)cd+=4;
			resetCd(cd);
		}
		return this;
	}
	@Override
	public Entity test_shoot(Human w,double x,double y){
		if(!ready())return null;
		
		y-=w.y;
		x-=w.x+w.dir*w.width();
		if(x*w.dir<=0)return null;
		
		Entity.beginTest();
		Entity ball=getBall();
		if(ball!=null)w.throwEnt(ball,y/x+rnd_gaussion()*0.08,mv2());
		return Entity.endTest();
	}
}

