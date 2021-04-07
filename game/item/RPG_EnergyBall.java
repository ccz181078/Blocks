package game.item;

import util.BmpRes;
import static util.MathUtil.*;
import game.entity.*;

public class RPG_EnergyBall extends RPG_Guided{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/RPG_EnergyBall");
	public int maxAmount(){return 2;}
	@Override
	protected Entity toEnt(){
		return new game.entity.RPG_EnergyBall(this);
	}
	@Override
	public void autoLaunch(Human h,Agent a){
		if(rnd()<0.2)super.autoLaunch(h,a);
	}
};
