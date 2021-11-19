package game.entity;

import util.BmpRes;
import static java.lang.Math.*;
import static util.MathUtil.*;
import game.item.*;
import game.block.Block;

public class SmallSpringBall extends IronBall{
	int tp;
	public SmallSpringBall(int tp){
		super();
		this.tp=tp;
		hp=300;
	}
	
	public String getName(){return util.AssetLoader.loadString(getClass(),tp+".name");}//获取名字
	public double touchVal(){return 0;}
	
	boolean exploded=false;
	public Entity getBall(){
		if(tp==1)return new game.entity.EnergyBall();
		if(tp==2)return new game.entity.FireBall();
		if(tp==3)return new game.entity.DarkBall();
		return null;
	}
	private void try_explode(){
		if(exploded||tp==0)return;
		exploded=true;
		explode(50,this,false);
		kill();
	}
	@Override
	void touchEnt(Entity ent){
		if(ent instanceof SmallSpringBall)return;
		double k=intersection(ent)*(0.1+v2rel(ent)*40)*10;
		ent.onAttacked(k,this);
		if(ent instanceof Agent){
			try_explode();
		}
		hp-=k;
	}
	@Override
	public void update(){
		super.update();
		hp-=0.2/(hypot(xv,yv)+0.01);
	}
	public Attack transform(Attack a){
		if(a!=null)a.val*=2;
		return a;
	}
	
	public double mass(){return 0.2;}
	public double width(){return 0.2;}
	public double height(){return 0.2;}
	public double gA(){return 0;}
	public double _fc(){return 0;}
	protected double K(){return 0.999;}
	void onKill(){
		try_explode();
		new game.item.SmallSpringBall(0).drop(x,y);
	}
	public BmpRes getBmp(){return game.item.SmallSpringBall.bmp[tp];}
}
