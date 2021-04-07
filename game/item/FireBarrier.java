package game.item;

import util.BmpRes;
import game.block.WireBlock;
import game.world.World;
import game.block.Block;
import static util.MathUtil.*;
import static java.lang.Math.*;
import game.entity.*;

public class FireBarrier extends Warhead{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/FireBarrier");
	public BmpRes getBmp(){return bmp;}
	@Override
	public int maxAmount(){return 4;}
	@Override
	public void onLaunchAtPos(game.entity.Agent a,int dir,double x,double y,double slope,double mv2){
		explode(x,y,dir,slope*dir,a.xv,a.yv,true,a);
	}
	public static void explode(double x,double y,double xv,double yv,double xv0,double yv0,boolean flag,Source src){
		double v=sqrt(xv*xv+yv*yv+1e-8);
		xv/=v;yv/=v;
		for(int i=-20;i<=20;i+=1){
			double c2=i/20.*0.1;
			double c1=sqrt(0.04-c2*c2);
			Entity f;
			if(i%2==0){
				f=new HE_FireBall().setHpScale(2);
			}else{
				f=new ShockWave().setHpScale(2);
			}
			double xv2=(xv*c1+yv*c2)*0.5;
			double yv2=(yv*c1-xv*c2)*0.5;
			double xv1=xv0+xv2;
			double yv1=yv0+yv2;
			if(!flag)f.initPos(x+rnd_gaussion()*0.2,y+rnd_gaussion()*0.2,xv1,yv1,src);
			else f.initPos(x+xv+xv0,y+yv+yv0,xv1,yv1,src);
			f.add();
		}
	}
	
	public int getBallCnt(){return 40;}
	public Entity getBall(){
		if(rnd()<0.5)return new HE_FireBall().setHpScale(2);
		return new ShockWave().setHpScale(3);
	}
	protected game.entity.RPG getRPG(){return (game.entity.RPG)new game.item.RPG_FireBarrier().toEnt();}
};
