package game.entity;
import util.BmpRes;
import static java.lang.Math.*;
import static util.MathUtil.*;
import graphics.Canvas;
import game.item.Item;
public class UsingItem extends NonInteractiveEnt{
	Item item;
	public Entity ent;
	double xd,yd;

	@Override
	public double width(){return xd;}

	@Override
	public double height(){return yd;}
	
	public static void gen(double xd,double yd,double hp,Item item,Entity ent){
		UsingItem e=new UsingItem();
		e.initPos(ent.x,ent.y,ent.xv,ent.yv,ent);
		e.xd=xd;
		e.yd=yd;
		e.hp=hp;
		e.item=item;
		e.ent=ent;
		e.add();
	}
	public boolean chkBlock(){return false;}
	public boolean chkRigidBody(){return false;}

	@Override
	public double gA(){
		return 0;
	}

	@Override
	public void update(){
		super.update();
		hp-=1;
		new SetRelPos(this,ent,0,0);
		item.using(this);
		if(!ent.active())kill();
	}
	void onKill(){
		Fragment.gen(ent.x,ent.y,width(),height(),2,2,3,item.getBmp());
	}

	@Override
	public void draw(Canvas cv){
		item.getBmp().draw(cv,0,0,(float)width(),(float)height());
	}
}
