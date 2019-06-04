package game.entity;

import util.BmpRes;
import graphics.Canvas;
import game.block.LiquidType;

public class HE_FireBall extends FireBall{
	private static final long serialVersionUID=1844677L;
	private static BmpRes bmp=new BmpRes("Entity/HE_FireBall");
	public BmpRes getBmp(){return bmp;}
	public HE_FireBall(){hp*=5;}
	void touchAgent(Agent a){
		if(a==src||hp<=0)return;
		double v=Math.min(Math.min(hp,a.hp),10);
		hp-=v;
		a.onAttackedByFire(v,src);
	}
	void touchBlock(int px,int py,game.block.Block b){
		if(b.rootBlock() instanceof LiquidType)hp-=1;
		b.onFireUp(px,py);
		if(!b.isCoverable()){
			b.des(px,py,5);
			if(b.isSolid())remove();
		}
	}
	public boolean rotByVelDir(){return true;}
}
