package game.entity;

import util.BmpRes;
import static util.MathUtil.*;

public class GreenMonster extends Agent{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Entity/GreenMonster");
	public BmpRes getBmp(){return bmp;}
	public double width(){return 0.4;}
	public double height(){return 0.4;}
	public GreenMonster(double _x,double _y){super(_x,_y);}
	Agent target;
	public void action(){
		if(target!=null&&target.removed)target=null;
		if(ydep<0&&ydir<0)ydir=0;
		if(target!=null&&rnd()<0.1){
			xdir=x<target.x?1:-1;
			ydir=y<target.y?1:-1;
			if(rnd()<0.03)target=null;
		}
		if(rnd()<(xdir==0?0.05:0.01)){
			xdir=rndi(-1,1);
			ydir=rndi(-1,1);
		}
		super.action();
	}
	
	void touchAgent(Agent ent){
		if(!(ent instanceof GreenMonster)){
			ent.onAttacked(0.2,this);
			target=ent;
		}
		super.touchAgent(ent);
	}
	
	void onKill(){
		if(rnd()<0.3)new game.item.PlantEssence().drop(x,y,1);
	}
	
}
