package game.item;
import util.BmpRes;
import game.entity.Human;
import static util.MathUtil.*;
import game.world.*;

public class FlankArmor extends EnergyArmor{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp_arm=new BmpRes("Armor/FlankArmor");
	public ShowableItemContainer getItems(){return ItemList.create(ec,af1);}
	public SpecialItem<AirshipFlank> af1=new SpecialItem<>(AirshipFlank.class);
	@Override public BmpRes getBmp(){return bmp_arm;}
	@Override
	public void onUpdate(Human w){
		AirshipFlank a1=af1.get();
		if(a1!=null){
			a1.ent.update(w);
			if(a1.ent.hp<=0)af1.pop();
		}
	}
}
