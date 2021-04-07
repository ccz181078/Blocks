package game.item;

import util.BmpRes;
import static util.MathUtil.*;
import game.entity.*;

public class RPG_Guided extends RPGItem{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/RPG_Guided");
	public int maxAmount(){return 2;}
	@Override
	protected Entity toEnt(){
		return new game.entity.RPG_Guided(this);
	}
	@Override
	public void autoLaunch(Human h,Agent a){
		if(rnd()<0.2)super.autoLaunch(h,a);
	}
};
