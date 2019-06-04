package game.entity;

import static util.MathUtil.*;
import game.item.Stone;
import util.BmpRes;
import game.block.Block;
import static java.lang.Math.*;

public class StoneBall extends Entity{
	private static final long serialVersionUID=1844677L;
	public double width(){return 0.4;}
	public double height(){return 0.4;}
	public StoneBall(){
		hp=10;
	}
	public void update(){
		super.update();
		if(xdep!=0||ydep!=0||in_wall)hp-=0.5;
		hp-=0.03;
	}
	double friction(){return 0.2;}

	void touchAgent(Agent ent){
		double k=(min(right,ent.right)-max(left,ent.left))*(min(top,ent.top)-max(bottom,ent.bottom))/V;
		k=min(hp,k*3);
		ent.onAttacked(k,src);
		hp-=k;
		super.touchAgent(ent);
	}
	
	void onKill(){
		new Stone().drop(x,y,rndi(4,6));
	}
	public BmpRes getBmp(){return new game.item.StoneBall().getBmp();}
}
