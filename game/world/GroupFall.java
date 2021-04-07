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
	public static void apply(int x,int y){
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
			visit(ba.x,ba.y);
			bs.add(ba);
			boolean fall=true;
			o:
			for(int i=0;i<bs.size();++i){
				ba=bs.get(i);
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
				for(BlockAt ba1:bs){
					ba1.block.fall(ba1.x,ba1.y,rnd_gaussion()*0.05,rnd_gaussion()*0.05);
				}
			}
			bs.clear();
		}
	}
}