package game.item;
import util.BmpRes;
import game.entity.Human;
import static util.MathUtil.*;
import game.world.*;

public class GreenArmor extends IronArmor{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/GreenArmor");
	@Override public BmpRes getBmp(){return bmp;}
	@Override
	public void onUpdate(Human w){
		if(World.cur.weather==Weather._energystone||World.cur.weather==Weather._plant)w.addHp(0.02);
	}
}
