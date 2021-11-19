package game.block;

import static java.lang.Math.*;
import static util.MathUtil.*;
import util.BmpRes;
import game.world.World;
import game.entity.*;

public class ExplosiveBlock extends StoneType{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/ExplosiveBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 100;}
	public double getFallHp(){return 10;}
	
	public boolean circuitCanBePlaced(){return false;}
	@Override
	public void onFireUp(int x,int y){
		if(World.cur.get(x,y)==this){
			World.cur.setAir(x,y);
			onDestroy(x+0.5,y+0.5);
		}
	}
	@Override
	public void onDestroy(double x,double y){
		Source ex=SourceTool.block((int)x,(int)y,this);
		Spark.explode(x+.5,y+.5,0,0,50,0.1,1,ex);
		ShockWave.explode(x+.5,y+.5,0,0,72,0.3,3,ex);//1944
	}
	
	@Override
	protected int crackType(){return 0;}
	/*@Override
	public void autoUse(Human h,Agent a){
		if(h.armor.get() instanceof game.item.Airship){
			double x1=h.x,y1=h.y-h.height()-0.5,xv1=h.xv,yv1=h.yv-0.5;
			double x2=a.x,y2=a.y,xv2=a.xv,yv2=a.yv;
			double W=a.width()+0.5,H=a.height()+0.5,H0=a.height();
			for(int i=0;i<100;++i){
				x2+=xv2;y2+=yv2;
				if(a.ydep==0)yv2-=0.03;
				if(World.cur.get(x2,y2-H0).isSolid())xv2=yv2=0;
				double v1=max(1,hypot(xv1,yv1));
				x1+=xv1/v1;y1+=yv1/v1;
				yv1-=0.03;
				
				if(y1<y2-H)return;
				
				if(abs(x1-x2)<W&&abs(y1-y2)<H){
					h.armor.get().onArmorLongPress(h,h.x,h.y-2.2);
					return;
				}
			}
		}
	}*/

};
