package game.block;

import static util.MathUtil.*;
import util.BmpRes;
import game.world.World;
import game.item.Item;
import game.item.Apple;
import game.world.Weather;

public class LeafBlock extends PlantType{
	private static final long serialVersionUID=1844677L;
	public int tp=0,trunk_v=0;
	private static double[] apple_x={0,0.4,0.8,0.2},apple_y={0,0.4,0.8,0.7}; 
	static BmpRes[] bmp=BmpRes.load("Block/LeafBlock_",4);
	public BmpRes getBmp(){return bmp[tp];}
	public double transparency(){return 0.55;}
	int maxDamage(){return 10;}
	public void onDestroy(int x,int y){}
	public void onPress(int x,int y,Item it){
		if(damage==0&&(it instanceof game.item.Scissors)){
			it.onDesBlock(this);
			if(rnd()*maxDamage()<1){
				World._.setAir(x,y);
				new LeafBlock(tp).asItem().drop(x,y);
			}
			return;
		}
		if(tp>0){
			new Apple().drop(x+apple_x[tp],y+apple_y[tp]);
			tp=0;
		}
		super.onPress(x,y,it);
	}
	public LeafBlock(int _tp){tp=_tp;light_v=2;}
	public void onPlace(int x,int y){}
	public boolean cmpType(Block b){
		if(b.getClass()==LeafBlock.class)return tp==((LeafBlock)b).tp;
		return false;
	}
	public boolean onClick(int x,int y,game.entity.Agent a){
		if(tp>0){
			new Apple().drop(x+apple_x[tp],y+apple_y[tp]);
			tp=0;
			return true;
		}
		return false;
	}
	public void touchEnt(int x,int y,game.entity.Entity ent){
		double k=intersection(x,y,ent);
		ent.f+=k*0.1;
		ent.inblock+=k*0.1;
		ent.anti_g+=0.1;
	}
	public boolean onUpdate(int x,int y){
		if(super.onUpdate(x,y))return true;
		if(World._.weather==Weather._energystone)++trunk_v;
		dirt_v=Math.max(0,dirt_v-0.0001f);
		light_v=Math.max(0,light_v-0.05f);
		if(light_v==0&&rnd()<0.1){
			World._.setAir(x,y);
			return true;
		}
		if(trunk_v>120)des(x,y,1);
		else repair(0.03,0.03);
		if(rnd()<0.0005){
			out:
			if(tp==0&&light_v>=3&&dirt_v>=0.3){
				for(BlockAt ba:World._.get4(x,y))if(ba.block.getClass()==LeafBlock.class){
					if(((LeafBlock)ba.block).tp!=0)break out;
				}
				light_v-=2f;
				dirt_v-=0.2f;
				tp=rndi(1,3);
			}
		}
		int x1=x;
		int y1=y;
		if(rnd()<0.27)x1+=(rnd()<0.5?-1:1);
		else y1+=(rnd()<0.5?-1:1);
		Block b=World._.get(x1,y1);
		Class tp=b.getClass();
		if(tp==LeafBlock.class){
			spread(((PlantType)b),0.1f);
			trunk_v=((LeafBlock)b).trunk_v=trunk_v+((LeafBlock)b).trunk_v>>1;
		}else if(tp==TrunkBlock.class){
			spread(((PlantType)b),x1==x?0.4f:0.1f);
			trunk_v>>=1;
		}else if(tp==AirBlock.class){
			if(dirt_v>0.5&&light_v>4){
				LeafBlock w=new LeafBlock(0);
				spread(w,0.3f);
				World._.place(x1,y1,w);
			}
		}
		return false;
	}
}
