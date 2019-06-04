package game.entity;

import util.BmpRes;
import game.block.LiquidType;

public class FireBall extends Entity{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Entity/FireBall");
	public BmpRes getBmp(){return bmp;}
	public double width(){return 0.2;}
	public double height(){return 0.2;}
	public boolean chkRigidBody(){return false;}
	double friction(){return 0;}
	public FireBall(){
		hp=10;
	}
	public void update(){
		super.update();
		hp-=0.03;
	}
	void touchAgent(Agent a){
		if(a==src)return;
		a.onAttackedByFire(7,src);
		kill();
	}
	void touchBlock(int px,int py,game.block.Block b){
		if(b.rootBlock() instanceof LiquidType)remove();
		b.onFireUp(px,py);
		if(!b.isCoverable()){
			hp-=0.3;
			b.des(px,py,1);
			if(b.isSolid())remove();
		}
	}
	double gA(){return 0;}
}
