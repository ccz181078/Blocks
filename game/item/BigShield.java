package game.item;

import static java.lang.Math.*;
import static util.MathUtil.*;
import game.entity.*;

public abstract class BigShield extends Item implements AttackFilter{
	public game.entity.BigShield ent;
	public BigShield(){
		ent=new game.entity.BigShield(this);
	}
	
	public double width(){return 0.5;}
	public double height(){return 1;}
	public double mass(){return 2;}
	public abstract double maxHp();
	
	public void update(){}
	
	public double touchVal(){return 2;}
	public boolean onClickEnt(double x,double y,Agent a){return false;}

	public Item clickAt(double x,double y,Agent a){
		ent.initPos(a.x+a.dir*(a.width()+ent.width()),a.y,a.xv,a.yv,a).add();
		ent.dir=a.dir;
		return null;
	}
	public void drawInfo(graphics.Canvas cv){
		if(ent.hp/ent.maxHp()<1-1e-4)game.ui.UI.drawProgressBar(cv,0xff00ff00,0xff007f00,(float)(ent.hp/maxHp()),-0.4f,-0.4f,0.4f,-0.33f);
	}
	public void touchEnt(Entity e){
		double c=max(ent.v2rel(e)-0.2,0);
		if(c>0)e.onAttacked(c*40,ent);
	}
	public final int maxAmount(){return 1;}
	public abstract double hardness();
	public void onKill(){}
	public void pick(DroppedItem item){}
}
class BigIronShield extends BigShield{
	public Attack transform(Attack a){
		return a;
	}
	public double mass(){return 6;}
	public double maxHp(){return 5000;}
	public double hardness(){return game.entity.NormalAttacker.IRON;}
}
class BigHDShield extends BigShield{
	public Attack transform(Attack a){
		return a;
	}
	public double mass(){return 80;}
	public double maxHp(){return 10000;}
	public double hardness(){return game.entity.NormalAttacker.HD;}
}
class BigIronNailShield extends BigIronShield{
	public void touchEnt(Entity e){
		super.touchEnt(e);
		if(abs(e.x-(ent.x+ent.dir*ent.width()))<=e.width()){
			e.onAttacked(ent.v2rel(e)*150+0.5,ent);
		}
	}
}
class BigWoodenShield extends BigShield{
	public Attack transform(Attack a){
		if(a instanceof FireAttack){
			a.val*=10;
		}
		return a;
	}
	public double mass(){return 3;}
	public double maxHp(){return 400;}
	public double hardness(){return game.entity.NormalAttacker.WOODEN;}
}
class BigCactusShield extends BigWoodenShield{
	public void touchEnt(Entity e){
		super.touchEnt(e);
		e.onAttacked(ent.v2rel(e)*10+0.2,ent);
	}
	public double hardness(){return game.entity.NormalAttacker.CACTUS;}
}
