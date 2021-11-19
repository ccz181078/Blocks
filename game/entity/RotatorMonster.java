package game.entity;

import util.BmpRes;
import static util.MathUtil.*;
import graphics.Canvas;

public class RotatorMonster extends SimpleAgent{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Entity/RotatorMonster");
	public BmpRes getBmp(){return bmp;}
	public double width(){return 0.4;}
	public double height(){return 0.4;}
	public double maxHp(){return 20;}
	Group group(){return Group.STONE;}
	public RotatorMonster(double _x,double _y){super(_x,_y);}
	public double hardness(){return game.entity.NormalAttacker.STONE;}
	
	float angle=0;
	int rotating=0;
	
	@Override
	public void update(){
		super.update();
		if(rotating==0&&rnd()<0.005){
			rotating=rndi(-1,1);
		}
		if(rotating!=0){
			angle+=rotating*17;
			if(rnd()<0.007){
				angle=0;
				rotating=0;
			}
		}
	}

	@Override
	public void action(){
		if(rotating!=0){
			xdir=0;
			if((rotating==1?ydep:-ydep)<0)xa-=0.05;
			if((rotating==1?ydep:-ydep)>0)xa+=0.05;
		}
		super.action();
	}
	
	

	void touchAgent(Agent ent){
		ent.onAttacked(rotating==0?0.2:1,this);
		target=ent;
		super.touchAgent(ent);
	}

	void onKill(){
		new game.item.Stone().drop(x,y,rndi(4,6));
		super.onKill();
	}

	@Override
	public float getRotation(){
		return angle;
	}
	
}
