package game.item;

import util.BmpRes;
import game.block.Block;
import static util.MathUtil.*;
import static java.lang.Math.*;
import game.entity.*;

public class EnergyGun extends EnergyLauncher{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/EnergyGun");
	SpecialItem<Bullet> bullet=new SpecialItem<Bullet>(Bullet.class);
	public EnergyGun(){
		bullet=new NonOverlapSpecialItem<Bullet>(Bullet.class,25);
	}
	public BmpRes getBmp(){return bmp;}
	public int maxDamage(){return 200;}
	public int energyCost(){return 2;}
	public boolean shootCond(){
		return !bullet.isEmpty()&&hasEnergy(energyCost());
	}
	public Entity getBall(){
		Bullet w=bullet.popItem();
		return w.toEnt();
	}

	public ShowableItemContainer getItems(){
		return ItemList.create(ec,bullet);
	}
	
	@Override
	public boolean autoUse(Human h,Agent a){
		h.items.insert(bullet);
		if(bullet.isEmpty()){
			SingleItem w=null;
			double mv=0;
			for(SingleItem si:h.items.toArray()){
				if(si.get() instanceof Bullet){
					Bullet b=(Bullet)si.get();
					double v=b.attackValue();
					if(b instanceof Bullet_HD)continue;
					if(v>mv){
						mv=v;
						w=si;
					}
				}
			}
			if(w!=null)bullet.insert(w);
		}
		if(bullet.isEmpty())return false;
		return super.autoUse(h,a);
	}
}

