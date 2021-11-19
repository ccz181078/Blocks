package game.item;

import util.BmpRes;
import game.entity.*;

public class SmallSpringBall extends StoneBall{
	public static BmpRes bmp[]=BmpRes.load("Item/SmallSpringBall_",4);
	int tp;
	public SmallSpringBall(int tp){
		this.tp=tp;
	}
	@Override
	public boolean cmpType(Item item){
		return super.cmpType(item)&&((SmallSpringBall)item).tp==tp;
	}
	public BmpRes getBmp(){return bmp[tp];}
	public Entity toEnt(){
		return new game.entity.SmallSpringBall(tp);
	}
	public String getName(){return util.AssetLoader.loadString(getClass(),tp+".name");}//获取名字
	public String getDoc(){return util.AssetLoader.loadString(getClass(),tp+".doc");}//获取说明
}