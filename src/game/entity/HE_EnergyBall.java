package game.entity;

import util.BmpRes;
import game.block.GlassBlock;
import graphics.Canvas;

public class HE_EnergyBall extends EnergyBall{
	private static final long serialVersionUID=1844677L;
	private static BmpRes bmp=new BmpRes("Entity/HE_EnergyBall");
	public BmpRes getBmp(){return bmp;}
	public HE_EnergyBall(){
		hp*=5;
	}
	void touchAgent(Agent a){
		if(a==src||hp<=0)return;
		double v=Math.min(hp,5);
		hp-=v;
		a.onAttackedByEnergy(v,src);
	}
	void touchBlock(int px,int py,game.block.Block b){
		if(b.rootBlock().transparency()<0.2)return;
		if(!b.isCoverable()){
			b.des(px,py,5);
			if(b.isSolid())remove();
		}
	}
	public boolean rotByVelDir(){return true;}
}
