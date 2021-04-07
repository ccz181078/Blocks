package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import graphics.Canvas;
import game.world.World;
import game.block.Block;
import util.BmpRes;

public class RPG_EnergyBall extends RPG_Guided{
	private static final long serialVersionUID=1844677L;
	public BmpRes getBmp(){return rpg.getBmp();}
	public RPG_EnergyBall(game.item.RPG_EnergyBall a){
		super(a);
	}
	protected void drop(){
		kill();
		rpg.drop(x,y);
	}
	public void explode(){
		ShockWave.explode(x,y,0,0,20,0.1,0.2,this);
		double xd=yv,yd=-xv;
		if(target!=null){
			xd=target.x-x;
			yd=target.y-y;
		}
		double d=sqrt(xd*xd+yd*yd+1e-8);
		xd/=d;yd/=d;
		Source ex=SourceTool.explode(this);
		for(int i=0;i<24;++i){
			double c1=(i<8?1:-1)*(0.5+rnd_gaussion()*0.2),c2=rnd_gaussion()*0.16;
			double xv=xd*c1+yd*c2,yv=yd*c1-xd*c2;
			if(i>=16){
				xv=rnd_gaussion()*0.3;
				yv=rnd_gaussion()*0.3;
			}
			Ball ball=new EnergyBall();
			ball.setHpScale(rnd(24,30));//24*27
			ball.initPos(x+xv,y+yv,xv,yv,ex);
			ball.add();
		}
		super.explode();
	}
}
