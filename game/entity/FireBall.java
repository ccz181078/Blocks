package game.entity;

import util.BmpRes;
import game.block.LiquidType;
import static java.lang.Math.*;
import static util.MathUtil.*;
import game.world.World;
import game.block.Block;

public class FireBall extends PureEnergyBall{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Entity/FireBall");
	public BmpRes getBmp(){return bmp;}
	public FireBall(){
		hp=10+rnd_gaussion()*4;
	}
	double friction(){return 0.01;}
	public void update(){
		super.update();
		int px=f2i(x),py=f2i(y);
		Block b=World.cur.get(px,py);
		b.touchEnt(px,py,this);
		touchBlock(px,py,b);
	}
	void touchAgent(Agent a){
		if(hp<=0||a.hp<=0)return;
		double v=2;
		while(a.hp>0&&hp>0){
			v=min(v*1.5,hp);
			a.onAttackedByFire(v,this);
			hp-=v;
		}
	}
	@Override
	public double RPG_ExplodeProb(){return radius()*0.3;}
	void touchBlock(int px,int py,game.block.Block b){
		if(b.rootBlock() instanceof LiquidType)remove();
		b.onFireUp(px,py);
		b.onBurn(px,py);
		if(!b.isCoverable()){
			hp-=0.3;
			b.des(px,py,1,this);
			if(b.isSolid()){
				hp-=3;
			}
		}
	}
}
