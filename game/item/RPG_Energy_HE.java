package game.item;

import util.BmpRes;
import static util.MathUtil.*;
import game.entity.*;

public class RPG_Energy_HE extends RPGItem{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/RPG_Energy_HE");
	public int maxAmount(){return 16;}
	@Override
	protected Entity toEnt(){
		return new game.entity.RPG_Energy_HE(this);
	}
};
