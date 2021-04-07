package game.entity;
import util.BmpRes;
import static util.MathUtil.*;
import game.block.StoneBlock;
import game.item.DarkBall;

public class DarkWorm extends SimpleAgent{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Entity/DarkWorm");
	public BmpRes getBmp(){return bmp;}
	public double width(){return 0.3;}
	public double height(){return 0.3;}
	public double maxHp(){return 8;}
	public double gA(){return 0.015;}

	@Override
	public double mass(){return 0.4;}
	
	Group group(){return Group.DARK;}
	
	public DarkWorm(double _x,double _y){super(_x,_y);}

	

	void touchAgent(Agent ent){
		if(ent.group()!=group()){
			ent.onAttackedByDark(0.2,this);
			target=ent;
		}
		super.touchAgent(ent);
	}

	void onKill(){
		if(rnd()<0.3)new DarkBall().drop(x,y);
		super.onKill();
	}
}
