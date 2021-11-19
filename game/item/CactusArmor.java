package game.item;
import util.*;
import game.entity.*;

public class CactusArmor extends WoodenArmor{
	private static final long serialVersionUID=1844677L;
	private static BmpRes bmp=new BmpRes("Item/CactusArmor");
	private static BmpRes bmp_arm=new BmpRes("Armor/CactusArmor");
	@Override public BmpRes getBmp(){return bmp;}
	@Override public BmpRes getArmorBmp(){return bmp_arm;}
	public double hardness(){return game.entity.NormalAttacker.CACTUS;}
	@Override
	public void touchAgent(Human w,Agent a){
		a.onAttacked(w.v2rel(a)*5+0.1,SourceTool.armor(w,this),this);
	}
}
