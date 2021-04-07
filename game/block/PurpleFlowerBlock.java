package game.block;

import static java.lang.Math.*;
import static util.MathUtil.*;
import util.BmpRes;
import game.item.Item;
import game.world.World;
import game.entity.Entity;
import game.item.*;
import game.item.Scissors;

public class PurpleFlowerBlock extends SingleBlockPlantType{
	private static final long serialVersionUID=1844677L;
	static BmpRes[] bmp=BmpRes.load("Block/PurpleFlower_",8);
	public BmpRes getBmp(){return bmp[tp];}
	public int fuelVal(){return 5;}
	public int foodVal(){return 2;}
	public PurpleFlowerBlock(){
		light_v=1;
	}
	public void onPress(int x,int y,Item it){
		it.onDesBlock(this);
		if(rnd()*15<it.axVal()){
			World.cur.setAir(x,y);
			damage=0;
			int t=tp+1;
			if(!(it instanceof Scissors))t=rf2i(t/8.);
			if(tp==6)new PurpleFlower().drop(x,y);
			if(tp==7)new PurpleFruit().drop(x,y);
			if(t>0){
				new PurpleFlowerBlock().drop(x,y);
			}
		}
	}
	public boolean onUpdate(int x,int y){
		if(super.onUpdate(x,y))return true;
		if(!(World.cur.get(x,y-1).rootBlock() instanceof DirtType)){
			World.cur.setAir(x,y);
			return true;
		}
		light_v-=(World.cur.weather==game.world.Weather._dark?0.005f:0.1f);
		if(light_v<0){
			World.cur.setAir(x,y);
			return true;
		}
		if(light_v>4&&rnd()<0.2){
			light_v-=2;
			if(rnd()<0.03){
				tp=max(0,min(7,tp+1));
				if(tp==7){
					int x1=x+(rnd()<0.5?-1:1),y1=y+rndi(-1,1);
					if(World.cur.get(x1,y1) instanceof AirBlock
					&& World.cur.get(x1,y1-1).rootBlock() instanceof DirtType){
						World.cur.place(x1,y1,new PurpleFlowerBlock());
						tp=5;
					}
				}
			}
		}
		return false;
	}
};
