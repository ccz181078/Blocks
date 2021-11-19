package game.block;

import static java.lang.Math.*;
import static util.MathUtil.*;
import util.BmpRes;
import game.world.World;
import game.entity.*;
import game.item.Item;
import game.item.Coal;

public class TrunkBlock extends PlantType{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/TrunkBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 60;}
	public int foodVal(){return 4;}
	public int eatTime(){return 300;}
	public int heatingTime(boolean in_furnace){return 100;}
	public Item heatingProduct(boolean in_furnace){return new Coal();}
	double friction(){return 0.2;}//内部
	double frictionIn1(){return 0.5;}//内部
	double frictionIn2(){return 0;}//内部
	public TrunkBlock(){
		light_v=2;
		dirt_v=2;
	}
	public void touchEnt(int x,int y,Entity ent){
		/*double k=intersection(x,y,ent);
		ent.f+=k*0.5;
		ent.inblock+=k;
		ent.anti_g+=k*8;*/
		ent.climbable=true;
		super.touchEnt(x,y,ent);
	}
	public void onLight(int x,int y,double v){}
	public boolean onUpdate(int x,int y){
		if(super.onUpdate(x,y))return true;
		
		if(World.cur.get(x,y-1).isCoverable()){
			World.cur.setAir(x,y);
			new FallingBlock(x,y,this).add();
			return true;
		}
		
		if((light_v==0||dirt_v==0)&&rnd()<0.1)des(x,y,1);
		
		Block d=World.cur.get(x,y-1);
		dirt_v=max(0,dirt_v-0.0001f);
		light_v=max(0,light_v-0.003f);
		repair(0.05,0.05);
		Class tp=d.rootBlock().getClass();
		if(tp==TrunkBlock.class){
			spread((PlantType)d,0.2f);
		}else if(tp==DirtBlock.class){
			if(light_v>1){
				light_v-=1;
				dirt_v=min(5f,dirt_v+1);
			}
		}
		if(dirt_v>1){
			boolean lf=true;
			out:
			for(int i=-1;i<=1;++i){
				for(int j=1;j<=2;++j){
					if(World.cur.get(x+i,y+j).getClass()!=LeafBlock.class){
						lf=false;
						break out;
					}
				}
			}
			if(lf){
				TrunkBlock w=new TrunkBlock();
				spread(w,0.2f);
				World.cur.place(x,y+1,w);
			}
		}
		return false;
	}
	@Override
	protected int crackType(){return 0;}
};
