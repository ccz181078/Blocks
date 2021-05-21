package game.item;

import util.BmpRes;
import game.block.Block;
import static util.MathUtil.*;
import static java.lang.Math.*;
import game.entity.*;
import game.world.World;

public class EnergyMachineGun extends EnergyGun{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/EnergyMachineGun");
	public BmpRes getBmp(){return bmp;}
	public int maxDamage(){return 1000;}
	public EnergyMachineGun(){}
	@Override
	public Item clickAt(double x,double y,Agent w){
		w.dir=(x-w.x>0?1:-1);
		if(!ready())return this;
		y-=w.y;
		x-=w.x+w.dir*w.width();
		if(x*w.dir>0&&shootCond()){
			Bullet b=bullet.get();
			shoot(y/x,w);
			int cd=3;
			if(b instanceof FlakBullet || b instanceof FireBullet || b instanceof PathBullet || b instanceof GuidedBullet)cd+=5;
			resetCd(cd);
		}
		return this;
	}
}

