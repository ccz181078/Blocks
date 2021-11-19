package game.entity;

import util.BmpRes;
import game.item.Warhead;
import static java.lang.Math.*;
import static util.MathUtil.*;

public class HandGrenadeSlow extends HandGrenade{
	Warhead cur;
	int cur_cnt;
	public HandGrenadeSlow(game.item.HandGrenade it){
		super(it);
		time=600;
		if(!Entity.is_test){
			cur=it.warhead.popItem();
			if(cur!=null)cur_cnt=cur.getBallCnt();
		}
	}
	public Entity getBall(){
		if(!Entity.is_test)--cur_cnt;
		if(cur==null)return null;
		return cur.getBall();
	}
	public void update(){
		super.update();
		if(time<540&&rnd()*time<cur_cnt){
			slowRelease();
		}
	}
	void onKill(){
		while(cur_cnt>0)slowRelease();
		super.onKill();
	}
	private void slowRelease(){
		if(cur_cnt>0){
			Source ex=SourceTool.explode(this);
			double xv1=rnd_gaussion()*0.2,yv1=abs(rnd_gaussion())*0.2;
			Entity e=getBall();
			e.initPos(x,y+height()+e.height()+0.05,xv+xv1,yv+yv1,ex).add();
			impulse(xv1,yv1,-e.mass());
		}
	}
}