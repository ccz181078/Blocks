package game.item;

import util.BmpRes;
import game.block.Block;
import static util.MathUtil.*;
import static java.lang.Math.*;
import game.entity.Human;
import game.entity.Entity;

public class EnergyGun extends EnergyBallLauncher{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/EnergyGun");
	SpecialItem<Bullet> bullet=new SpecialItem<Bullet>(Bullet.class);
	public BmpRes getBmp(){return bmp;}
	public int maxDamage(){return 1000;}
	public int energyCost(){return 2;}
	double speed(){return 0.3;}
	public boolean shootCond(){
		return !bullet.isEmpty()&&hasEnergy(energyCost());
	}
	public Entity getBall(){
		Bullet w=bullet.popItem();
		return w.asEnt();
	}

	public ShowableItemContainer getItems(){
		return ItemList.create(ec,bullet);
	}
	
}

