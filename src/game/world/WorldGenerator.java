package game.world;

//import static java.lang.Math.*;
import static java.lang.Math.min;
import static java.lang.Math.max;
import static java.lang.Math.abs;
import static util.MathUtil.*;
import static game.world.World.World_Height;
import game.block.*;
import java.util.*;
import java.io.*;
import static game.world.WorldGenerator.*;

class WorldGenerator implements Serializable{
	private static final long serialVersionUID=1844677L;
	//世界生成器，用于动态生成世界，储存了世界边界的信息
	int bedrock_y=2,lava_y=4,stone_y=60,dirt_d=5,nxt_plant=rndi(3,10);
	double yv=0;
	GroundGen ground=new DirtGen();
	ArrayList<SpecialGen>sgs=new ArrayList<>();
	static AirBlock _AirBlock;
	static StaticBlock _StoneBlock,_BedRockBlock,_DirtBlock,_SandBlock,_DarkSandBlock,_GravelBlock,_LavaBlock,_WaterBlock
	,_CoalOreBlock,_QuartzOreBlock;
	
	void init(){
		_AirBlock=new AirBlock();
		_StoneBlock=new StaticBlock(new StoneBlock());
		_BedRockBlock=new StaticBlock(new BedRockBlock());
		_DirtBlock=new StaticBlock(new DirtBlock());
		_SandBlock=new StaticBlock(new SandBlock());
		_DarkSandBlock=new StaticBlock(new DarkSandBlock());
		_GravelBlock=new StaticBlock(new GravelBlock());
		_LavaBlock=new StaticBlock(new LavaBlock());
		_WaterBlock=new StaticBlock(new WaterBlock());
		_CoalOreBlock=new StaticBlock(new CoalOreBlock());
		_QuartzOreBlock=new StaticBlock(new QuartzOreBlock());
	}
	
	//在世界的左侧或右侧生成一列方块
	Block[] nxt(){
		Block[] b=new Block[World_Height];
		int y=0;
		for(;y<bedrock_y;++y)b[y]=_BedRockBlock;
		for(;y<lava_y;++y)b[y]=_LavaBlock;
		for(;y<stone_y;++y)b[y]=_StoneBlock;
		for(y=stone_y;y<World_Height;++y)b[y]=_AirBlock;
		ground.gen(b,stone_y,stone_y+dirt_d);
		--nxt_plant;
		if(nxt_plant<0){
			if(rnd()<0.9)nxt_plant=rndi(6,10);
			else nxt_plant=rndi(6,60);
		}
		--ground.width;
		if(ground.width<=0){
			ground=ground.change();
		}
		if(rnd()<0.3){
			bedrock_y=max(0,min(bedrock_y+rndi(-1,1),lava_y-2));
		}
		if(rnd()<0.3){
			lava_y=max(bedrock_y+2,min(lava_y+rndi(-1,1),6));
		}
		if(rnd()<0.2){
			dirt_d=max(3,min(dirt_d+rndi(-1,1),6));
		}else if(abs(yv)>0.2||rnd()<0.3){
			stone_y+=rf2i(yv);
		}
		double mv=0.95;
		if(y<70)mv=0.7;
		if(y<50)mv=0.4;
		double mya=0.04;
		if(rnd()<0.03)mya=0.4;
		yv=max(-mv,min(yv+rnd(-mya,mya),mv));
		if(stone_y>90){
			stone_y=90;
			if(yv>0)yv=0;
		}
		if(stone_y<43){
			stone_y=43;
			if(yv<0)yv=0;
		}
		for(int i=0;i<sgs.size();++i){
			SpecialGen sg=sgs.get(i);
			if(sg.finished()){
				sgs.set(i,sgs.get(sgs.size()-1));
				sgs.remove(sgs.size()-1);
				--i;
			}else{
				sg.gen(this,b);
			}
		}
		if(rnd()<0.05){
			int py=rndi(0,100);
			if(b[py].rootBlock().getClass()==StoneBlock.class)b[py]=new BloodStoneBlock();
		}
		if(rnd()<0.007)sgs.add(new CaveGen(this));
		if(rnd()<0.05)sgs.add(new GravelGen(this));
		if(rnd()<0.07)sgs.add(new QuartzOreGen(this));
		if(rnd()<0.04)sgs.add(new IronOreGen(this));
		if(rnd()<0.07)sgs.add(new CoalOreGen(this));
		if(rnd()<0.04)sgs.add(new EnergyStoneOreGen(this));
		if(rnd()<0.03)sgs.add(new LavaGen(this));
		if(rnd()<0.01)sgs.add(new BlueCrystalOreGen(this));
		if(rnd()<0.007)sgs.add(new GoldOreGen(this));
		if(rnd()<0.003)sgs.add(new DiamondOreGen(this));
		return b;
	}
}

abstract class GroundGen implements Serializable{
	private static final long serialVersionUID=1844677L;
	//生成地表
	
	int width;
	abstract GroundGen change();
	void gen(Block[]bs,int l,int r){
		for(int y=r;y<50;++y)bs[y]=_WaterBlock;
	}
}

class DirtGen extends GroundGen{
	private static final long serialVersionUID=1844677L;
	//草地
	
	DirtGen(){
		width=rndi(40,80);
	}
	GroundGen change(){
		if(rnd()<0.5)return new DirtGen();
		if(rnd()<0.8)return new SandGen();
		return new DarkSandGen();
	}
	void gen(Block[]bs,int l,int r){
		for(int y=l;y<r;++y)bs[y]=_DirtBlock;
		super.gen(bs,l,r);
		if(bs[r].rootBlock().getClass()==AirBlock.class)bs[r]=new GrassBlock(rnd()<0.7?0:1);
	}
}

class SandGen extends GroundGen{
	private static final long serialVersionUID=1844677L;
	//沙地
	
	SandGen(){
		width=rndi(30,60);
	}
	GroundGen change(){
		if(rnd()<0.4)return new SandGen();
		if(rnd()<0.8)return new DirtGen();
		return new DarkSandGen();
	}
	void gen(Block[]bs,int l,int r){
		for(int y=l;y<r;++y)bs[y]=_SandBlock;
		super.gen(bs,l,r);
	}
}

class DarkSandGen extends GroundGen{
	private static final long serialVersionUID=1844677L;
	//影沙地
	
	DarkSandGen(){
		width=rndi(70,140);
	}
	GroundGen change(){
		if(rnd()<0.3)return new DarkSandGen();
		if(rnd()<0.5)return new DirtGen();
		return new SandGen();
	}
	void gen(Block[]bs,int l,int r){
		for(int y=l;y<r;++y)bs[y]=_DarkSandBlock;
		super.gen(bs,l,r);
	}
}

abstract class SpecialGen implements Serializable{
	private static final long serialVersionUID=1844677L;
	//特殊生成器，以一定概率启动，生成跨越多列的地形
	
	//此特殊生成是否已结束
	abstract boolean finished();
	
	//每生成一列时调用
	abstract void gen(WorldGenerator g,Block[]bs);
}

abstract class OreGen extends SpecialGen{
private static final long serialVersionUID=1844677L;
	double y,h,yv,width;
	boolean finished(){
		return width<0;
	}
	void upd(){
		y+=yv;
		width-=rnd(0,2);
	}
}

class LavaGen extends SpecialGen{
private static final long serialVersionUID=1844677L;
	double y,yv,ya;
	LavaGen(WorldGenerator g){
		yv=rnd(0,4);
		ya=-rnd(0.07,0.4)*yv;
		y=rnd(0,yv);
	}
	boolean finished(){
		return y<0;
	}
	void gen(WorldGenerator g,Block[] bs){
		int l=g.lava_y,r=l+rf2i(y);
		for(int y=l;y<r;++y)if(bs[y].rootBlock() instanceof StoneBlock)bs[y]=_LavaBlock;
	}
}

class QuartzOreGen extends OreGen{
private static final long serialVersionUID=1844677L;
	QuartzOreGen(WorldGenerator g){
		y=rnd(10,g.stone_y);
		h=rnd(0.5,2);
		yv=rnd(-0.3,0.3);
		width=rnd(2,12);
	}
	void gen(WorldGenerator g,Block[] bs){
		upd();
		yv=max(-0.3,min(yv+rnd(-0.05,0.05),0.3));
		int l=max(g.lava_y,rf2i(y-h)),r=min(g.stone_y,rf2i(y+h));
		for(int y=l;y<r;++y)if(bs[y].rootBlock().getClass()==StoneBlock.class)bs[y]=_QuartzOreBlock;
	}
}
class CaveGen extends OreGen{
	private static final long serialVersionUID=1844677L;
	CaveGen(WorldGenerator g){
		y=rnd(10,g.stone_y-5);
		h=rnd(1,2);
		yv=rnd(-0.2,0.2);
		width=rnd(15,70);
	}
	void gen(WorldGenerator g,Block[] bs){
		upd();
		yv=max(-1,min(yv+rnd(-0.1,0.1),1));
		h=max(1,min(3.5,h+rnd(-0.2,0.2)));
		y=max(g.lava_y,min(g.stone_y-5,y));
		int l=max(g.lava_y,rf2i(y-h)),r=min(g.stone_y,rf2i(y+h));
		for(int y=l;y<r;++y)if(bs[y].rootBlock() instanceof StoneBlock)bs[y]=_AirBlock;
	}
}

class IronOreGen extends OreGen{
private static final long serialVersionUID=1844677L;
	IronOreGen(WorldGenerator g){
		y=rnd(10,g.stone_y);
		h=rnd(0.5,1.5);
		yv=rnd(-0.5,0.5);
		width=h*rnd(2,6);
		while(rnd()<0.3){
			width*=1.5;
			h*=1.5;
		}
	}
	void gen(WorldGenerator g,Block[] bs){
		upd();
		yv=max(-0.7,min(yv+rnd(-0.1,0.1),0.7));
		int l=max(g.lava_y,rf2i(y-h)),r=min(g.stone_y,rf2i(y+h));
		for(int y=l;y<r;++y)if(bs[y].rootBlock().getClass()==StoneBlock.class&&rnd()<0.7){
			int tp=0;
			if(rnd()<0.3)++tp;
			if(rnd()<0.3)++tp;
			if(rnd()<0.1)++tp;
			bs[y]=new IronOreBlock(tp);
		}
	}
}

class CoalOreGen extends OreGen{
private static final long serialVersionUID=1844677L;
	CoalOreGen(WorldGenerator g){
		y=rnd(g.stone_y-15,g.stone_y);
		h=rnd(0.3,1.5);
		yv=g.yv+rnd(-0.1,0.1);
		width=h*rnd(3,8);
	}
	void gen(WorldGenerator g,Block[] bs){
		upd();
		yv=g.yv+rnd(-0.1,0.1);
		int l=max(g.lava_y,rf2i(y-h)),r=min(g.stone_y,rf2i(y+h));
		for(int y=l;y<r;++y)if(bs[y].rootBlock().getClass()==StoneBlock.class&&rnd()<0.7)bs[y]=_CoalOreBlock;
	}
}

class GravelGen extends OreGen{
	private static final long serialVersionUID=1844677L;
	GravelGen(WorldGenerator g){
		y=rnd(g.stone_y-15,g.stone_y);
		h=rnd(1,2.5);
		yv=g.yv+rnd(-0.1,0.1);
		width=h*rnd(3,8);
	}
	void gen(WorldGenerator g,Block[] bs){
		upd();
		yv=g.yv+rnd(-0.1,0.1);
		int l=max(g.lava_y,rf2i(y-h)),r=min(g.stone_y,rf2i(y+h));
		for(int y=l;y<r;++y)if(bs[y].rootBlock().getClass()==StoneBlock.class&&rnd()<0.9)bs[y]=_GravelBlock;
	}
}

class EnergyStoneOreGen extends OreGen{
private static final long serialVersionUID=1844677L;
	EnergyStoneOreGen(WorldGenerator g){
		y=rnd(g.lava_y,g.lava_y+20);
		h=rnd(0.3,1.3);
		yv=rnd(-0.2,0.2);
		width=rnd(2,10)*h;
		while(rnd()<0.3){
			width*=1.5;
			h*=1.5;
		}
	}
	void gen(WorldGenerator g,Block[] bs){
		upd();
		int l=max(g.lava_y,rf2i(y-h)),r=min(g.stone_y,rf2i(y+h));
		for(int y=l;y<r;++y)if(bs[y].rootBlock().getClass()==StoneBlock.class&&rnd()<0.7)bs[y]=new EnergyStoneOreBlock((rnd()<0.3?1:0)+(rnd()<0.3?1:0));
	}
}

class BlueCrystalOreGen extends OreGen{
	private static final long serialVersionUID=1844677L;
	BlueCrystalOreGen(WorldGenerator g){
		y=rnd(g.lava_y,g.stone_y);
		h=rnd(1,2.5);
		yv=rnd(-0.5,0.5);
		width=rnd(1,2)*h;
	}
	void gen(WorldGenerator g,Block[] bs){
		upd();
		int l=max(g.lava_y,rf2i(y-h)),r=min(g.stone_y,rf2i(y+h));
		for(int y=l;y<r;++y)if(bs[y].rootBlock().getClass()==StoneBlock.class&&rnd()<0.3)bs[y]=new BlueCrystalOreBlock();
	}
}

class GoldOreGen extends OreGen{
private static final long serialVersionUID=1844677L;
	GoldOreGen(WorldGenerator g){
		y=rnd(g.lava_y,g.lava_y+25);
		h=rnd(0.1,0.7);
		yv=rnd(-0.2,0.2);
		width=rnd(1,6);
	}
	void gen(WorldGenerator g,Block[] bs){
		upd();
		int l=max(g.lava_y,rf2i(y-h)),r=min(g.stone_y,rf2i(y+h));
		for(int y=l;y<r;++y)if(bs[y].rootBlock().getClass()==StoneBlock.class&&rnd()<0.3)bs[y]=new GoldOreBlock();
	}
}

class DiamondOreGen extends OreGen{
private static final long serialVersionUID=1844677L;
	DiamondOreGen(WorldGenerator g){
		y=rnd(g.lava_y,g.lava_y+15);
		h=rnd(0.5,1.5);
		yv=rnd(-0.2,0.2);
		width=rnd(1,7);
	}
	void gen(WorldGenerator g,Block[] bs){
		upd();
		int l=max(g.lava_y,rf2i(y-h)),r=min(g.stone_y,rf2i(y+h));
		for(int y=l;y<r;++y)if(bs[y].rootBlock().getClass()==StoneBlock.class&&rnd()<0.1)bs[y]=new DiamondOreBlock();
	}
}
