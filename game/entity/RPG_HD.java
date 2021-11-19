package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import graphics.Canvas;
import game.world.World;
import game.block.Block;
import util.BmpRes;

public class RPG_HD extends RPG_Guided{
	private static final long serialVersionUID=1844677L;
	public BmpRes getBmp(){return rpg.getBmp();}
	public double mass(){return 8;}
	public RPG_HD(game.item.RPG_HD a){
		super(a);
	}
	@Override
	public void touchEnt(Entity ent){
		if(!removed&&(ent instanceof Agent)){
			ent.impulse(this,1);
			try_explode();
			remove();
		}
	}
	public void explode(){
		Source src=SourceTool.explode(this);
		for(int i=0;i<3;++i){
			double a=i*(PI*2./3.)+atan2(yv,xv);
			double c=cos(a),s=sin(a);
			Entity e=new Bullet(new game.item.Bullet_HD());
			e.initPos(x+c*0.5,y+s*0.5,c*2,s*2,src);
			e.add();
		}
		super.explode();
	}
}
