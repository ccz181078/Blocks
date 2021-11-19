package game.item;

import util.BmpRes;
import game.entity.*;
import static util.MathUtil.*;
import static java.lang.Math.*;

public class Bullet extends LaunchableItem implements AttackFilter{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp[]=BmpRes.load("Item/Bullet_",6);
	public BmpRes getBmp(){return bmp[0];}
	@Override
	protected Entity toEnt(){return new game.entity.Bullet(this);}
	public void onKill(double x,double y){
		drop(x,y);
	}
	public void onKill(double x,double y,game.entity.Bullet ent){
		onKill(x,y);
	}
	public double attackValue(){return 40;}
	public double launchValue(){return attackValue();}
	public double mass(){return 0.1;}
	public double RPG_ExplodeProb(){return 0.4;}
	public double hardness(){return game.entity.NormalAttacker.IRON;}
	public void touchEnt(game.entity.Bullet self,Entity ent){
		double v2=self.v2rel(ent);
		ent.onAttacked(max(0,v2-0.01)*4*attackValue(),self);
		self.exchangeVel(ent,0.1);
	}
	public boolean chkBlock(){return true;}
	public double _fc(){return 1e-3;}
	@Override
	public Attack transform(Attack a){
		if(a instanceof FireAttack)a.val*=0.05;
		return a;
	}
};
abstract class WeakBullet extends Bullet{
	public void onKill(double x,double y,game.entity.Bullet ent){
		new Fragment.Config().setEnt(ent).setGrid(2,2,3).apply();
	}
	public void touchEnt(game.entity.Bullet self,Entity ent){
		double v2=self.v2rel(ent);
		ent.onAttacked(max(0,v2-0.01)*4*attackValue(),self);
		if(rnd()<0.1+v2*3)self.hp-=40;
	}
}
class WoodenBullet extends WeakBullet{
	static BmpRes bmp=new BmpRes("Item/WoodenBullet");
	public BmpRes getBmp(){return bmp;}
	public double mass(){return 0.05;}
	public double _fc(){return 1e-2;}
	public double attackValue(){return 15;}
	public double hardness(){return game.entity.NormalAttacker.WOODEN;}
	public Attack transform(Attack a){
		if(a instanceof FireAttack)a.val*=10;
		return a;
	}
	public void onKill(double x,double y,game.entity.Bullet ent){
		super.onKill(x,y,ent);
		if(rnd()<0.8)new WoodenBullet().drop(x,y);
	}
}
class StoneBullet extends WeakBullet{
	static BmpRes bmp=new BmpRes("Item/StoneBullet");
	public BmpRes getBmp(){return bmp;}
	public double _fc(){return 2e-3;}
	public double attackValue(){return 25;}
	public double hardness(){return game.entity.NormalAttacker.STONE;}
}
class QuartzBullet extends WeakBullet{
	static BmpRes bmp=new BmpRes("Item/QuartzBullet");
	public BmpRes getBmp(){return bmp;}
	public double _fc(){return 2e-3;}
	public double attackValue(){return 40;}
	public double hardness(){return game.entity.NormalAttacker.QUARTZ;}
}
class GoldBullet extends WeakBullet{
	static BmpRes bmp=new BmpRes("Item/GoldBullet");
	public BmpRes getBmp(){return bmp;}
	public double attackValue(){return 40;}
	public double mass(){return 0.2;}
	public double hardness(){return game.entity.NormalAttacker.GOLD;}
	public void onKill(double x,double y,game.entity.Bullet ent){
		super.onKill(x,y,ent);
		new GoldBullet().drop(x,y);
	}
}
class DiamondBullet extends WeakBullet{
	static BmpRes bmp=new BmpRes("Item/DiamondBullet");
	public BmpRes getBmp(){return bmp;}
	public double attackValue(){return 80;}
	public double hardness(){return game.entity.NormalAttacker.DIAMOND;}
	public void onKill(double x,double y,game.entity.Bullet ent){
		super.onKill(x,y,ent);
		new Bullet().drop(x,y);
	}
}