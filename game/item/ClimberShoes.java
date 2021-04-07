package game.item;

import util.BmpRes;
import game.world.World;
import game.entity.*;
import static util.MathUtil.*;
import static java.lang.Math.*;
import static game.ui.UI.drawProgressBar;

public class ClimberShoes extends Shoes{
	private static final long serialVersionUID=1844677L;
	public static BmpRes bmp=new BmpRes("Item/ClimberShoes");
	public BmpRes getBmp(){return bmp;}
	public int maxDamage(){return 1000;}
	public double hardness(){return game.entity.NormalAttacker.IRON;}
	
	@Override
	public Attack transform(Attack a){
		return super.transform(a);
	}
	
	@Override
	public Shoes update(Human h){
		if(h.xdep<0&&h.xdir<0){
			h.anti_g+=1/h.mass();
		}
		if(h.xdep>0&&h.xdir>0){
			h.anti_g+=1/h.mass();
		}
		return super.update(h);
	}

};
