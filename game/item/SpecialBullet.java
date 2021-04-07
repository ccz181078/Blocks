package game.item;

import util.BmpRes;
import game.entity.*;
import static util.MathUtil.*;
import static java.lang.Math.*;

public class SpecialBullet extends Bullet{
	int tp;
	public SpecialBullet(int tp){
		this.tp=tp;
	}
	public double _fc(){return tp==3?0:1e-3;}
	@Override
	public boolean cmpType(Item item){
		return super.cmpType(item)&&((SpecialBullet)item).tp==tp;
	}
	public void onKill(double x,double y){
		new Bullet().drop(x,y);
	}
	public BmpRes getBmp(){return Bullet.bmp[tp];}
	public void touchEnt(game.entity.Bullet self,Entity ent){
		if(tp==1){
			new FocusedEnergy().initPos(self.x,self.y,self.xv,self.yv,SourceTool.make(self,"产生的")).add();
		}
		if(tp==2)Spark.explode(self.x,self.y,ent.xv,ent.yv,1,0,3,self,true,0,false);
		if(tp==3)ent.onAttackedByDark(120,self);
		if(tp==4&&ent instanceof Agent){
			for(int i=0;i<4;++i)BloodBall.drop((Agent)ent,3,self);
		}
		if(tp==5)ent.onAttacked(120,self);
		super.touchEnt(self,ent);
		ent.impulseMerge(self);
		self.kill();
	}
	public boolean chkBlock(){return tp!=3;}
	public String getName(){return util.AssetLoader.loadString(getClass(),tp+".name");}//获取名字
	public String getDoc(){return util.AssetLoader.loadString(getClass(),tp+".doc");}//获取说明
}