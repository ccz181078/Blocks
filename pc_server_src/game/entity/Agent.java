package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import game.world.World;
import game.item.Item;
import game.block.BlockAt;
import graphics.Canvas;
import game.world.Weather;
import game.block.AirBlock;

public abstract class Agent extends Entity{
	private static final long serialVersionUID=1844677L;
	public transient int xdir,ydir;
	public int dir;//direction, -1 or 1
	public boolean des_flag;
	public int des_x,des_y;
	public double xp;
	public transient int is_attacked;
	public long spawn_time,last_seen_time;
	public double maxHp(){return 10;}
	public double maxXp(){return 10;}
	public Agent(double _x,double _y){
		x=_x;y=_y;
		hp=maxHp();
		xp=maxXp();
		dir=rnd()<0.5?-1:1;
	}

	public void onAttacked(Attack a){
		super.onAttacked(a);
		is_attacked=8;
	}
	
	public void setDes(int x,int y){//设置破坏
		des_x=x;
		des_y=y;
		des_flag=true;
		if(abs(des_x+0.5-x)>4||abs(des_y+0.5-y)>4)des_flag=false;
	}
	public void cancelDes(){
		des_flag=false;
	}
	public void add(){
		World.cur.newAgent(this);
	}
	public boolean cadd(){
		if(!World.cur.noBlock(x,y,width(),height()))return false;
		//debug.Log.i("add:"+World.cur.RX(x)+","+World.cur.RY(y)+":"+getClass().getSimpleName());
		spawn_time=last_seen_time=World.cur.time;
		add();
		return true;
	}
	public void throwEnt(Entity w,double slope,double v){
		w.x=x+dir*(width()+0.05+w.width())+xv;
		w.y=y+0.2+yv;
		w.xv=xv+dir*v;
		w.yv=yv+dir*slope*v;
		w.src=this;
		w.add();
	}
	public void throwEntFromCenter(Entity w,double tx,double ty,double v){
		w.src=this;
		w.x=x;w.y=y;
		tx-=x;ty-=y;
		v/=sqrt(tx*tx+ty*ty+0.01);
		w.xv=tx*v;
		w.yv=ty*v;
		w.add();
	}
	public void action(){//应用动作
		double lr=max(xdep!=0||ydep!=0?1:0,inblock);
		if(abs(xv)<0.05)lr=max(lr,0.01);
		if(climbable){
			anti_g=1;
			lr=max(lr,0.14);
		}
		if(xdir<0){
			if(lr>0){
				xa-=0.03*lr;
				if(!in_wall)dir=-1;
			}
		}
		if(xdir>0){
			if(lr>0){
				xa+=0.03*lr;
				if(!in_wall)dir=1;
			}
		}
		if(ydir!=0&&climbable){
			ya+=ydir*0.02;
		}else{
			if((ydir>0||xdir<0&&xdep<0||xdir>0&&xdep>0)&&ydep<0)ya+=0.2;
			else if(ydir>0&&inblock>0.05)ya+=0.05*lr;
			if(ydir<0)ya-=0.05f*lr;
		}
		if(des_flag){
			if(destroyable(des_x,des_y)){
				desBlock();
			}
		}
		double xp_a=0.005;
		if(World.cur.weather==Weather._energystone)xp_a=0.03;
		xp=min(maxXp(),xp+xp_a);
	}
	public void update(){
		super.update();
		is_attacked=max(0,is_attacked-1);
		if(!removed&&chkRemove(World.cur.time-last_seen_time))remove();
	}
	public boolean chkRemove(long t){//t帧未被绘制过，检查是否移除
		return t>=30*60;
	}
	public void desBlock(){//破坏方块
		World.cur.get(des_x,des_y).onPress(des_x,des_y,new AirBlock());
	}
	public boolean destroyable(int dx,int dy){
		double xd=abs(dx+0.5-x),yd=abs(dy+0.5-y);
		return xd<=4&&yd<=4&&(xd<0.5+width()&&yd<0.5+height()||World.cur.destroyable(dx,dy));
	}
	public final void before_draw(Canvas cv){
		last_seen_time=World.cur.time;
		if(is_attacked>0)cv.red();
	}
	public final void after_draw(Canvas cv){
		if(is_attacked>0)cv.red();
	}
	public void draw(Canvas cv){//绘制
		float w=(float)width(),h=(float)height();
		game.ui.UI.drawProgressBar(cv,0xffff0000,0xff7f0000,(float)(hp/maxHp()),-w,h+0.1f,w,h+0.2f);
		super.draw(cv);
	}
}

