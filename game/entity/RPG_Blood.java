package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import graphics.Canvas;
import game.world.World;
import game.block.Block;
import util.BmpRes;

public class RPG_Blood extends RPG{
	private static final long serialVersionUID=1844677L;
	game.item.RPG_Blood rpg;
	public BmpRes getBmp(){return rpg.getBmp();}
	public RPG_Blood(game.item.RPG_Blood a){
		super();
		rpg=a;
	}
	protected void drop(){
		kill();
		rpg.drop(x,y);
	}
	void touchAgent(Agent ent){
		if(ent.hp<1){
			super.touchAgent(ent);
			return;
		}
		for(int i=0;i<27;++i){
			Entity e=BloodBall.drop(ent,min(8.,ent.hp*0.05),this);
			if(e!=null)e.hp+=1;
		}
		super.explode();
		kill();
	}
	@Override
	public Entity getBall(){return new BloodBall(1);}
	public void explode(){
		explode(38);
		super.explode();
	}
}
