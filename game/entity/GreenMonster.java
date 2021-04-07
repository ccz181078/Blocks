package game.entity;

import util.BmpRes;
import static util.MathUtil.*;
import static java.lang.Math.*;
import game.block.*;

public class GreenMonster extends SimpleAgent{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Entity/GreenMonster");
	public BmpRes getBmp(){return bmp;}
	public double width(){return 0.4;}
	public double height(){return 0.4;}
	public GreenMonster(double _x,double _y){super(_x,_y);cnt=1;hp=maxHp();}
	Group group(){return Group.GREEN;}
	
	int cnt=1;
	public double maxHp(){return cnt*10;}
	public double mass(){return 1;}
	
	@Override
	public double buoyancyForce(){return 2;}
	@Override
	public double fluidResistance(){return 0.4;}
	void touchAgent(Agent ent){
		if(ent.getClass()==getClass()){
			GreenMonster g=(GreenMonster)ent;
			if(distL2(g)<0.25&&!isRemoved()&&!g.isRemoved()&&cnt>=g.cnt){
				//System.out.printf("%d+%d : (%d,%d)%d\n",cnt,g.cnt,(int)x,(int)y,game.world.World.cur.time);
				cnt+=g.cnt;
				g.cnt=0;
				addHp(g.hp);
				g.remove();
				return;
			}
		}else{
			ent.onAttacked(0.2*cnt,this);
			target=ent;
		}
		super.touchAgent(ent);
	}
	
	void onKill(){
		if(getClass()==GreenMonster.class){
			//if(cnt>=2)System.out.printf("%d killed : (%d,%d)%d\n",cnt,(int)x,(int)y,game.world.World.cur.time);
			int k=rf2i(0.3*cnt);
			if(k>0)new game.item.PlantEssence().drop(x,y,k);
		}
		super.onKill();
	}
	void touchBlock(int x,int y,Block b){
		super.touchBlock(x,y,b);
		if(b instanceof PlantType && hp<maxHp()){
			if(b instanceof CactusBlock && rnd()<0.1){
				xdir=(this.x<x+0.5?-1:1);
				ydir=1;
			}
			PlantType pt=(PlantType)b;
			double c=max(0,min(min(0.01,pt.light_v),(maxHp()-hp)));
			if(c>0){
				pt.light_v-=c;
				addHp(c);
			}
		}
	}
	
}
