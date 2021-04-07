package game.item;
import util.BmpRes;
import game.entity.Human;
import static util.MathUtil.*;

public class BloodArmor extends IronArmor{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/BloodArmor");
	@Override public BmpRes getBmp(){return bmp;}
	@Override
	public void onUpdate(Human w){
		if(w.hp<w.maxHp()-0.03){
			w.addHp(0.03);
			if(rnd()<0.08)damage+=1;
		}
	}
}
