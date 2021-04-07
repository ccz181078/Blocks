package game.item;
import util.BmpRes;
import game.entity.Human;
import game.entity.Attack;
import static util.MathUtil.*;
import game.entity.DarkAttack;

public class DarkArmor extends IronArmor{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/DarkArmor");
	@Override public BmpRes getBmp(){return bmp;}
	public int maxDamage(){return 2000;}
	@Override
	public Attack transform(Attack a){
		if(a instanceof DarkAttack)a.val*=0.05;
		return super.transform(a);
	}
	@Override
	public void onAttacked(Human w,Attack a){
		if(rnd()<0.2){
			double x1=w.x+rnd(-4,4),y1=w.y+rnd(-4,4);
			if(game.world.World.cur.noBlock(x1,y1,w.width(),w.height())){
				w.x=x1;w.y=y1;
				damage+=10;
			}
		}
	}
}
