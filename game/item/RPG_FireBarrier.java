package game.item;

import util.BmpRes;
import static util.MathUtil.*;
import game.entity.*;

public class RPG_FireBarrier extends RPG_Guided{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/RPG_FireBarrier");
	@Override
	protected Entity toEnt(){
		return new game.entity.RPG_FireBarrier(this);
	}
};
