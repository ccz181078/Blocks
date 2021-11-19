package game.entity;

import util.BmpRes;
import game.block.GlassBlock;
import static util.MathUtil.*;
import static java.lang.Math.*;

public class EnergyExplodeBall extends Entity{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Entity/EnergyBall");
	public BmpRes getBmp(){return bmp;}
	public double hardness(){return game.entity.NormalAttacker.HD;}
	public boolean shouldKeepAwayFrom(){return true;}
	@Override
	public double light(){
		return 2;
	}
	double r=0.8;
	public double width(){return r;}
	public double height(){return r;}
	public double gA(){return 0;}
	public boolean chkAgent(){return true;}
	public boolean chkEnt(){return true;}
	public double mass(){return 16;}
	@Override
	public double touchVal(){return 0;}
	public static void gen(double x,double y,Source src){
		EnergyExplodeBall e=new EnergyExplodeBall();
		e.initPos(x,y,0,0,src);
		e.hp=500;
		e.add();
	}
	void touchAgent(Agent a){
		a.onAttackedByEnergy(10,this);
		a.onAttacked(10,this);
	}
	@Override
	public void update(){
		super.update();
		hp-=10;
		r=max(0.6,min(1,r+rnd_gaussion()*0.02));
	}
	public void onKill(){
		ShockWave.explode(x+.5,y+.5,xv,yv,360,0.2,12,this);//622080
	}
	void touchBlock(int px,int py,game.block.Block b){
		if(!b.isCoverable()){
			b.des(px,py,16,this);
		}
	}
}
