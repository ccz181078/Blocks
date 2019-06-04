package game.block;

import static util.MathUtil.*;
import util.BmpRes;
import game.item.Item;
import game.world.World;
import game.entity.Entity;
import game.item.Grass;
import game.item.Scissors;

public class GrassBlock extends PlantType{
	private static final long serialVersionUID=1844677L;
	static BmpRes[] bmp=BmpRes.load("Block/GrassBlock_",3);
	public BmpRes getBmp(){return bmp[tp];}
	int maxDamage(){return 1;}
	public int fuelVal(){return 5;}
	public boolean isSolid(){return false;}
	public boolean isCoverable(){return true;}
	public void touchEnt(int x,int y,Entity e){}
	public void onPress(int x,int y,Item it){
		it.onDesBlock(this);
		if(rnd()*15<it.axVal()){
			World._.setAir(x,y);
			damage=0;
			int t=tp+1;
			if(!(it instanceof Scissors))t=rf2i(t/8.);
			if(t>0)new Grass().drop(x,y,t);
		}
	}
	int tp=0;
	public GrassBlock(int _tp){
		light_v=1;
		tp=_tp;
	}

	public boolean cmpType(Block b){
		if(b.getClass()==getClass())return ((GrassBlock)b).tp==tp;
		return false;
	}
	
	public boolean onCheck(int x,int y){
		if(super.onCheck(x,y))return true;
		if(light_v<0||!World._.get(x,y-1).isSolid()){
			World._.setAir(x,y);
			return true;
		}
		return false;
	}
	public boolean onUpdate(int x,int y){
		if(super.onUpdate(x,y))return true;
		if(World._.get(x,y-1).rootBlock().getClass()!=DirtBlock.class){
			World._.setAir(x,y);
			return true;
		}
		light_v-=(World._.weather==game.world.Weather._dark?0.005f:0.1f);
		if(light_v<0){
			World._.setAir(x,y);
			return true;			
		}
		if(light_v>4&&tp<=1&&rnd()<0.05){
			light_v-=2;
			if(rnd()<0.1){
				++tp;
				if(tp==2){
					for(int i=-1;i<=1;++i)
					for(int j=-1;j<=1;j+=2){
						Block b=World._.get(x+j,y+i);
						if(b.rootBlock().getClass()==GrassBlock.class){
							if(((GrassBlock)b).tp==2){tp=1;}
						}
					}
				}
			}
		}
		if(light_v>4&&tp>=1&&rnd()<0.3){
			int x1=x+(rnd()<0.5?-1:1),y1=y+rndi(-1,1);
			if(World._.get(x1,y1-1).rootBlock().getClass()==DirtBlock.class){
				Block b=World._.get(x1,y1);
				Class tp=b.rootBlock().getClass();
				if(tp==GrassBlock.class){
					spread((PlantType)b,0.1f);
				}else if(tp==AirBlock.class){
					b=new GrassBlock(0);
					spread((PlantType)b,0.2f);
					World._.place(x1,y1,b);
				}
			}
		}
		return false;
	}
	public void onDestroy(int x,int y){}
};
