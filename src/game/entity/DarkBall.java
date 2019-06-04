package game.entity;

import util.BmpRes;

public class DarkBall extends Entity{
	private static final long serialVersionUID=1844677L;
	public BmpRes getBmp(){return bmp;}
	public double width(){return 0.2;}
	public double height(){return 0.2;}	
	static BmpRes bmp=new BmpRes("Entity/DarkBall");
	
	public DarkBall(){
		hp=10;
	}
	public boolean chkBlock(){return false;}
	public boolean chkRigidBody(){return false;}
	public void update(){
		super.update();
		hp-=0.03;
	}
	void touchAgent(Agent a){
		if(a==src)return;
		a.onAttackedByDark(7,src);
		kill();
	}
	double gA(){return 0;}
}
