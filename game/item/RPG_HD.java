package game.item;

import util.BmpRes;
import static util.MathUtil.*;
import game.entity.*;

public class RPG_HD extends RPG_Guided{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/RPG_HD");
	@Override
	protected Entity toEnt(){
		return new game.entity.RPG_HD(this);
	}
};
