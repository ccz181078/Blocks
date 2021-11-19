package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import game.world.*;
import game.block.Block;
import game.block.BlockAt;
import java.io.*;
import graphics.Canvas;
import util.BmpRes;

public abstract class Entity implements Serializable,BallProvider,Source,NormalAttacker{
	private static final long serialVersionUID=1844677L;
	public double x,y,a,xv,yv,av,hp;//postion,velocity,health
	public transient double __x,__y;
	protected transient double Ek;
	public boolean removed=false,locked=false,teleporting=false;
	public long id;
	public Source source=null;
	public transient double xdep,ydep,xa,ya,aa,xv0,yv0;//acceleration
	public transient double xf,yf,f,left,top,right,bottom,V,ax,ay;
	public transient double xfl,yfl,xfs,yfs,fs,fs2,fc;
	public transient double inblock,anti_g,last_g;
	public transient boolean in_wall,climbable,shadowed;
	public transient java.util.ArrayList<Pose> history_pose;
	public boolean isPureEnergy(){return false;}
	public static class FriendGroup implements Serializable{
		
	}
	public FriendGroup friend_group;
	//public double hardness(){return game.entity.NormalAttacker.AGENT;}
	public static class Pose implements Cloneable{
		public double x,y;
		Pose(double x,double y){
			this.x=x;
			this.y=y;
		}
		public Pose clone(){
			try{
				return (Pose)super.clone();
			}catch(Exception e){}
			return null;
		}
	}
	public BmpRes getBmp(){return null;}
	public boolean pickable(){return false;}
	public void pickedBy(Agent a){}
	public double fluidResistance(){return 1/max(1,mass());}
	public static boolean is_test=false,is_calc_impulse=false;
	static Entity last_launched_ent=null;
	static double xmv,ymv;
	public boolean active(){return !isRemoved()&&!locked&&!teleporting;}
	public static void beginCalcImpulse(Entity ent){//bug?
		xmv=ymv=0;
		is_calc_impulse=true;
	}
	public static void endCalcImpulse(Entity ent,double k){
		is_calc_impulse=false;
		if(ent!=null)ent.impulse(xmv,ymv,k);
	}
	public static void beginTest(){
		is_test=true;
		last_launched_ent=null;
	}
	public static Entity endTest(){
		is_test=false;
		return last_launched_ent;
	}
	@Override
	public Agent getSrc(){
		if(source==null)return null;
		return source.getSrc();
	}
	@Override
	public String toString(){
		String s=getName();
		if(source!=null)s=source+s;
		return s;
	}
	public String getName(){
		return util.AssetLoader.loadString(getClass(),"name");
	}

	public double light(){return 0;}
	
	public double buoyancyForce(){return 0.3;}
	
	public boolean isRemoved(){return removed;}
	public boolean harmless(){return false;}
	public boolean shouldKeepAwayFrom(){return false;}
	
	private final void apply_v(){
		xv+=xa;yv+=ya;av+=aa;
		xa=ya=aa=0;
		
		double dxv=xv-xv0,dyv=yv-yv0;
		//if(this instanceof Player)System.err.println("dxv="+dxv+"  dyv="+dyv+"  xv="+xv+"  yv="+yv+"  time="+World.cur.time);
		double delta_Ek=max(0,mass()*(dxv*dxv+dyv*dyv)-0.3);
		if(delta_Ek>0){
			delta_Ek=onImpact(delta_Ek*30);
			//if(this instanceof Player)System.err.printf("d Ek=%g\n",delta_Ek);
			loseHp(delta_Ek,SourceTool.IMPACT);
		}
		xv0=xv;yv0=yv;
	}
	
	public void move(){
		//apply_v();
		upd_v();
		updBlock();
		if(x!=x||y!=y||a!=a||xv!=xv||yv!=yv||av!=av||!(abs(x)<100000)||!(abs(y)<100000)){
			System.out.printf("error: %s:  %g,%g,%g; %g,%g,%g; %g,%g,%g; %d; %g,%g,%g\n",getClass().toString(),x,y,a,xv,yv,av,xa,ya,aa,(int)id,xf,yf,f);
			if(__x==__x&&__y==__y){
				x=__x+rnd_gaussion()*0.01;y=__y+rnd_gaussion()*0.01;a=0;
			}else{
				x=y=10;a=0;
			}
			xv=yv=av=0;
		}
		/*if(history_pose==null)history_pose=new java.util.ArrayList<>();
		history_pose.add(0,new Pose(x,y));
		while(history_pose.size()>NearbyInfo.BW+1)history_pose.remove(history_pose.size()-1);*/
	}
	
	//移除实体
	final void remove(){
		hp=0;
		removed=true;
	}
	
	//移除实体，并调用onKill
	public final void kill(){
		if(!removed){
			onKill();
			remove();
		}
	}
	public void kill(Source src){
		if(!removed){
			loseHp(hp,src);
			kill();
		}
	}
	public Entity getBall(){return new FireBall();}
	public void explode(double v){explode(v,this,false);}
	public void explode(double v,int k){explode(v,this,false,k);}
	public void explode(double v,BallProvider ball,boolean directed){
		explode(v,ball,directed,5);
	}
	public void explode(double v,BallProvider ball,boolean directed,int k){
		//v*1.55
		Source ex=SourceTool.explode(this);
		int c=min(300,rf2i(v*2));
		Spark.gen(x,y);
		double v0=getExplodeVel();
		for(int i=0;i<c;++i){
			double xv=rnd(-1,1),yv=rnd(-1,1),v2=xv*xv+yv*yv;
			if(directed&&xv*this.xv+yv*this.yv<v*hypot(this.xv,this.yv)*rnd(0,1)-1e-4)continue;
			if(v2>0.1&&v2<1){
				ball.getBall().initPos(x+xv/3,y+yv/3,this.xv/k+xv*v0,this.yv/k+yv*v0,ex).add();
				if(is_test)return;
			}
		}
	}
	double getExplodeVel(){return 0.4;}
	
	public void explodeDirected(BallProvider ball,int cnt,double angle_range,double max_v,double min_v,double hp_scale){
		Source ex=SourceTool.explode(this);
		for(int i=1;i<=cnt;++i){
			if(is_test)i=cnt;
			double Xv=1,Yv=1,t=max_v*i*i/(cnt*cnt);
			double angle=rnd_gaussion()*angle_range;
			double v=sqrt(xv*xv+yv*yv+1e-8);
			Xv *= xv / v;
			Yv *= yv / v;
			Xv *= t * ( 1 + rnd_gaussion()*0.05 );
			Yv *= t * ( 1 + rnd_gaussion()*0.05 );
			v=sqrt(Xv*Xv+Yv*Yv);
			if(v>min_v){
				double c=cos(angle),s=sin(angle);
				ball.getBall().initPos(x,y,Xv*c-Yv*s,Xv*s+Yv*c,ex).add();
			}
		}
	}
	
	public void desBlock(){
		Entity w=this;
		double Ek=mass()*(xv*xv+yv*yv);
		if(Ek>0.05){
			Ek=max(0,Ek-0.05);
			for(int i=0;i<1;++i){
				int x=f2i(w.x+rnd(-1,1)*w.width());
				int y=f2i(w.y+rnd(-1,1)*w.height());
				Block b=World.cur.get(x,y);
				if(!b.isSolid()&&b.fallable()){
					b.fall(x,y,w.xv,w.yv);
					w.impulse(w.xv,w.yv,-1);
				}
			}
		}
	}
	
	//前更新
	//每次世界更新时，在所有实体更新前执行
	public void update0(){
		__x=x;__y=y;
		//if(xa==xa&&ya==ya){xv0=xv+xa;yv0=yv+ya;}else{xv0=xv;yv0=yv;}
		//xv0=xv;yv0=yv;
		xa=ya=aa=0;
		if(!rotByVelDir())a=av=0;
		ax=cos(a);ay=sin(a);
		inblock=0;
		climbable=false;
		shadowed=true;
		xf=yf=f=fc=anti_g=0;
		double W=width(),H=height();
		left=x-W;
		right=x+W;
		top=y+H;
		bottom=y-H;
		V=W*H*4;
		
		Ek=(xv*xv+yv*yv);
		atked_sum*=0.99;
	}
	public Pose getPose(double x0,double y0){
		if(true){
			//boolean flag=true;
			//for(int i=1;i<100;++i)
			//	if(World.cur.get(x+(x0-x)*i/100,y+(y0-y)*i/100).transparency()>0.99)return null;
			return new Pose(x,y);
		}
		if(history_pose==null)return null;
		for(int i=1;i<history_pose.size();++i){
			Pose p0=history_pose.get(i-1);
			Pose p1=history_pose.get(i);
			double d0=hypot(p0.x-x0,p0.y-y0)-(i-1);
			double d1=hypot(p1.x-x0,p1.y-y0)-i;
			if(abs(d0)<1e-3||abs(d1)<1e-3||(d0<0)!=(d1<0)){
				double t=Math.max(0,Math.min(1,d1/(d1-d0)));
				if(!(0<=t&&t<=1))t=0.5;
				Pose tmp=p1.clone();
				tmp.x+=(p0.x-p1.x)*t;
				tmp.y+=(p0.y-p1.y)*t;
				return tmp;
			}
		}
		return null;
	}
	public boolean chkBlock(){return true;}//是否与方块接触
	public boolean chkRigidBody(){return true;}//是否与方块进行刚体碰撞检测
	public boolean chkAgent(){return true;}//是否与Agent接触
	public boolean chkEnt(){return true;}//是否与Entity接触
	
	
	//更新
	public void update(){
		boolean chk_block=chkBlock();
		boolean chk_agent=chkAgent();
		boolean chk_ent=chkEnt();
		if(!chk_block)shadowed=false;
		if(!chk_block&&!chk_agent&&!chk_ent)return;
		game.world.NearbyInfo ni=World.cur.getNearby(x,y,width(),height(),false,chk_ent,chk_agent);
		if(chk_ent)
		for(Entity a:ni.ents)if(a!=this)if(hitTest(a)){
			touchEnt(a,a.chkEnt());
			if(isRemoved())return;
		}
		if(chk_agent)
		for(Agent a:ni.agents)if(a!=this)if(hitTest(a)){
			touchAgent(a);
			if(isRemoved())return;
		}
		if(useRandomWalk()){
			impulse(rnd_gaussion(),rnd_gaussion(),(World.cur.setting.random_walk_coefficient)*sqrt(hypot(xv,yv))*(width()+height()));
		}
		upd_a();
		
		fc+=_fc()/mass();
	}
	
	void fallBlock(){
		if(!chkRigidBody())return;
		if(mass()<1)return;
		Entity w=this;
		double Ek=mass()*(xv*xv+yv*yv);
		int x=f2i(w.x+rnd(-1,1)*w.width());
		int y=f2i(w.y+rnd(-1,1)*w.height());
		Block b=World.cur.get(x,y);
		if(b.fallable()&&Ek>b.fallValue()){
			b.fall(x,y,w.xv,w.yv);
			w.impulse(w.xv,w.yv,-1);
		}
	}
	
	void upd_a(){}
	
	private double mov(double vd_0){
		double vd=1;
		double xt=2,yt=2;
		if(xv!=0)vd=min(vd,xt=(xv-xdep)/xv);
		if(yv!=0)vd=min(vd,yt=(yv-ydep)/yv);
		double vd0=vd;
		vd=min(vd,vd_0);
		double k=(abs(xv)+abs(yv))*vd;
		if(k>1e-4)vd*=(k-1e-4)/k;
		else vd=0;
		if(!in_wall){
			x+=xv*vd;
			y+=yv*vd;
		}
		if(vd_0>=vd){
			if(vd0==xt&&xdep!=0)xv=0;else if(xv!=0)xdep=0;
			if(vd0==yt&&ydep!=0)yv=0;else if(yv!=0)ydep=0;
		}
		return vd;
	}
	
	void chkBlock(int x,int y,Block b){
		if(b.forceCheckEnt())b.touchEnt(x,y,this);
		touchBlock(x,y,b);
		/*double m=mass();
		double Ek=m*(xv*xv+yv*yv);
		if(b.fallable()&&Ek>b.fallValue()){
			b.fall(x,y,xv,yv);
			impulse(xv,yv,-1);
		}*/
	}
	public final void updBlock(){
		boolean chk_block=chkBlock();
		xfl=xfs=yfl=yfs=fs=fs2=f=0;
		in_wall=false;
		boolean chk_rigidbody=chkRigidBody();
		if(chk_block){
			game.world.NearbyInfo ni=World.cur.getNearby(x,y,width(),height(),true,false,false);
			for(int y=0;y<ni.blocks.length;++y){
				Block[] b=ni.blocks[y];
				for(int x=0;x<b.length;++x){
					if(b[x].rootBlock().transparency()<0.6)shadowed=false;
					chkBlock(ni.xl+x,ni.yl+y,b[x]);
				}
			}
		}
		if(!in_wall&&!climbable){
			last_g=gA()*World.cur.setting.gravitational_constant*max(0,1-anti_g);
			if(World.cur.gtree!=null){
				World.cur.gtree.query(this);
			}else{
				ya-=last_g;
			}
		}else last_g=0;
		//apply_v();
		//if(this instanceof Player)System.out.printf("aa: %g, v: (%g,%g; %g)\n",aa,xv,yv,av);
		if(xfl>0&&ydep!=0){
			double y0=(ydep>0?top:bottom);
			double rxv=-xvAt(x,y0);
			double xF=min(0.5,xfs*frictionX()/(xfl+1e-8));
			impulse(x,y0,xF*rxv,0,mass());
			//if(this instanceof Player)System.out.printf("xF: %g, rxv: %g at (%g,%g)\n",xF,rxv,x-x,y0-y);
		}
		//if(this instanceof Player)System.out.printf("aa: %g\n",aa);
		if(yfl>0&&xdep!=0){
			double x0=(xdep>0?right:left);
			double yF=min(0.5,yfs*frictionY()/(yfl+1e-8));
			double ryv=-yvAt(x0,y);
			impulse(x0,y,0,yF*ryv,mass());
			//if(this instanceof Player)System.out.printf("yF: %g, ryv: %g at (%g,%g)\n",yF,ryv,x0-x,y-y);
		}
		//if(this instanceof Player)System.out.printf("aa: %g\n",aa);
		apply_v();
		if(fs2>0)fs2=max(fs2,0.1);
		fs=max(fs,fs2);
		if(fs>0||f>0){
			double rxv=-xvAt(x,y),ryv=-yvAt(x,y);
			double F1=fs;
			double F2=(1-min(1,f*friction()));
			double F3=(1-min(1,max(f,fs)*friction()));
			impulse(rxv*F1,ryv*F1,mass());
			apply_v();
			xv*=F2;yv*=F2;av*=F3;
		}
		if(fc!=0){
			double k=1/(1+fc*hypot(xv,yv)*World.cur.setting.air_drag_coefficient);
			xv*=k;
			yv*=k;
		}
		
		final double max_v=1,max_v_val=1e10;
		double abs_v=sqrt(xv*xv+yv*yv);
		if(abs_v>max_v_val){
			double v_scale=max_v_val/abs_v;
			xv*=v_scale;
			yv*=v_scale;
			abs_v=max_v_val;
		}
		double xv0=xv,yv0=yv;
		if(true){
			double c=abs_v/max_v;
			double v_scale=1/sqrt(1+c*c);
			xv*=v_scale;
			yv*=v_scale;
		}
		
		if(chk_rigidbody&&!in_wall){
			double hxv=xv/2,hyv=yv/2;
			game.world.NearbyInfo ni=World.cur.getNearby(x+hxv,y+hyv,width()+abs(hxv)+0.1,height()+abs(hyv)+0.1,true,false,false);

			in_wall=false;
			xdep=ydep=0;
			
			for(int y=0;y<ni.blocks.length;++y){
				Block[] b=ni.blocks[y];
				for(int x=0;x<b.length;++x){
					b[x].rigidBodyHitTest(ni.xl+x,ni.yl+y,this);
				}
			}
			
			
			if(in_wall){
				x+=xv;
				y+=yv;
			}else{
				double vd=1-mov(1);

				for(int y=0;y<ni.blocks.length;++y){
					Block[] b=ni.blocks[y];
					for(int x=0;x<b.length;++x){
						b[x].rigidBodyHitTest(ni.xl+x,ni.yl+y,this);
					}
				}
				vd-=mov(vd);
			}
		}else{
			x+=xv;
			y+=yv;
		}
		
		//av%=(PI*2);
		a=(a+av)%(PI*2);
		
		double k=K();
		
		xv=(xv!=0?xv0:xv0*-k);yv=(yv!=0?yv0:yv0*-k);
		
		apply_v();
		//fallBlock();
	}
	protected double K(){return 0;}
	private final void upd_v(){
		double f1=friction();
		xf*=f1;yf*=f1;f*=f1;
		xv*=(1-min(1,xf))*(1-min(1,f));
		yv*=(1-min(1,yf))*(1-min(1,f));
		if(in_wall)av*=(1-min(1,f/mass()));
	}
	double friction(){return 1;}//摩擦力
	double frictionX(){return friction();}//摩擦力
	double frictionY(){return friction();}//摩擦力
	public AttackFilter getAttackFilter(){return identity_filter;}//攻击过滤器
	public boolean dead=false;
	
	Source atker=null;
	double atked_sum=0;
	
	public void onAttackedTip(Source src,double weight){}
	
	public final void loseHp(double a,Source src){
		if(src instanceof Human)debug.Log.printStackTrace();
		if(hp>0&&a>0){
			double weight=min(a,hp+1);
			onAttackedTip(src,weight);
			if(src.getSrc()==null)weight*=1e-4;
			atked_sum+=weight;
			if(rnd(atked_sum)<weight)atker=src;
			
			hp=max(0,hp-a);
			if(hp==0&&!dead){
				onKilled(atker);
				dead=true;
			}
		}
	}
	public void onKilled(Source src){}
	public void onAttacked(Attack att){}//被攻击
	private void _onAttacked(Attack att){
		if(att!=null){
			AttackFilter af=getAttackFilter();
			if(af!=null)att=af.transform(att);
			if(att!=null){
				if(att instanceof NormalAttack){
					att.val*=((NormalAttack)att).getWeight(this);
				}
				onAttacked(att);
				loseHp(att.val,att.src);
			}
		}
	}
	public final void onAttacked(double v,Entity w){//普通攻击
		onAttacked(v,w,w);
	}
	public final void onAttacked(double v,Source w,NormalAttacker z){//普通攻击
		_onAttacked(new NormalAttack(w,v,z));
	}
	public final void onAttackedByEnergy(double v,Source w){//能量石攻击
		_onAttacked(new EnergyAttack(w,v));
	}
	public final void onAttackedByFire(double v,Source w){//火/热攻击
		_onAttacked(new FireAttack(w,v));
	}
	public final void onAttackedByDark(double v,Source w){//影/空间攻击
		_onAttacked(new DarkAttack(w,v));
	}
	void touchBlock(int x,int y,Block b){}//事件：接触方块
	final boolean hitTest(Entity ent){//碰撞检测
		if(active()&&ent.active()&&
			abs(x-ent.x)<=width()+ent.width()&&
			abs(y-ent.y)<=height()+ent.height()){
			if(friend_group!=null&&friend_group==ent.friend_group){
				if(this instanceof Agent&&ent instanceof Agent)return true;
				return false;
			}
			return true;
		}
		return false;
	}
	public double touchVal(){return 1;}
	void touchEnt(Entity ent,boolean chk_ent){
		if(chk_ent)touchEnt(ent);
	}
	void touchEnt(Entity ent){//事件：接触实体
		double m=min(mass(),ent.mass());
		double x1=max(left,ent.left),x2=min(right,ent.right);
		double y1=max(bottom,ent.bottom),y2=min(top,ent.top);
		double xl=x2-x1;
		double yl=y2-y1;
		double S=xl*yl;
		if((left<ent.left)==(right>ent.right))yl=0;
		if((bottom<ent.bottom)==(top>ent.top))xl=0;
		double l=sqrt(xl*xl+yl*yl+1e-8);
		double c=min(S/V,0.4)*0.15*ent.touchVal()*touchVal();
		double x_=yl*(x<ent.x?-1:1),y_=xl*(y<ent.y?-1:1);
		double x0=(x1+x2)/2,y0=(y1+y2)/2;
		impulse(x0,y0,x_,y_,m*c);
		ent.impulse(x0,y0,x_,y_,-m*c);
		
		exchangeVel(x0,y0,ent,0.1);
	}
	void touchAgent(Agent ent){//事件：接触实体
		touchEnt(ent);
	}
	public boolean isDead(){return hp<=0||removed;}
	public final void onDestroy(){
		if(!removed){
			onKill();
			remove();
		}
	}
	public double onImpact(double v){return 0;}//撞击伤害计算
	void onKill(){}//事件：被移除
	public double width(){return 0.1;}//half of width
	public double height(){return 0.1;}//half of height
	public double mass(){return 1;}//mass
	public double gA(){return 0.03;}//gravitational acceleration
	public void draw(Canvas cv){//绘制
		float w=(float)width();
		float h=(float)height();
		BmpRes bmp=getBmp();
		if(bmp!=null){
			float a=getRotation();
			if(a!=0){
				cv.save();
				cv.rotate(a);
				bmp.draw(cv,0,0,w,h);
				cv.restore();
			}else{
				bmp.draw(cv,0,0,w,h);
			}
		}
	}
	public float getRotation(){
		if(rotByVelDir())return (float)(a*180/PI);
		return 0;
	}
	public boolean rotByVelDir(){return false;}//绘制时是否根据速度方向旋转
	public void add(){
		if(is_test){
			last_launched_ent=this;
			return;
		}
		if(is_calc_impulse){
			double m=mass();
			xmv+=m*xv;
			ymv+=m*yv;
		}
		if(x!=x||y!=y)return;
		World.cur.newEnt(this);
	}//add to world
	protected static final AttackFilter
	all_filter=new AttackFilter(){public Attack transform(Attack a){
		return null;
	}},special_filter=new AttackFilter(){public Attack transform(Attack a){
		return (a instanceof NormalAttack)?a:null;
	}},energy_filter=new AttackFilter(){public Attack transform(Attack a){
		return (a instanceof EnergyAttack)?null:a;
	}},fire_filter=new AttackFilter(){public Attack transform(Attack a){
		return (a instanceof FireAttack)?null:a;
	}},dark_filter=new AttackFilter(){public Attack transform(Attack a){
		return (a instanceof DarkAttack)?null:a;
	}},identity_filter=new AttackFilter(){public Attack transform(Attack a){
		return a;
	}};
	public double RPG_ExplodeProb(){return 1;}
	//tools
	public final double distL1(Entity e){
		return abs(x-e.x)+abs(y-e.y)+1e-16;
	}

	public final double distL2(Entity e){
		return hypot(x-e.x,y-e.y)+1e-16;
	}

	public final double distLinf(Entity e){
		return max(abs(x-e.x),abs(y-e.y))+1e-16;
	}
	
	public final double v2rel(Entity e){
		double xd=xv-e.xv,yd=yv-e.yv;
		return xd*xd+yd*yd;
	}
	
	public final double v2rel_rnd(Entity e){
		double x1=max(left,e.left),x2=min(right,e.right);
		double y1=max(bottom,e.bottom),y2=min(top,e.top);
		if(x1>x2||y1>y2)return 0;
		double x=rnd(x1,x2),y=rnd(y1,y2);
		double xd=xvAt(x,y)-e.xvAt(x,y),yd=yvAt(x,y)-e.yvAt(x,y);
		return xd*xd+yd*yd;
	}
	
	public double intersection(Entity ent){
		return max(0,min(right,ent.right)-max(left,ent.left))*max(0,min(top,ent.top)-max(bottom,ent.bottom));
	}
	public final Entity initAddVel(double xv,double yv){
		return initAddVel(xv,yv,0);
	}
	public final Entity initAddVel(double xv,double yv,double av){
		this.xv+=xv;
		this.yv+=yv;
		this.av+=av;
		return this;
	}
	public final Entity initPos(double x,double y,double xv,double yv,Source src){
		this.x=x;
		this.y=y;
		this.xv=xv0=xv;
		this.yv=yv0=yv;
		this.a=atan2(yv,xv);
		this.av=0;
		this.source=src;
		return this;
	}
	public final void impulse(Entity ent,double k){
		impulse(ent.x,ent.y,ent.xv,ent.yv,k*ent.mass());
	}
	public final void impulseMerge(Entity ent){
		impulseMerge(ent.xv,ent.yv,ent.mass());
	}
	public final void acc(double xv,double yv,double mv2){
		double c=sqrt(mv2/mass())/(hypot(xv,yv)+1e-8);
		this.xa+=c*xv;
		this.ya+=c*yv;
	}
	public final double I(){return 0.5*mass()*width()*height();}
	public final void impulse(double x,double y,double xv,double yv,double m){
		if(rotByVelDir()){
			double I=this.I();
			double c=(x-this.x)*yv-(y-this.y)*xv;
			double beta=c*m/I;
			//if(this instanceof game.entity.StoneBall)System.out.printf("%g,%g; %g,%g;  c=%g  b=%g  a=%g  av=%g  I=%g\n",x-this.x,y-this.y,xv,yv,c,beta,a,av,I);
			aa+=beta;
		}
		impulse(xv,yv,m);
	}
	public final void impulseMerge(double xv,double yv,double m){
		double m0=mass(),ms=m0+m;
		xa+=(xv-(this.xv+this.xa))*(m/ms);
		ya+=(yv-(this.yv+this.ya))*(m/ms);
	}
	public final void impulse(double xv,double yv,double m){
		double k=m/mass();
		xa+=xv*k;
		ya+=yv*k;
	}
	public final Entity setHpScale(double k){
		hp*=k;
		return this;
	}
	public final Entity setPos(double x,double y){
		this.x=x;
		this.y=y;
		return this;
	}
	public final void exchangeVel(Entity e,double k){
		double m0=min(mass(),e.mass())*k;
		double rxv=(xv+xa)-(e.xv+e.xa),ryv=(yv+ya)-(e.yv+e.ya);
		e.impulse(rxv,ryv,m0);
		impulse(rxv,ryv,-m0);
	}
	public double xvAt(double x0,double y0){
		return xv-(y0-y)*(av);
	}
	public double yvAt(double x0,double y0){
		return yv+(x0-x)*(av);
	}
	public final void exchangeVel(double x0,double y0,Entity e,double k){
		double m0=min(mass(),e.mass())*k;
		double rxv=xvAt(x0,y0)-e.xvAt(x0,y0);
		double ryv=yvAt(x0,y0)-e.yvAt(x0,y0);
		e.impulse(x0,y0,rxv,ryv,m0);
		impulse(x0,y0,rxv,ryv,-m0);
	}
	public final void thrownFrom(Agent src,double x,double y,double xv,double yv){
		double W=width()+src.width()+0.05,H=height()+src.height()+0.05;
		
		double m0=src.mass(),m1=mass();
		double p=sqrt(m1*(xv*xv+yv*yv)/(1/m0+1/m1));
		double v_scale=p/(m1*hypot(xv,yv));
		xv*=v_scale;
		yv*=v_scale;
		
		double tx=1e10,ty=1e10;
		if(xv>0)tx=((src.x+W)-x)/xv;
		if(xv<0)tx=((src.x-W)-x)/xv;
		if(yv>0)ty=((src.y+H)-y)/yv;
		if(yv<0)ty=((src.y-H)-y)/yv;
		double t=min(max(0,tx),max(0,ty));
		x+=xv*t;y+=yv*t;
		
		Source launcher=src.getLaunchSrc();
		if(launcher instanceof Agent&&!harmless()){
			friend_group=((Agent)launcher).friend_group;
		}
		double src_v=max(1,hypot(src.xv,src.yv));
		initPos(x+src.xv/src_v,y+src.yv/src_v,xv,yv,SourceTool.launch(launcher));
		if(!is_test){
			Agent w=launcher.getSrc();
			if(w==null)w=src;
			w.impulse(this,-1);
		}
		initAddVel(src.xv,src.yv);
		if(is_test)last_launched_ent=this;
		else add();
	}
	public double xmov(){return 0;}
	public double ymov(){return 0;}
	public static double predictHit(Entity w,Entity target){
		return predictHit(w,target,null);
	}
	public static double predictHit(Entity w,Entity target0,NearbyInfo ni){
		if(w==null||target0==null)return 1e10;
		Entity target=new SimEntity(target0);
		double x=target.x,y=target.y;
		double W=w.width()+target.width();
		double H=w.height()+target.height();
		//target.test_step();
		//boolean flag=false;
		double md=1e10;
		//Line.begin();
		for(int i=0;i<400;++i){
			double d=max(abs(w.x-target.x)-W,abs(w.y-target.y)-H);
			if(ni!=null){
				if(ni.hitTest(w)){
					for(Entity e:ni.ents){
						if(hypot(e.xv,e.yv)>0.2)continue;
						if(w.hitTest(e))return md;
					}
					for(Entity e:ni.agents){
						if(hypot(e.xv,e.yv)>0.2)continue;
						if(e==target0)continue;
						if(w.hitTest(e))return md;
					}
				}
			}
			md=min(md,d);
			if(i==0){
				if(w.test_chkBlock())break;
			}
			if(w instanceof Bullet && hypot(w.xv,w.yv)<0.4)break;
			if(w instanceof Arrow && hypot(w.xv,w.yv)<0.3)break;
			if(hypot(w.xv,w.yv)<1e-2&&hypot(target.xv,target.yv)<1e-2)break;
			w.test_update();
			w.test_step();
			target.test_update();
			target.test_step();
			if(ni!=null&&World.cur.setting.predict_hit_tip)Line.gen(w,0xff0000ff);
			//Line.gen(target,0xffff0000);
		}
		//Line.end(true);
		return md;
	}
	public static void predict(Entity w){
		if(w==null)return;
		if(w instanceof Agent)w=new SimEntity(w);
		Line.begin();
		double x0=1e10;
		int stop_t=0;
		for(int i=0;i<1000;++i){
			w.test_update();
			w.test_step();
			if(x0==w.x)++stop_t;
			x0=w.x;
			if(stop_t>5)break;
			Line.gen(w,0xff00ff00);
		}
		Line.end(true);
	}
	double _fc(){return 0.001;}
	public void test_update(){
		update0();
		if(useRandomWalk()){
			impulse(rnd_gaussion(),rnd_gaussion(),(World.cur.setting.random_walk_coefficient)*sqrt(hypot(xv,yv))*(width()+height()));
		}
		upd_a();
		
		fc+=_fc()/mass();
	}
	public boolean test_chkBlock(){
		if(chkBlock()||chkRigidBody()){
			double w=width(),h=height();
			Block
			b=World.cur.get(x-w,y-h);
			if(b.isSolid()||b.chkNonRigidEnt())return true;
			b=World.cur.get(x-w,y+h);
			if(b.isSolid()||b.chkNonRigidEnt())return true;
			b=World.cur.get(x+w,y-h);
			if(b.isSolid()||b.chkNonRigidEnt())return true;
			b=World.cur.get(x+w,y+h);
			if(b.isSolid()||b.chkNonRigidEnt())return true;
		}
		return false;
	}
	public final void test_step(){
		if(test_chkBlock()){xv=yv=xa=ya=0;}
		else{
			last_g=gA()*World.cur.setting.gravitational_constant;
			if(last_g!=0&&World.cur.gtree!=null){
				World.cur.gtree.query(this);
			}else{
				ya-=last_g;
			}
		}
		if(fc!=0){
			double k=1/(1+fc*hypot(xv,yv));
			xv*=k;
			yv*=k;
		}
		xv+=xa;yv+=ya;av+=aa;
		upd_v();
		double v_scale=1/sqrt(1+xv*xv+yv*yv);
		x+=xv*v_scale;y+=yv*v_scale;a+=av;
	}
	protected boolean useRandomWalk(){return true;}
}
class SimEntity extends Entity{
	private static final long serialVersionUID=1844677L;
	double w,h,g;
	public double width(){return w;}
	public double height(){return h;}
	public double gA(){return g;}
	public double hardness(){return game.entity.NormalAttacker.POWDER;}
	public SimEntity(Entity e){
		x=e.x;y=e.y;a=e.a;
		xv=e.xv;yv=e.yv;av=e.av;
		xa=e.xv;ya=e.yv;aa=e.av;
		xf=e.xf;yf=e.yf;f=e.f;
		
		w=e.width();h=e.height();
		if(e instanceof Human && ((Human)e).armor.get() instanceof game.item.Airship&&e.anti_g>0.99)w+=2;
		g=e.last_g;
		//g=e.gA()*(1-min(1,e.anti_g));
		//if(e.ydep<0||e.in_wall||e.climbable)g=0;
	}
}
