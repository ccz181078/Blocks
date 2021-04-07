package game.entity;

import util.BmpRes;
import static util.MathUtil.*;
import static java.lang.Math.*;
import game.world.World;

public class CactusMonster extends SimpleAgent{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Entity/CactusMonster");
	public BmpRes getBmp(){return bmp;}
	public double width(){return 0.25;}
	public double height(){return 0.25;}
	public double maxHp(){return 6;}
	public double mass(){return 0.5;}
	public double hardness(){return game.entity.NormalAttacker.CACTUS;}
	
	@Override
	public double buoyancyForce(){return 1.5;}
	@Override
	public double fluidResistance(){return 0.2;}
	
	Group group(){return Group.GREEN;}
	@Override
	public void update(){
		super.update();
		if(ydep!=0&&World.cur.get(x,y-1).rootBlock() instanceof game.block.SandBlock)loseHp(0.01,this);
	}
	void touchAgent(Agent ent){
		if(!(ent instanceof CactusMonster)){
			ent.onAttacked(attackVal(),this);
			target=ent;
		}
		super.touchAgent(ent);
	}

	
	public CactusMonster(double _x,double _y){super(_x,_y);}
	void onKill(){
		new game.block.CactusBlock().drop(x,y,1);
		super.onKill();
	}
	protected double attackVal(){return 0.2;}
}
