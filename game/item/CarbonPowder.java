package game.item;

import util.BmpRes;
import game.entity.*;
import static util.MathUtil.*;

public class CarbonPowder extends Item{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/CarbonPowder");
	public BmpRes getBmp(){return bmp;}
	public int fuelVal(){return 120;}
	public double hardness(){return game.entity.NormalAttacker.POWDER;}
	public void onExplode(Entity pos,double tx,double ty,int amount,Source src){
		Spark.explode(pos.x,pos.y,pos.xv,pos.yv,amount*3,0.1,8,src);
	}
	// 1 CarbonPowder ~ 25 Spark hp unit 
	@Override
	public void onLaunchAtPos(game.entity.Agent a,int dir,double x,double y,double slope,double mv2){
		for(int i=0;i<3;++i){
			Spark ball=new Spark(a.x,a.y,true);
			a.throwEntAtPos(ball.setHpScale(8),dir,x,y,slope+rnd_gaussion()*0.1,mv2/3*rnd(0.8,1.2));
		}
	}
};
