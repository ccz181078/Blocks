package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import game.world.World;
import game.item.Item;
import game.block.Block;
import game.block.BlockAt;
import graphics.Canvas;
import game.world.Weather;
import game.block.AirBlock;
import util.BmpRes;
import game.GameSetting;
import game.GlobalSetting;
import game.item.SingleItem;

public abstract class Agent extends Entity implements game.item.EnergyContainer{
	private static final long serialVersionUID=1844677L;
	public transient double jump_acc,jump_len;
	public transient int xdir,ydir;
	public transient boolean is_ctrled;
	public int dir;//direction, -1 or 1
	public boolean des_flag;
	public int des_x,des_y;
	public double xp;
	//public transient int is_attacked;
	public long spawn_time,last_seen_time;
	public int camp;
	public double maxHp(){return 10;}
	public double maxXp(){return 10;}
	public double hardness(){return game.entity.NormalAttacker.AGENT;}
	public boolean hasBlood(){return true;}
	public Agent(double _x,double _y){
		x=_x;y=_y;
		hp=maxHp();
		xp=maxXp();
		dir=rnd()<0.5?-1:1;
		camp=rndi(1,10000);
	}
	@Override
	void chkBlock(int x,int y,Block b){
		b.touchEnt(x,y,this);
		touchBlock(x,y,b);
	}
	
	public boolean onClick(double x,double y,Agent w){
		return false;
	}
	
	public double RPG_ExplodeProb(){return 0.3;}
	public static Agent temp(double x,double y,final double w,final double h,final int dir,final Source src){
		Agent tmp=new Agent(x,y){
			public void add(){throw new UnsupportedOperationException();}
			public double width(){return w;}
			public double height(){return h;}
			public double mass(){return 1;}
			Group group(){return Group.STONE;}
			public Source getLaunchSrc(){return src;}
		};
		tmp.dir=dir;
		return tmp;
	}
	public int resCap(){return max(0,(int)(maxXp()-xp));}
	public int getEnergy(){return max(0,(int)(xp));}
	public void loseEnergy(int v){xp-=v;}
	public void gainEnergy(int v){xp=min(xp+v,maxXp());}
	
	public boolean isWeakToNormalAttack(){
		return hp<200;
	}
	
	public double xvAt(double x0,double y0){
		return super.xvAt(x0,y0)-xmov();
	}
	public double yvAt(double x0,double y0){
		return super.yvAt(x0,y0)-ymov();
	}
	@Override
	public Agent getSrc(){
		if(!isRemoved())return this;
		if(source!=null)return source.getSrc();
		return this;
	}
	/*public void launch(Entity e,double s,double mv2){
		throwEnt(e,s,mv2);
	}*/
		
	public boolean clickable(double tx,double ty){
		return max(abs(tx-x),abs(ty-y))<4;
	}
	
	enum Group{
		GREEN,
		ENERGY,
		BLOOD,
		FIRE,
		DARK,
		STONE,
		ZOMBIE,
		PLAYER,
	};
	
	abstract Group group();
	
	transient double last_hp=Double.NaN;
	transient long last_hp_show_time=0;
	
	/*@Override
	void touchEnt(Entity ent){//事件：接触实体
		super.touchEnt(ent);
		if(xdir!=0||ydir!=0){
			double m=min(mass(),ent.mass());
			double S=
				(min(right,ent.right)-max(left,ent.left))*
				(min(top,ent.top)-max(bottom,ent.bottom));
			double c=S/V*0.03;
			double x_=xdir*c,y_=ydir*c;
			impulse(x_,y_,m);
			ent.impulse(x_,y_,-m);
		}
	}*/
	double cur_hp;
	int hp_unchange_time=0;
	public void showHpChange(){
		if(!GlobalSetting.getGameSetting().show_hp_change)return;
		if(this instanceof VehiclePlaceHolder)return;
		if(Double.isNaN(last_hp)||last_hp_show_time==0){
			last_hp=hp;
			cur_hp=hp;
			hp_unchange_time=0;
			last_hp_show_time=World.cur.time;
			return;
		}
		if(abs(cur_hp-max(0,hp))>=1){
			hp_unchange_time=0;
			cur_hp=max(0,hp);
		}else{
			++hp_unchange_time;
		}
		double a=max(0,hp)-last_hp;
		if((hp_unchange_time>30||World.cur.time-last_hp_show_time>100||hp<=0)&&abs(a)>=1){
			int delta=rf2i(a);
			if(x!=0){
				Text.gen(x+rnd_gaussion()*width(),y+rnd_gaussion()*height(),(delta>0?"+":"")+delta,this);
				last_hp=hp;
				cur_hp=hp;
				hp_unchange_time=0;
				last_hp_show_time=World.cur.time;
			}
		}
	}

	/*public void onAttacked(Attack a){
		super.onAttacked(a);
		is_attacked=8;
	}*/
	
	public void setDes(double x,double y){
		setDes(f2i(x),f2i(y));
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
		if(is_test){
			last_launched_ent=this;
			return;
		}
		World.cur.newAgent(this);
	}
	public boolean cadd(){
		if(!World.cur.noBlock(x,y,width(),height()))return false;
		//debug.Log.i("add:"+World.cur.RX(x)+","+World.cur.RY(y)+":"+getClass().getSimpleName());
		spawn_time=last_seen_time=World.cur.time;
		add();
		return true;
	}
	public Source getLaunchSrc(){return this;}
	public void throwEnt(Entity w,double slope,double mv2){
		double v=sqrt(mv2/w.mass());
		double r=hypot(1,slope);
		double tx=1/r;
		double ty=slope/r;
		w.thrownFrom(this,x+dir*(width()+0.05+w.width()),y+0.2,dir*tx*v,dir*ty*v);
	}
	public void throwEntAtPos(Entity w,int dir,double x,double y,double slope,double mv2){
		double v=sqrt(mv2/w.mass());
		double r=hypot(1,slope);
		double tx=1/r;
		double ty=slope/r;
		w.thrownFrom(this,x,y,dir*tx*v,dir*ty*v);
	}
	public void throwEntFromCenter(Entity w,double tx,double ty,double mv2){
		double v=sqrt(mv2/w.mass());
		tx-=x;ty-=y;
		double r=hypot(tx,ty)+1e-8;
		tx/=r;ty/=r;
		double d=hypot(width(),height())+hypot(w.width(),w.height())+0.05;
		w.thrownFrom(this,x+tx*d,y+ty*d,tx*v,ty*v);
	}
	public double getJumpAcc(){
		double hp_rate=1;//sqrt(min(1,hp*2/maxHp()));
		return 0.4*rnd(hp_rate,1)*(jump_acc/jump_len);
	}
	public void clickAt(double tx,double ty){}
	public void ai(){}//决策
	public void action(){defaultAction();}//应用动作
	public final void defaultAction(){
		inblock=min(1,inblock);
		double lr=max(xdep!=0||ydep!=0?1:0,inblock)*min(1,(f+xf)*8);
		double hp_rate=1;//sqrt(min(1,hp*2/maxHp()));
		if(abs(xv)<0.05)lr=max(lr,0.01);
		if(climbable){
			anti_g=1;
			lr=max(lr,0.14);
		}
		lr*=hp_rate;
		if(xdir<0){
			if(lr>0){
				xa-=0.03*lr;
				//if(!in_wall)dir=-1;
			}
		}
		if(xdir>0){
			if(lr>0){
				xa+=0.03*lr;
				//if(!in_wall)dir=1;
			}
		}
		if(ydir!=0&&climbable){
			ya+=ydir*0.02*hp_rate;
		}else{
			if((xdir<0&&xdep<0||xdir>0&&xdep>0)&&ydep==0)ya+=ydir*inblock*0.1;
			if((ydir>0||xdir<0&&xdep<0||xdir>0&&xdep>0)&&ydep<0&&!in_wall)ya+=getJumpAcc();
			else if(ydir>0&&inblock>0.05)ya+=0.05*lr;
			if(ydir<0)ya-=0.05f*lr;
		}
		if(in_wall){
			if(lr>0)ya+=ydir*0.05*lr;
			if(lr>0)xa+=xdir*0.02*lr;
		}
		if(des_flag&&rnd()<hp_rate){
			if(destroyable(des_x,des_y)){
				desBlock();
			}
		}
		
		double xp_a=0.005;
		if(World.cur.weather==Weather._energystone)xp_a=0.03;
		xp=min(maxXp(),xp+xp_a);
	}
	@Override
	public void update0(){
		super.update0();
		is_ctrled=false;
	}
	@Override
	public final void move(){
		jump_acc=0;jump_len=1e-8;
		super.move();
	}
	@Override
	public double onImpact(double v){return v;}
	@Override
	public void update(){
		super.update();
		//is_attacked=max(0,is_attacked-1);
		showHpChange();
		
		//if(!removed&&chkRemove(World.cur.time-last_seen_time))remove();
	}
	public boolean chkRemove(long t){//t帧未被绘制过，检查是否移除
		return t>=30*60;
	}
	public void desBlock(){//破坏方块
		SingleItem si=getCarriedItem();
		Item it=null;
		if(si!=null)it=si.get();
		if(it==null)it=new AirBlock();
		World.cur.get(des_x,des_y).onPress(des_x,des_y,it);
	}
	public boolean destroyable(int dx,int dy){
		double xd=abs(dx+0.5-x),yd=abs(dy+0.5-y);
		return xd<=4&&yd<=4&&(xd<0.5+width()&&yd<0.5+height()||World.cur.destroyable(dx,dy));
	}
	public final void before_draw(Canvas cv){
		last_seen_time=World.cur.time;
		//if(is_attacked>0)cv.red();
	}
	public final void after_draw(Canvas cv){
		//if(is_attacked>0)cv.red();
	}
	public void draw(Canvas cv){//绘制
		float w=(float)width(),h=(float)height();
		game.ui.UI.drawProgressBar(cv,0xffff0000,0xff7f0000,(float)(hp/maxHp()),-w,h+0.1f,w,h+0.2f);
		super.draw(cv);
	}
	
	public void initUI(game.ui.UI_MultiPage ui){}
	public SingleItem getCarriedItem(){
		return null;
	}
	public final void throwCarriedItem(boolean all,long time){
		SingleItem s,s0=getCarriedItem();
		if(s0==null)return;
		if(all){
			s=new SingleItem();
			s.insert(s0);
		}else s=s0.pop();
		if(!s.isEmpty()){
			if(!all&&time>30){
				double k=min(0.3,time*0.005);
				throwEnt(new ThrowedItem(x,y,s.get()),rnd_gaussion()*0.1,rnd(k*k,(k+0.1)*(k+0.1))*0.1);
			}else{
				throwEnt(new DroppedItem(x,y,s),rnd(-0.3,0.3),0.1*rnd(0.01,0.02));
			}
		}
	}
	@Override
	void onKill(){
		showHpChange();
		BmpRes bmp=getBmp();
		if(bmp!=null){
			double w=width(),h=height();
			new Fragment.Config().setEnt(this).setGrid(4,4,6).apply();
		}
		super.onKill();
	}

	@Override
	public void onKilled(Source src){
		super.onKilled(src);
		Agent a=src.getSrc();
		if(a!=null)a.onKillAgent(this);
	}
	
	public void onKillAgent(Agent a){}
	
	public final void addHp(double a){
		if(hp>0&&a>0)hp=min(maxHp(),hp+a);
	}
	
	public final double xmov(){return xdir*maxv();}
	public final double ymov(){return ydir*maxv();}
	public double maxv(){return 0.16;}
}

