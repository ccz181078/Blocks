package game.item;

import util.BmpRes;
import static util.MathUtil.*;
import game.entity.*;

public class Tank_HEATFS extends RPGItem{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/Tank_HEATFS");
	public int maxAmount(){return 32;}
	@Override
	protected Entity toEnt(){
		return new game.entity.Tank_HEATFS(this);
	}
};
