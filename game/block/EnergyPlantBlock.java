package game.block;

import util.BmpRes;
import static util.MathUtil.*;
import game.world.World;
import game.item.*;

public class EnergyPlantBlock extends StoneType{
	private static final long serialVersionUID=1844677L;
	static BmpRes[] bmp=BmpRes.load("Block/EnergyPlantBlock_",8);
	public BmpRes getBmp(){
		if(gr<2)return bmp[gr];
		return bmp[2+tp];
	}
	int maxDamage(){return 50;}
	public boolean isSolid(){return false;}
	int tp=0,gr=0;
	public boolean onUpdate(int x,int y){
		if(super.onUpdate(x,y))return true;
		if(rnd()<0.005){
			if(gr<3){
				if(++gr==3)tp=rndi(1,5);
			}else{
				if(tp==1)new EnergyStone().drop(x,y);
				if(tp==2)new DarkBall().drop(x,y);
				if(tp==3)new FireBall().drop(x,y);
				if(tp==4)new PlantEssence().drop(x,y);
				if(tp==5)new BloodEssence().drop(x,y);
				gr=tp=0;
			}
		}
		return false;
	}
	static boolean placeable(int x,int y){
		Class tp=World.cur.get(x,y-1).rootBlock().getClass();
		return tp==EnergyStoneOreBlock.class||tp==EnergyStoneBlock.class;
	}
	public boolean placeAt(int x,int y){
		return placeable(x,y)&&super.placeAt(x,y);
	}
	public boolean onCheck(int x,int y){
		if(super.onCheck(x,y))return true;
		if(!placeable(x,y)){
			des(x,y,maxDamage());
		}
		return false;
	}
	public void onDestroy(int x,int y){
		tp=gr=0;
		super.onDestroy(x,y);
	}
}
