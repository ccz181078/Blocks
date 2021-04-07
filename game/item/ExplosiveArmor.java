package game.item;

import util.BmpRes;
import game.entity.*;
import static util.MathUtil.*;

public class ExplosiveArmor extends IronArmor implements DefaultItemContainer{
	private static final long serialVersionUID=1844677L;
	public int maxDamage(){return 200;}
	private static BmpRes bmp=new BmpRes("Item/ExplosiveArmor");
	private static BmpRes bmp_armor=new BmpRes("Armor/ExplosiveArmor");
	public BmpRes getBmp(){return bmp;}
	public BmpRes getArmorBmp(){return bmp_armor;}
	public void onBroken(double x,double y){
		Source src=SourceTool.armor(null,this);
		Spark.explode(x,y,0,0,100,0.1,3,src);
		ShockWave.explode(x,y,0,0,20,0.1,0.2,src);
		Warhead w=warhead.popItem();
		if(w!=null){
			w.explode(x,y,rnd_gaussion()*0.3,rnd_gaussion()*0.3,src,Agent.temp(x,y,0.01,0.01,1,src));
		}
	}
	NonOverlapSpecialItem<Warhead> warhead=new NonOverlapSpecialItem<Warhead>(Warhead.class);
	public ShowableItemContainer getItems(){return warhead;}
}

