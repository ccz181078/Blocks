package game.entity;

import util.BmpRes;
import static java.lang.Math.*;
import static util.MathUtil.*;
import java.util.Arrays;
import java.util.Collections;

public abstract class NormalAgent extends Agent{
	private static final long serialVersionUID=1844677L;
	Enemy es[];
	public double width(){return 0.4;}
	public double height(){return 0.4;}
	public NormalAgent(double _x,double _y){
		super(_x,_y);
		es=new Enemy[4];
	}
	public void onAttacked(Attack a){
		super.onAttacked(a);
		if(a.src!=null)Enemy.ins(es,new Enemy(a.val,a.src));
	}
	protected abstract Entity getBall();
	public void action(){
		if(ydep<0&&ydir<=0&&rnd()<0.1)ydir=1;
		Enemy.check(es);
		if(rnd()<0.05){
			xdir=rnd()<0.5?-1:1;
			ydir=rndi(-1,1);
		}
		if(rnd()<0.03){
			Enemy e0=null;
			for(Enemy e:es)if(e!=null){
				if(abs(e.w.x-x)<8&&abs(e.w.y-y)<8){
					throwEntFromCenter(getBall(),e.w.x,e.w.y,0.2);
					e0=null;
					break;					
				}
				e0=e;
			}
			if(e0!=null){
				xdir=x<e0.w.x?1:-1;
				ydir=y<e0.w.y?1:-1;
			}
			Collections.shuffle(Arrays.asList(es));
		}
		super.action();
	}
}


