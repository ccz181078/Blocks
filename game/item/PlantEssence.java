package game.item;

import util.BmpRes;

public class PlantEssence extends Item{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/PlantEssence");
	public BmpRes getBmp(){return bmp;}
	public int foodVal(){return 8;}
	public int fuelVal(){return 80;}
	public int heatingTime(boolean in_furnace){return 20;}
	public Item heatingProduct(boolean in_furnace){return new CarbonPowder();}
	public int eatTime(){return 8;}
	public double hardness(){return game.entity.NormalAttacker.POWDER;}
	
	@Override
	public void onLaunchAtPos(game.entity.Agent a,int dir,double x,double y,double slope,double mv2){
		a.throwEntAtPos(new game.entity.GreenBall(),dir,x,y,slope,mv2);
	}
};
