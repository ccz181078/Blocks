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

public abstract class Block extends Item{
	private static final long serialVersionUID=1844677L;//这一行每个被存档的类都要写
	//所有方块的基类
	//方块本身没有坐标，方块可以直接存在于世界中的某个整数坐标，也可以存在于一个BlockItem物品中
	
	static BmpRes crack_bmp[]=BmpRes.load("Block/Crack/",10);
	public int damage=0;
	transient public long last_chk_time=0;
	
	//获取根方块（对有电路的方块会获得放置电路前的方块，否则获得的就是自身）
	//一般只在判定方块类别时用到
	public Block rootBlock(){return this;}
	
	
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
	
	
	
	public int getCraftType(){
		return 0;
	}
	public boolean isDeep(){return false;}
	
	//basic info and event
	//基本信息和事件
	
	//
	int maxDamage(){return 100000000;}
	public double transparency(){return 1;}
	public boolean isCoverable(){return false;}//return whether you can place a block on the position of "this"
	public boolean isSolid(){return true;}
	public void onPlace(int x,int y){}
	public void onPress(int x,int y,Item item){}
	public boolean onClick(int x,int y,Agent agent){
		if((this instanceof BlockWithUI)&&(agent instanceof Player)){
			((Player)agent).openDialog(((BlockWithUI)this).getUI(new BlockAt(x,y,this)));
		}
		return false;
	}
	public boolean updateCond(int x,int y){return false;}
	
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
			World.cur.setAir(x,y);
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
	
	//强制地增加方块损坏度，并检查损坏
	public void des(int x,int y,int v){
		damage+=v;
		if(damage>=maxDamage())World.cur.checkBlock(x,y);
	}
	//被破坏/覆盖时调用
	public void onDestroy(int x,int y){
		//called when "this" is destroyed
		damage=0;
		drop(x,y);
	}
	
	
	//fire
	//和火相关
	
	//作为物品时
	public int fuelVal(){return 0;}
	public int heatingTime(boolean in_furnace){return 1000000000;}
	public Item heatingProduct(boolean in_furnace){return new AirBlock();}
	
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
	public int energyValX(){return 0;}
	//竖直能量信号
	public int energyValY(){return 0;}
	//可以在上面放置电路
	public boolean circuitCanBePlaced(){return isSolid();}
	
	
	//physics
	//物理相关
	
	//作为刚体，对实体的滑动摩擦力（相对值）
	double friction(){return 0.125;}
	
	//接触实体
	public void touchEnt(int x,int y,Entity ent){
		double xc=min(x+1,ent.right)-max(x,ent.left);
		double yc=min(y+1,ent.top)-max(y,ent.bottom);
		if(xc>0&&yc>0){
			if(xc>yc){
				ent.xf+=xc/(ent.right-ent.left)*friction()*2;
			}else{
				ent.yf+=yc/(ent.top-ent.bottom)*friction();
			}
		}
	}
	
	//与实体进行刚体碰撞测试
	public void rigidBodyHitTest(int x,int y,Entity ent){
		if(!isSolid())return;
		double xd=x+0.5-ent.x,yd=y+0.5-ent.y;//relative position
		double W=0.45+ent.width(),H=0.45+ent.height();
		if(abs(xd)<W&&abs(yd)<H){
			ent.in_wall=true;
			return;
		}
		if(ent.xv>0)if(xd-W>0){
			if(xd-W<ent.xv)if(abs(ent.yv*(xd-W)/ent.xv-yd)<H){
				ent.xdep=max(ent.xdep,ent.xv-(xd-W));
			}
		}
		if(ent.xv<0)if(xd+W<0){
			if(xd+W>ent.xv)if(abs(ent.yv*(xd+W)/ent.xv-yd)<H){
				ent.xdep=min(ent.xdep,ent.xv-(xd+W));	
			}
		}
		if(ent.yv>0)if(yd-H>0){
			if(yd-H<ent.yv)if(abs(ent.xv*(yd-H)/ent.yv-xd)<W){
				ent.ydep=max(ent.ydep,ent.yv-(yd-H));
			}
		}
		if(ent.yv<0)if(yd+H<0){
			if(yd+H>ent.yv)if(abs(ent.xv*(yd+H)/ent.yv-xd)<W){
				ent.ydep=min(ent.ydep,ent.yv-(yd+H));
			}
		}
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
	
	//求方块和实体的交占实体的比例
	public static double intersection(int x,int y,Entity ent){
		double xc=min(x+1,ent.right)-max(x,ent.left);
		double yc=min(y+1,ent.top)-max(y,ent.bottom);
		if(xc>0&&yc>0)return xc*yc/ent.V;
		return 0;
	}
	
	//draw
	//绘制相关
	
	//获取默认贴图
	public BmpRes getBmp(){
		return new BmpRes("Block/"+getClass().getSimpleName());
	}
	
	//默认绘制
	public void draw(Canvas cv){
		getBmp().draw(cv,0,0,0.5f,0.5f);
		if(damage>0&&maxDamage()>1){
			int t=max(0,min(9,damage*10/maxDamage()));
			crack_bmp[t].draw(cv,0,0,0.5f,0.5f);
		}
	}
	
	public Item clickAt(double x,double y,Agent a){
		if(this instanceof AirBlock)return super.clickAt(x,y,a);
		boolean placeable=World.cur.placeable(f2i(x),f2i(y));
		if(a instanceof Player){
			Player pl=(Player)a;
			if(pl.creative_mode&&pl.suspend_mode)placeable=World.cur.get(x,y).isCoverable();
		}
		if(placeable){
			World.cur.place(f2i(x),f2i(y),this);
			return null;
		}
		if(a instanceof Player){
			((Player)a).fail_to_place_block=10;
		}
		return super.clickAt(x,y,a);
	}
	public boolean xRev(){return false;}
};

abstract class AirType extends Block{
	private static final long serialVersionUID=1844677L;
	public boolean isCoverable(){return true;}
	public boolean isSolid(){return false;}
	public double transparency(){return 0;}
	public void touchEnt(int x,int y,Entity ent){}
};


