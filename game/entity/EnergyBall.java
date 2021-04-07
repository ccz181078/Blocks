package game.entity;

import util.BmpRes;
import static util.MathUtil.*;
import static java.lang.Math.*;
import game.world.World;
import game.block.Block;

public class EnergyBall extends PureEnergyBall{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Entity/EnergyBall");
	public BmpRes getBmp(){return bmp;}
	public void update(){
		super.update();
		int px=f2i(x),py=f2i(y);
		Block b=World.cur.get(px,py);
		b.touchEnt(px,py,this);
		touchBlock(px,py,b);
	}
	public EnergyBall(){
		hp=rnd_exp(5)+rnd_exp(5);
	}
	void touchAgent(Agent a){
		if(hp<=0||a.hp<=0)return;
		double v=2;
		while(a.hp>0&&hp>0){
			v=min(v*1.5,hp);
			a.onAttackedByEnergy(v*0.8,this);
			hp-=v;
		}
	}
	@Override
	public double RPG_ExplodeProb(){
		double r=radius();
		return r*r*0.3;
	}
	void touchBlock(int px,int py,game.block.Block b){
		if(rnd()>b.rootBlock().transparency())return;
		if(!b.isCoverable()){
			hp-=0.5;
			b.des(px,py,1,this);
			if(b.isSolid()){
				hp-=3;
			}
		}
	}
}