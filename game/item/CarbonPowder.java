package game.item;

import util.BmpRes;
import static util.MathUtil.*;

public class CarbonPowder extends Item{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/CarbonPowder");
	public BmpRes getBmp(){return bmp;}
	public int fuelVal(){return 120;}
	public double hardness(){return game.entity.NormalAttacker.POWDER;}
	@Override
	public void onLaunchAtPos(game.entity.Agent a,int dir,double x,double y,double slope,double mv2){
		for(int i=0;i<3;++i){
			game.entity.Spark ball=new game.entity.Spark(a.x,a.y,true);
			a.throwEntAtPos(ball.setHpScale(8),dir,x,y,slope+rnd_gaussion()*0.1,mv2/3*rnd(0.8,1.2));
		}
	}
};
