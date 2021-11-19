package game.item;

import util.BmpRes;
import static util.MathUtil.*;
import game.entity.*;

public class RPG_Bullet extends RPGItem implements DefaultItemContainer{
	private static final long serialVersionUID=1844677L;
	public static BmpRes bmp=new BmpRes("Item/RPG_Bullet");
	public BmpRes getBmp(){return bmp;}
	public int maxAmount(){return 1;}
	
	public NonOverlapSpecialItem<Bullet> bullet=new NonOverlapSpecialItem<>(Bullet.class,33);
	public ShowableItemContainer getItems(){return bullet;}
	
	@Override
	protected Entity toEnt(){
		return new game.entity.RPG_Bullet(this);
	}
	@Override
	public boolean autoUse(Human h,Agent a){
		if(bullet.isEmpty())
		for(SingleItem si:h.items.toArray()){
			bullet.insert(si);
		}
		return false;
	}
};
