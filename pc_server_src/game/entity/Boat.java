package game.entity;
import game.block.Block;
import util.BmpRes;

public class Boat extends Entity{
	private static final long serialVersionUID=1844677L;
	public double width(){return 0.4;}
	public double height(){return 0.4;}
	public Boat(){
		hp=10;
	}

	@Override
	public BmpRes getBmp(){return game.item.Boat.bmp;}
	public void onAttacked(Attack att){
		if(att instanceof FireAttack)hp-=att.val*10;
		super.onAttacked(att);
	}

	void onKill(){
		new game.item.Boat().drop(x,y);
	}
	
	void touchAgent(Agent ent){
		if(ent.bottom>bottom){
			xv=ent.xv;
			yv=ent.yv;
			ent.inblock+=1;
			/*ent.xa+=(x-ent.x)*0.04;
			ent.ya+=0.06*(top-ent.bottom);
			if(ent.xdir!=0){
				xa+=ent.xdir*0.03;
			}*/
		}else super.touchAgent(ent);
	}
	
}
