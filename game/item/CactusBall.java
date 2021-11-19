package game.item;

import util.BmpRes;
import game.entity.*;
import static java.lang.Math.*;
import static util.MathUtil.*;
import game.world.World;

public class CactusBall extends StoneBall{
	public static BmpRes bmp=new BmpRes("Item/CactusBall");
	public BmpRes getBmp(){return bmp;}
	public double hardness(){return game.entity.NormalAttacker.CACTUS;}
	@Override
	protected Entity toEnt(){
		return new game.entity.CactusBall();
	}
	@Override
	int energyCost(){return 10;}
	
	public int foodVal(){return 12;}
	public int eatTime(){return 150;}
	public void using(UsingItem ent){
		if(ent.hp>120){
			Human hu=(Human)ent.ent;
			hu.loseHp(0.3,SourceTool.item(hu,this));
		}
		super.using(ent);
	}
};
