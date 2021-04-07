package game.entity;
import util.BmpRes;
import static util.MathUtil.*;
import game.block.StoneBlock;

public class StoneMonster extends SimpleAgent{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Entity/StoneMonster");
	public BmpRes getBmp(){return bmp;}
	public double width(){return 0.4;}
	public double height(){return 0.4;}
	public double maxHp(){return 40;}
	Group group(){return Group.STONE;}
	public StoneMonster(double _x,double _y){super(_x,_y);}
	public double hardness(){return game.entity.NormalAttacker.STONE;}

	void touchAgent(Agent ent){
		ent.onAttacked(0.2,this);
		target=ent;
		super.touchAgent(ent);
	}

	void onKill(){
		if(getClass()==StoneMonster.class)new StoneBlock().drop(x,y,1);
		super.onKill();
	}
}
