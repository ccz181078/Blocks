package game.entity;

import util.BmpRes;
import game.block.GlassBlock;
import graphics.Canvas;

public class HE_DarkBall extends DarkBall{
	private static final long serialVersionUID=1844677L;
	private static BmpRes bmp=new BmpRes("Entity/HE_DarkBall");
	public BmpRes getBmp(){return bmp;}
	public HE_DarkBall(){
		hp*=5;
	}
	void touchAgent(Agent a){
		if(a==src||hp<=0)return;
		double v=Math.min(hp,5);
		hp-=v;
		a.onAttackedByDark(v,src);
	}
	public boolean rotByVelDir(){return true;}
}
