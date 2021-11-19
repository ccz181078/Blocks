package game.world;

import static java.lang.Math.*;
import static util.MathUtil.*;
import static game.world.World.*;
import game.block.*;
import java.util.*;
import java.io.*;
import game.entity.*;

public class GroupFall{
	static long flag_[]=new long[64];
	static int x0_,y0_;
	static ArrayList<BlockAt> bs=new ArrayList<>();
	static ArrayList<BlockAt> floating=new ArrayList<>();
	static boolean out(int x,int y){
		x=x-x0_+32;
		y=y-y0_+32;
		return ((x|y)&~63)!=0;
	}
	static boolean visited(int x,int y){
		x=x-x0_+32;
		y=y-y0_+32;
		return (flag_[x]>>y&1)!=0;
	}
	static void visit(int x,int y){
		x=x-x0_+32;
		y=y-y0_+32;
		flag_[x]|=1l<<y;
	}
	static void unvisit(int x,int y){
		x=x-x0_+32;
		y=y-y0_+32;
		flag_[x]&=~(1l<<y);
	}
	static class CheckMove extends Event{
		BlockAt ba;
		CheckMove(BlockAt ba,int delay){
			this.ba=ba;
			add(delay);
		}
		@Override
		public void run(){
			if(World.cur.get(ba.x,ba.y)==ba.block){
				apply(ba.x,ba.y,20);
			}
		}
	}
	public static void apply(int x,int y){
		apply(x,y,5);
	}
	public static void apply(int x,int y,int min_cnt){
		if(!World.cur.getMode().enable_group_fall)return;
		x0_=x;y0_=y;
		BlockAt ba=World.cur.get1(x,y);
		if(!ba.block.isCoverable()){
			double p=rnd(0.22032345557),s=0;
			int cnt=5;
			for(;cnt<=1000;++cnt){
				s+=1./(cnt*cnt);
				if(s>p)break;
			}
			cnt=max(min_cnt,cnt);
			visit(ba.x,ba.y);
			bs.add(ba);
			boolean fall=true;
			o:
			for(int i=0;i<bs.size();++i){
				ba=bs.get(i);
				if(ba.block instanceof BedRockBlock){fall=false;break o;}
				if(ba.block instanceof FloatingBlock){
					floating.add(ba);
				}
				for(BlockAt ba1:World.cur.get4(ba.x,ba.y)){
					if(out(ba1.x,ba1.y)){fall=false;break o;}
					if(!ba1.block.isCoverable()&&!visited(ba1.x,ba1.y)){
						if(--cnt<=0){fall=false;break o;}
						visit(ba1.x,ba1.y);
						bs.add(ba1);
					}
				}
			}
			for(BlockAt ba1:bs)unvisit(ba1.x,ba1.y);
			if(fall){
				int tot=bs.size();
				int xdir=0,ydir=0;
				boolean flag=true;
				boolean movable=true;
				for(BlockAt ba1:floating){
					FloatingBlock fb=(FloatingBlock)ba1.block;
					if(fb.hasEnergy(tot)){
						fb.loseEnergy(tot);
						flag=false;
						if(fb.last_move_time+30>World.cur.time)movable=false;
						if(World.cur.get(ba1.x-1,ba1.y).energyValR()>0)xdir+=1;
						if(World.cur.get(ba1.x+1,ba1.y).energyValL()>0)xdir-=1;
						if(World.cur.get(ba1.x,ba1.y-1).energyValU()>0)ydir+=1;
						if(World.cur.get(ba1.x,ba1.y+1).energyValD()>0)ydir-=1;
					}
				}
				if(flag)
				for(BlockAt ba1:bs){
					ba1.block.fall(ba1.x,ba1.y,rnd_gaussion()*0.05,rnd_gaussion()*0.05);
				}
				else if(movable){
					for(BlockAt ba1:floating){
						FloatingBlock fb=(FloatingBlock)ba1.block;
						fb.last_move_time=World.cur.time;
					}
					if(xdir!=0)xdir=(xdir>0?1:-1);
					if(ydir!=0)ydir=(ydir>0?1:-1);
					BlockAt ba0=floating.get(0);
					new CheckMove(new BlockAt(ba0.x+xdir,ba0.y+ydir,ba0.block),32);
					int xl=Integer.MAX_VALUE,xr=Integer.MIN_VALUE,yl=Integer.MAX_VALUE,yr=Integer.MIN_VALUE;
					if(xdir!=0||ydir!=0){
						for(BlockAt ba1:bs){
							World.cur.set(ba1.x,ba1.y,new AirBlock());
							xl=min(xl,ba1.x);
							xr=max(xr,ba1.x);
							yl=min(yl,ba1.y);
							yr=max(yr,ba1.y);
						}
						for(BlockAt ba1:bs){
							World.cur.set(ba1.x+xdir,ba1.y+ydir,ba1.block);
						}
						for(BlockAt ba1:bs){
							World.cur.check4(ba1.x+xdir,ba1.y+ydir);
						}
						for(BlockAt ba1:bs){
							World.cur.check4(ba1.x,ba1.y);
						}
						double xl0=xl-0.1,xr0=xr+1.1;
						double yl0=yl-0.1,yr0=yr+1.1;
						NearbyInfo ni=World.cur.getNearby((xl0+xr0)/2,(yl0+yr0)/2,(xr0-xl0)/2,(yr0-yl0)/2,false,false,true);
						for(Agent a:ni.agents){
							a.anti_g+=1;
							new SetRelPos(a,a,xdir,ydir);
						}
					}
				}
			}
			floating.clear();
			bs.clear();
		}
	}
}
