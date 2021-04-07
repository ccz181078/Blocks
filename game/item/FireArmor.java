package game.item;
import util.BmpRes;
import game.entity.*;
import game.world.World;
import static java.lang.Math.*;
import static util.MathUtil.*;


public class FireArmor extends IronArmor{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/FireArmor");
	@Override public BmpRes getBmp(){return bmp;}
	public int maxDamage(){return 6000;}
	@Override
	public void onUpdate(Human hu){
		last_attacked_val*=0.99;
		if(rnd()<0.2){
			for(Agent e:World.cur.getNearby(hu.x,hu.y,3,3,false,false,true).agents){
				double d=hu.distL2(e);
				if(d>3||rnd()<0.5)continue;
				if(e!=hu){
					Spark.explode(e.x+rnd_gaussion()*e.width()*0.5,e.y+rnd_gaussion()*e.height()*0.5,e.xv,e.yv,rf2i(4/(1+d)),0.05,1,SourceTool.item(hu,this));
				}
			}
			int x=rf2i(hu.x+rnd_gaussion()*2);
			int y=rf2i(hu.y+rnd_gaussion()*2);
			World.cur.get(x,y).onFireUp(x,y);
		}
	}
	double last_attacked_val=0;
	public Attack transform(Attack a){
		damage+=rf2i(a.val);
		double delta=a.val;
		if(a instanceof FireAttack)a.val=min(delta,sqrt(last_attacked_val+delta)-sqrt(last_attacked_val))*0.3;
		last_attacked_val+=delta;
		return super.transform(a);
	}
}
