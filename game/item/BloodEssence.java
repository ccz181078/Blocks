package game.item;

import util.BmpRes;
import static util.MathUtil.*;

public class BloodEssence extends Item{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/BloodEssence");
	public BmpRes getBmp(){return bmp;}
	public int foodVal(){return 30;}
	public int fuelVal(){return 80;}
	public int heatingTime(boolean in_furnace){return 20;}
	public Item heatingProduct(boolean in_furnace){return new CarbonPowder();}
	public int eatTime(){return 8;}
	public double hardness(){return game.entity.NormalAttacker.POWDER;}
	
	@Override
	public void onLaunchAtPos(game.entity.Agent a,int dir,double x,double y,double slope,double mv2){
		for(int i=0;i<3;++i){
			a.throwEntAtPos(new game.entity.BloodBall(a.x,a.y,10),dir,x,y,slope+rnd(-0.1,0.1),mv2/3*rnd(0.8,1.2));
		}
	}
};
