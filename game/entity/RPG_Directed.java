package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import graphics.Canvas;
import game.world.World;
import game.block.Block;
import game.item.NonOverlapSpecialItem;
import util.BmpRes;

public class RPG_Directed extends RPG{
	private static final long serialVersionUID=1844677L;
	NonOverlapSpecialItem<game.item.Warhead> warhead=new NonOverlapSpecialItem<>(game.item.Warhead.class,2);
	public BmpRes getBmp(){return game.item.RPG_Directed.bmp;}
	int time=0;
	public double width(){return 0.4;}
	public double height(){return 0.4;}
	public RPG_Directed(game.item.RPG_Directed a){
		super();
		if(!Entity.is_test)warhead.insert(a.warhead);
		hp*=10;
	}
	double bxv=0,byv=0;
	public void update(){
		super.update();
		++time;
		for(int T=0;T<3;++T){
			bxv=rnd_gaussion()*0.4;
			byv=rnd_gaussion()*0.4;
			if(time>=5)
			for(Agent a:World.cur.getNearby(x,y,6,6,false,false,true).agents){
				if(a instanceof Airship_Flank){
					Airship_Flank af=(Airship_Flank)a;
					if(af.airship!=null)a=af.airship;
				}
				if(a==source.getSrc())continue;
				int n=0;
				for(int i=0;i<5;++i){
					double xd=a.x-x,yd=a.y-y;
					double k=0.55/(1e-3+hypot(xv,yv))+rnd_exp(0.55);
					double rxv=a.xv-(xv*k+yv*rnd_gaussion()*0.2+bxv),ryv=a.yv-(yv*k-xv*rnd_gaussion()*0.2+byv);
					double t=hypot(xd,yd)/(hypot(rxv,ryv)+0.1);
					if(hypot(xd+rxv*t,yd+ryv*t)<hypot(a.width(),a.height()))++n;
				}
				if(n>=3)try_explode();
			}
		}
	}
	public void onKill(){
		super.onKill();
		DroppedItem.dropItems(warhead,x,y);
	}
	public void explode(){
		Source ex=SourceTool.explode(this);
		while(!warhead.isEmpty()){
			double k=0.55/(1e-3+hypot(xv,yv))+rnd_exp(0.55);
			warhead.popItem().explode(x,y,(xv*k+yv*rnd_gaussion()*0.2+bxv),(yv*k-xv*rnd_gaussion()*0.2+byv),this,null);
		}
		fuel=0;
		super.explode();
	}
}
