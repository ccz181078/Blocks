package game.block;

import static java.lang.Math.*;
import static util.MathUtil.*;
import util.BmpRes;
import game.world.*;
import game.entity.*;
import game.item.*;
import java.io.*;
import graphics.Canvas;
import game.ui.BlockWithUI;

public abstract class Block extends Item implements BlockItem,NormalAttacker{
	private static final long serialVersionUID=1844677L;//这一行每个被存档的类都要写
	//所有方块的基类
	//方块本身没有坐标，方块可以直接存在于世界中的某个整数坐标，也可以存在于一个BlockItem物品中
	
	static BmpRes crack_bmp[]=BmpRes.load("Block/Crack/",13);
	int damage=0;
	public int shockWaveResistance(){return maxDamage()-damage;}
	public transient Agent src=null;
	transient public long last_chk_time=0;
	
	//获取根方块（对有电路的方块会获得放置电路前的方块，否则获得的就是自身）
	//一般只在判定方块类别时用到
	public Block rootBlock(){return this;}
	@Override
	public Block clone(){return (Block)super.clone();}
	
	public boolean chkNonRigidEnt(){return false;}
	public int mass(){return isCoverable()?0:1;}
	/*//item
	//和物品相关的部分
	
	//把方块包装成物品
	public final Item asItem(){return new BlockItem(this);}
	
	//浅拷贝
	//方块在成为物品时不应有深的信息
	public Block clone(){
		Block b=null;
		try{
			b=(Block)super.clone();
		}catch(Exception e){}
		return b;
	}*/
	public SingleItem[] getItems(){
		return new SingleItem[0];
	}
	
	public int getCraftType(){
		return 0;
	}
	public boolean isDeep(){return false;}
	
	//basic info and event
	//基本信息和事件
	
	//
	public double impactValue(){return max(0,min(100,maxDamage()*0.3));}
	public double fallValue(){return max(0,maxDamage()-damage)*0.01+2;}
	int maxDamage(){return 100000000;}
	public double maxHp(){return maxDamage();}
	public double transparency(){return 1;}
	public double light(){return 0;}
	public boolean isCoverable(){return false;}//return whether you can place a block on the position of "this"
	public boolean isSolid(){return true;}
	public void onFall(game.entity.FallingBlock b){}
	public void onPlace(int x,int y){}
	public void onPress(int x,int y,Item item){}
	public boolean onClick(int x,int y,Agent agent){
		if((this instanceof BlockWithUI)&&(agent instanceof Player)){
			((Player)agent).openDialog(((BlockWithUI)this).getUI(new BlockAt(x,y,this)));
		}
		return false;
	}
	public boolean updateCond(int x,int y){return false;}
	public Block deStatic(int x,int y){
		return this;
	}
	//called when a block appears or disappears
	//check block state
	//called at most once per World.update
	//return whether the block is removed
	//在方块出现或消失时调用
	//一般用于检查方块状态，或者对方块进行不间断的更新
	//每次世界更新中，一个方块至多被检查一次
	//返回方块是否被移除
	public boolean onCheck(int x,int y){
		if(damage>=maxDamage()){
			World.cur.setVoid(x,y);
			onDestroy(x,y);
			return true;
		}
		return false;
	}
	
	//called when a block is randomly updated
	//return whether the block is removed
	//当方块被随机更新时调用（平均几秒一次）
	//返回方块是否被移除
	public boolean onUpdate(int x,int y){
		return onCheck(x,y);
	}
	
	@Override
	public void onVanish(double x,double y,Source src){
		int px=f2i(x),py=f2i(y);
		if(!isDeep()&&rnd()<0.9)new FallingBlock(px,py,this).initPos(x,y,0,0,src).add();
		else if(World.cur.get(px,py) instanceof AirBlock)placeAt(px,py);
	}
	
	//被光照
	public void onLight(int x,int y,double v){}
	public int getDamage(){return damage;}
	//强制地增加方块损坏度，并检查损坏
	public void des(int x,int y,double v,NormalAttacker z){
		v*=z.getWeight(this);
		des(x,y,rf2i(v));
	}
	public void repair(int x,int y,int v){
		damage=max(0,damage-v);
	}
	protected void des(int x,int y,int v){
		Block b=World.cur.get(x,y);
		/*if(b!=this){
			//System.err.println(this+" != "+b);
			return;
		}*/
		damage+=v;
		if(damage>=maxDamage())World.cur.checkBlock(x,y);
	}
	//被破坏/覆盖时调用
	public void onDestroy(int x,int y){
		//called when "this" is destroyed
		damage=0;
		drop(x,y);
	}
	public void onDestroy(double x,double y){
		//called when "this" is destroyed
		onDestroy(f2i(x),f2i(y));
	}
	
	//fire
	//和火相关
	
	//作为物品时
	public int fuelVal(){return 0;}
	public int heatingTime(boolean in_furnace){return 1000000000;}
	public Item heatingProduct(boolean in_furnace){return new AirBlock();}
	@Override
	public void onLaunchAtPos(game.entity.Agent a,int dir,double x,double y,double slope,double mv2){
		if(isDeep())super.onLaunchAtPos(a,dir,x,y,slope,mv2);
		else a.throwEntAtPos(new game.entity.FallingBlock(0,0,this),dir,x,y,slope,mv2);
	}
	
	//被点火
	public void onFireUp(int x,int y){
		int v=fuelVal();
		if(v>0){
			int x1=x,y1=y;
			if(rnd()<0.5)x1+=rnd()<0.5?-1:1;
			else y1+=rnd()<0.5?-1:1;
			if(World.cur.get(x1,y1).isCoverable())World.cur.place(x1,y1,new FireBlock());
		}
	}
	
	//被灼烧
	public void onBurn(int x,int y){
		int v=fuelVal();
		if(v>0){
			if(v*rnd()<10){
				World.cur.setAir(x,y);
				onDestroyByFire(x,y);
			}
		}
	}
	
	//event: be destroyed by fire
	public void onDestroyByFire(int x,int y){}
	
	
	//energystone circuit
	//能量石电路相关
	
	//水平能量信号
	public int energyValL(){return 0;}
	public int energyValR(){return 0;}
	//竖直能量信号
	public int energyValD(){return 0;}
	public int energyValU(){return 0;}
	//可以在上面放置电路
	public boolean circuitCanBePlaced(){return isSolid();}
	
	
	//physics
	//物理相关
	
	//作为刚体，对实体的滑动摩擦力（相对值）
	double friction(){return 0.125;}//表面
	double frictionIn1(){return 0.5;}//内部
	double frictionIn2(){return isSolid()?500:0.05;}//内部
	double minEnterVel(){return isSolid()?0.3:0;}
	
	//接触实体
	public boolean forceCheckEnt(){return isSolid();}
	public void touchEnt(int x,int y,Entity ent){
		if(!ent.chkRigidBody()&&!chkNonRigidEnt())return;
		double xd=x+0.5-ent.x,yd=y+0.5-ent.y;//relative position
		double W=0.45+ent.width(),H=0.45+ent.height();
		double xc=min(x+1,ent.right)-max(x,ent.left);
		double yc=min(y+1,ent.top)-max(y,ent.bottom);
		if(abs(xd)<W+0.05&&abs(yd)<H+0.05){
			double mev=fallValue(),m=ent.mass();
			if((ent.xv*ent.xv*yc+ent.yv*ent.yv*xc)*m*rnd_exp(1)>mev){
				if(World.cur.get(x,y)==this){
					double m0=min(m,1);
					fall(x,y,ent.xv*m0,ent.yv*m0);
					ent.impulse(ent.xv,ent.yv,-m0);
				}
			}
		}
		if(abs(xd)<W&&abs(yd)<H){
			ent.in_wall|=isSolid();
			onOverlap(x,y,ent,intersection(x,y,ent));
			return;
		}
		double f=friction();
		if(f==0)return;
		
		
		if(xc>=0.06&&yc<0.06){
			//double c=xc/(ent.right-ent.left);
			//double rxv=-ent.xvAt(x0,y0);
			ent.xfl+=xc;
			ent.xfs+=xc*f;
			//double a=c*friction()*rxv;
			//ent.impulse(x0,y0,a,0,ent.mass());
		}
		if(yc>=0.06&&xc<0.06){
			ent.yfl+=yc;
			ent.yfs+=yc*f;
			//double x0=(x+0.5<ent.x?x+1:x),y0=ent.y;
			//double c=yc/(ent.top-ent.bottom);
			//double ryv=-ent.yvAt(x0,y0);
			//double a=c*friction()*ryv;
			//ent.impulse(x0,y0,0,a,ent.mass());
		}
	}
	
	public void onOverlap(int x,int y,Entity ent,double k){
		ent.fs=max(ent.fs,frictionIn1()/max(1,ent.mass()));
		ent.fc+=k*frictionIn2()/ent.mass();
		//ent.f+=k*frictionIn();
		//ent.inblock+=1;
		//ent.anti_g+=1;
		double x1=max(x,ent.left),x2=min(x+1,ent.right);
		double y1=max(y,ent.bottom),y2=min(y+1,ent.top);
		double x0=rnd(x1,x2),y0=rnd(y1,y2);
		double rxv=ent.xvAt(x0,y0);
		double ryv=ent.yvAt(x0,y0);
		k*=0.5*ent.mass()*min(1,frictionIn2());
		ent.impulse(x0,y0,rxv,ryv,-k);
	}
	@Override
	public Entity asEnt(){
		if(fallable())return new FallingBlock(0,0,this);
		return super.asEnt();
	}
	public boolean fallable(){
		return !isDeep();
	}
	public void fall(int x,int y,double xmv,double ymv){
		World.cur.setVoid(x,y);
		if(fallable()){
			double m=mass();
			new FallingBlock(x,y,this).initPos(x+0.5,y+0.5,xmv/m,ymv/m,null).add();
		}else onDestroy(x,y);
	}
	public double getFallHp(){return 100;}
	//与实体进行刚体碰撞测试
	public final void rigidBodyHitTest(int x,int y,Entity ent){
		if(ent.in_wall)return;
		double mev=minEnterVel();
		if(mev==0)return;
		double xd=x+0.5-ent.x,yd=y+0.5-ent.y;//relative position
		double W=0.45+ent.width(),H=0.45+ent.height();
		double xc=min(x+1,ent.right)-max(x,ent.left);
		double yc=min(y+1,ent.top)-max(y,ent.bottom);
		double xmv=0,ymv=0;
		if(ent.xv>0)if(xd-W>0){
			if(xd-W<ent.xv)if(abs(ent.yv*(xd-W)/ent.xv-yd)<H){
				xmv=yc/(ent.top-ent.bottom)*ent.xv*ent.mass();
				ent.xdep=max(ent.xdep,ent.xv-(xd-W));
			}
		}
		if(ent.xv<0)if(xd+W<0){
			if(xd+W>ent.xv)if(abs(ent.yv*(xd+W)/ent.xv-yd)<H){
				ent.xdep=min(ent.xdep,ent.xv-(xd+W));
				xmv=yc/(ent.top-ent.bottom)*ent.xv*ent.mass();
			}
		}
		if(ent.yv>0)if(yd-H>0){
			if(yd-H<ent.yv)if(abs(ent.xv*(yd-H)/ent.yv-xd)<W){
				ymv=xc/(ent.right-ent.left)*ent.yv*ent.mass();
				ent.ydep=max(ent.ydep,ent.yv-(yd-H));
			}
		}
		if(ent.yv<0)if(yd+H<0){
			if(yd+H>ent.yv)if(abs(ent.xv*(yd+H)/ent.yv-xd)<W){
				ymv=xc/(ent.right-ent.left)*ent.yv*ent.mass();
				ent.ydep=min(ent.ydep,ent.yv-(yd+H));
				if(ent instanceof Agent&&ent.yv-(yd+H)<0&&abs(ent.yv)<0.051){
					double len=(min(x+1,ent.right)-max(x,ent.left))/(ent.width()*2);
					if(len>1e-2){
						Agent a=(Agent)ent;
						a.jump_acc+=len*getJumpAcc();
						a.jump_len+=len;
					}
				}
			}
		}
		//if(abs(xmv)+abs(ymv)>0.2)System.out.println(x+","+y+"   "+abs(xmv)+","+abs(ymv)+"  "+mev);
		/*if(abs(xmv)>mev||abs(ymv)>mev){
			if(World.cur.get(x,y)==this){
				fall(x,y,xmv,ymv);
			}
		}*/
		/*double xc=min(x+1,ent.right)-max(x,ent.left),yc=min(y+1,ent.top)-max(y,ent.bottom);//intersection
		if(xc>0&&yc>0)ent.onblock+=1;
		if(yc>0.11){
			ent.yf+=yc*friction()/(ent.height()*2);
			if(xd<0)ent.ldep=max(ent.ldep,xc);
			else ent.rdep=max(ent.rdep,xc);
		}
		if(xc>0.11){
			ent.xf+=xc*friction()*2/(ent.width()*2);
			if(yd<0)ent.ddep=max(ent.ddep,yc);
			else ent.udep=max(ent.udep,yc);
		}*/
		
	}
	//求跳跃加速度（相对值）
	public double getJumpAcc(){return 1;}
	
	//求方块和实体的交占实体的比例
	public static double intersection(int x,int y,Entity ent){
		double xc=min(x+1,ent.right)-max(x,ent.left);
		double yc=min(y+1,ent.top)-max(y,ent.bottom);
		if(xc>0&&yc>0)return xc*yc/ent.V;
		return 0;
	}
	
	public static double intersectionLength(int x,int y,Entity ent){
		double xc=min(x+1,ent.right)-max(x,ent.left);
		double yc=min(y+1,ent.top)-max(y,ent.bottom);
		return max(0,xc)+max(0,yc);
	}
	
	//draw
	//绘制相关
	
	//获取默认贴图
	public BmpRes getBmp(){
		return new BmpRes("Block/"+getClass().getSimpleName());
	}
	
	protected int crackType(){return 0;}
	
	//默认绘制
	public void draw(Canvas cv){
		getBmp().draw(cv,0,0,0.5f,0.5f);
		if(damage>0&&maxDamage()>1){
			int tp=crackType();
			if(tp==0){
				int t=max(0,min(2,damage*3/maxDamage()));
				crack_bmp[10+t].draw(cv,0,0,0.5f,0.5f);
			}else if(tp==1){
				int t=max(0,min(9,damage*10/maxDamage()));
				crack_bmp[t].draw(cv,0,0,0.5f,0.5f);
			}
		}
	}
	
	public Item clickAt(double x,double y,Agent a){
		if(this instanceof AirBlock)return super.clickAt(x,y,a);
		int px=f2i(x),py=f2i(y);
		boolean placeable=World.cur.placeable(px,py);
		if(a instanceof Player){
			Player pl=(Player)a;
			if(pl.creative_mode&&pl.suspend_mode)placeable=World.cur.get(px,py).isCoverable();
		}
		if(placeable&&placeAt(px,py)){
			src=a;
			return null;
		}
		if(a instanceof Player){
			((Player)a).fail_to_place_block=10;
		}
		return super.clickAt(x,y,a);
	}
	public boolean placeAt(int x,int y){
		World.cur.place(x,y,this);
		return true;
	}
	public boolean xRev(){return false;}
	
	static void onLadderTouchEnt(int x,int y,Entity ent){
		double xd=x+0.5-ent.x,yd=y+0.5-ent.y;//relative position
		double W=0.45+ent.width(),H=0.45+ent.height();
		if(ent instanceof Agent && ((Agent)ent).ydir!=-1 && max(abs(xd)-W,abs(yd)-H)<0)ent.climbable=true;
	}
	static void onScaffoldTouchEnt(int x,int y,Entity ent){
		double h=min(y+1,ent.top)-max(y,ent.bottom);
		if(h>0.051)ent.climbable=true;
	}
};

abstract class AirType extends Block{
	private static final long serialVersionUID=1844677L;
	public boolean isCoverable(){return true;}
	public boolean isSolid(){return false;}
	public double transparency(){return 0;}
	public void touchEnt(int x,int y,Entity ent){}
	@Override
	public boolean fallable(){return false;}
};


