package game.item;

import util.BmpRes;
import game.block.*;
import game.world.World;
import game.entity.*;
import static util.MathUtil.*;

public abstract class PureEnergyBottle extends Bottle implements BallProvider{
	public void onBroken(double x,double y,game.entity.Bottle ent){
		ent.explode(50,this,false);
	}
	@Override
	public abstract Entity getBall();
}

class EnergyStoneBottle extends PureEnergyBottle{
	private static BmpRes bmp=new BmpRes("Item/EnergyStoneBottle");
	public BmpRes getBmp(){return bmp;}
	public Entity getBall(){return new EnergyBall();}
	@Override
	public boolean autoUse(Human h,Agent a){
		if(a instanceof Player){
			if(((Player)a).armor.get()==null)return false;
		}
		return super.autoUse(h,a);
	}
}

class HeatBottle extends PureEnergyBottle{
	private static BmpRes bmp=new BmpRes("Item/HeatBottle");
	public BmpRes getBmp(){return bmp;}
	public Entity getBall(){return new game.entity.FireBall();}
}

class DarkBottle extends PureEnergyBottle{
	private static BmpRes bmp=new BmpRes("Item/DarkBottle");
	public BmpRes getBmp(){return bmp;}
	public Entity getBall(){return new game.entity.DarkBall();}
}