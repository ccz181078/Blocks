package game.entity;

import util.BmpRes;
import static util.MathUtil.*;

public class DarkMonster extends NormalAgent{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Entity/DarkMonster");
	public BmpRes getBmp(){return bmp;}
	public double maxHp(){return 30;}
	public DarkMonster(double _x,double _y){super(_x,_y);}
	double friction(){return 0;}
	protected Entity getBall(){return new DarkBall();}
	private boolean tran_flag=false;
	public void onAttacked(Attack a){
		super.onAttacked(a);
		if(rnd()<0.5)tran_flag=true;
	}
	public AttackFilter getAttackFilter(){return dark_filter;}
	
	public void action(){
		super.action();
		if(tran_flag&&rnd()<0.2){
			double x1=x+rnd(-4,4),y1=y+rnd(-4,4);
			if(game.world.World.cur.noBlock(x1,y1,width(),height())){
				x=x1;y=y1;
			}
			if(rnd()<0.3)tran_flag=false;
		}
	}
	
	void onKill(){
		new game.item.DarkBall().drop(x,y,rndi(1,2));
	}
}


