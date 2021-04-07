package game.item;

import util.BmpRes;
import graphics.Canvas;
import game.block.Block;
import game.entity.*;
import game.world.World;
import game.item.Item;
import static java.lang.Math.*;
import static util.MathUtil.*;
import game.entity.*;

public class IronAirship extends Airship{
	private static final long serialVersionUID=1844677L;
	private static BmpRes bmp=new BmpRes("Item/Airship");
	private static BmpRes bmp_armor[]=BmpRes.load("Armor/Airship_",4);
	public BmpRes getBmp(){
		int tp=max(0,min(3,damage/8000));
		return bmp_armor[tp];
	}
	public double mass(){return 15;}
	public int maxDamage(){return 32000;}
	public double fireReloadCost(){return 2f;}
	public double TorpedoReloadCost(){return 6f;}
	public float maxReload(){return 10f;}
	public double hardness(){return game.entity.NormalAttacker.IRON;}
	
	public Attack transform(Attack a){
		int t = 1;
		int v=rf2i(a.val);
		if(a instanceof FireAttack){
			damage+=v*t*1.25;
			a.val*=0.008*t;
		}else if(a instanceof DarkAttack){
			damage+=v*t;
			a.val*=0.04*t;
		}else if(a instanceof EnergyAttack){
			damage+=v*t;
			a.val=0;
		}else{
			damage+=rf2i(a.val*t*((NormalAttack)a).getWeight(this));
			a.val=0;
		}
		return a;
	}
	
	public void onBroken(double x,double y,Agent w){
		new Iron().drop(x,y,util.MathUtil.rndi(10,20));
		Source src=SourceTool.item(w,this);
		if(rnd(0,1)<0.8){
			Spark.explode(x,y,0,0,90,0.12,3,src);
			Spark.explode_adhesive(x,y,0,0,60,0.2,90,src);
			ShockWave.explode(x,y,0,0,40,0.1,0.3,src);
		}
		ShockWave.explode(x,y,0,0,36,0.1,0.6,src);
		super.onBroken(x,y,w);
	}
}