package game.item;

import util.BmpRes;
import static util.MathUtil.*;
import static java.lang.Math.*;
import game.entity.*;

public class RPG_Empty extends RPGItem{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/RPG_Empty");
	@Override
	protected Entity toEnt(){
		return new game.entity.RPG_Empty(this);
	}
};