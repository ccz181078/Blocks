package game.entity;

import util.BmpRes;
import graphics.Canvas;
import game.block.LiquidType;
import static java.lang.Math.*;

public class HE_FireBall extends FireBall{
	private static final long serialVersionUID=1844677L;
	private static BmpRes bmp=new BmpRes("Entity/HE_FireBall");
	public BmpRes getBmp(){return bmp;}
	public double light(){return 0.6;}
	public HE_FireBall(){hp*=5;}
	@Override
	public double radius(){return 0.2;}
	double _fc(){return 1e-6;}
	@Override
	void touchAgent(Agent a){
		if(a.group()==Agent.Group.FIRE||hp<=0||a.hp<=0)return;
		double v=(1+v2rel(a));
		double hp0=hp*0.8;
		while(a.hp>0&&hp>hp0){
			v=min(v*1.5,hp);
			a.onAttackedByFire(v,this);
			hp-=v;
		}
	}
	void touchBlock(int px,int py,game.block.Block b){
		if(b.rootBlock() instanceof LiquidType)hp-=1;
		b.onFireUp(px,py);
		if(!b.isCoverable()){
			b.des(px,py,5,this);
			if(b.isSolid())
				hp -= 6;
		}
	}
	@Override
	public double RPG_ExplodeProb(){return 1;}
	@Override
	void upd_a(){
		aa+=-av*0.3;
	}
	@Override
	public boolean rotByVelDir(){return true;}
}
