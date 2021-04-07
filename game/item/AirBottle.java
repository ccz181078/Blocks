package game.item;

import util.BmpRes;
import game.entity.*;

public class AirBottle extends Item{
	private static final long serialVersionUID=1844677L;
	private static BmpRes bmp=new BmpRes("Item/AirBottle");
	public BmpRes getBmp(){return bmp;}
	
	public int maxAmount(){return 1;}
	public double hardness(){return game.entity.NormalAttacker.QUARTZ;}

	public void onUse(Human a){
		if(!(a instanceof Player))return;
		Player pl=(Player)a;
		pl.air_rate=100;
	}
	public BmpRes getUseBmp(){
		return eat_btn;
	}
}