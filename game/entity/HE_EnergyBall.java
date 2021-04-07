package game.entity;

import util.BmpRes;
import game.block.GlassBlock;
import graphics.Canvas;
import static util.MathUtil.*;
import static java.lang.Math.*;

public class HE_EnergyBall extends EnergyBall{
	private static final long serialVersionUID=1844677L;
	private static BmpRes bmp=new BmpRes("Entity/HE_EnergyBall");
	public BmpRes getBmp(){return bmp;}
	@Override
	public double radius(){return 0.2;}
	@Override
	public double light(){return 0.6;}
	public HE_EnergyBall(){
		hp*=5;
	}
	void touchAgent(Agent a){
		if(a.group()==Agent.Group.ENERGY||hp<=0||a.hp<=0)return;
		double v=(1+v2rel(a));
		double hp0=hp*0.6;
		while(a.hp>0&&hp>hp0){
			v=min(v*1.5,hp);
			a.onAttackedByEnergy(v,this);
			hp-=v;
		}
	}
	void touchBlock(int px,int py,game.block.Block b){
		if(rnd()>b.rootBlock().transparency())return;
		if(!b.isCoverable()){
			b.des(px,py,5,this);
			if(b.isSolid())
				hp -= 6;
		}
	}
	@Override
	public boolean rotByVelDir(){return true;}
	@Override
	void upd_a(){
		aa+=-av*0.3;
	}
	@Override
	public double RPG_ExplodeProb(){return 1;}
}
