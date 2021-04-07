package game.block;

import static java.lang.Math.*;
import static util.MathUtil.*;
import util.BmpRes;
import game.item.Item;
import game.world.World;
import game.entity.Entity;
import game.item.Grass;
import game.item.Scissors;

public abstract class SingleBlockPlantType extends PlantType{
	private static final long serialVersionUID=1844677L;
	int tp=0;
	public int maxDamage(){return 1;}
	public boolean isSolid(){return false;}
	public boolean isCoverable(){return true;}
	public double transparency(){return 0;}
	public void touchEnt(int x,int y,Entity e){}

	public boolean cmpType(game.item.Item b){
		if(b.getClass()==getClass())return ((SingleBlockPlantType)b).tp==tp;
		return false;
	}
	
	public boolean onCheck(int x,int y){
		if(super.onCheck(x,y))return true;
		if(light_v<0||!World.cur.get(x,y-1).isSolid()){
			World.cur.setAir(x,y);
			return true;
		}
		return false;
	}
	public void onDestroy(int x,int y){}
}