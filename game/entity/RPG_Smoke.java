package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import graphics.Canvas;
import game.world.World;
import game.block.SmokeBlock;
import util.BmpRes;

public class RPG_Smoke extends RPG{
	private static final long serialVersionUID=1844677L;
	game.item.RPG_Smoke rpg;
	public BmpRes getBmp(){return rpg.getBmp();}
	public RPG_Smoke(game.item.RPG_Smoke a){
		super();
		rpg=a;
	}
	protected void drop(){
		kill();
		rpg.drop(x,y);
	}
	@Override
	public Entity getBall(){return new FallingBlock(0,0,new SmokeBlock());}
	public void explode(){
		Source ex=SourceTool.explode(this);
		for(int i=0;i<50;++i){
			double xd=rnd(-1,1);
			double yd=rnd(-1,1);
			if(xd*xd+yd*yd>1||rnd()<0.7&&!World.cur.get(x+xd*4,y+yd*4).isCoverable()){--i;continue;}
			int px=f2i(x+xd*4),py=f2i(y+yd*4);
			if(World.cur.get(px,py).isCoverable()){
				World.cur.place(px,py,new SmokeBlock());
			}else getBall().initPos(x+xd*4,y+yd*4,xv/10+xd*0.1,yv/10+yd*0.1,ex).setHpScale(4).add();
		}
		super.explode();
	}
}
