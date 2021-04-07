package game.entity;

import util.BmpRes;
import game.block.GlassBlock;
import graphics.Canvas;
import static java.lang.Math.*;

public class HE_DarkBall extends DarkBall{
	private static final long serialVersionUID=1844677L;
	private static BmpRes bmp=new BmpRes("Entity/HE_DarkBall");
	public BmpRes getBmp(){return bmp;}
	public HE_DarkBall(){
		hp*=5;
	}
	@Override
	public double radius(){return 0.2;}

	void touchAgent(Agent a){
		if(a.group()==Agent.Group.DARK||hp<=0||a.hp<=0)return;
		double v=(1+v2rel(a));
		double hp0=hp*0.6;
		while(a.hp>0&&hp>hp0){
			v=min(v*1.5,hp);
			a.onAttackedByDark(v*0.5,this);
			hp-=v;
		}
	}
	@Override
	void upd_a(){
		aa+=-av*0.3;
	}
	@Override
	public boolean rotByVelDir(){return true;}
}
