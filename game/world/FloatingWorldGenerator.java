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

class FloatingWorldGenerator extends WorldGenerator{
	private static final long serialVersionUID=1844677L;
	
	ArrayList<IslandGen> islands=new ArrayList<>();
	
	FloatingWorldGenerator(){
		super();
		lava_y=8;
		islands.add(new IslandGen(new StaticBlock(new DirtBlock())));
	}
	
	@Override
	Block[] nxt(){
		Block[] b=new Block[World_Height];
		int y=0;
		for(;y<bedrock_y;++y)b[y]=_BedRockBlock;
		for(;y<lava_y;++y)b[y]=_LavaBlock;
		for(;y<World_Height;++y)b[y]=_AirBlock;
		
		--nxt_plant;
		if(nxt_plant<0){
			if(rnd()<0.9)nxt_plant=rndi(6,10);
			else nxt_plant=rndi(6,60);
		}
		
		if(rnd()<0.3){
			bedrock_y=max(1,min(bedrock_y+rndi(-1,1),lava_y-2));
		}
		
		for(int i=0;i<islands.size();++i){
			IslandGen ig=islands.get(i);
			if(ig.finished()){
				islands.set(i,islands.get(islands.size()-1));
				islands.remove(islands.size()-1);
				--i;
			}else{
				ig.gen(b);
			}
		}
		if(true){
			if(rnd()<0.15)islands.add(new IslandGen(_DirtBlock));
			if(rnd()<0.1)islands.add(new IslandGen(_SandBlock));
			if(rnd()<0.05)islands.add(new IslandGen(_GravelBlock));
			if(rnd()<0.05)islands.add(new IslandGen(_DarkSandBlock));
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
		
		if(rnd()<0.1){
			int py=rndi(0,100);
			if(b[py].rootBlock().getClass()==StoneBlock.class)b[py]=new BloodStoneBlock();
		}
		
		for(int i=World.cur.getMode().resourceRate();i>0;--i){
			if(rnd()<0.07)sgs.add(new QuartzOreGen(this));
			if(rnd()<0.04)sgs.add(new IronOreGen(this));
			if(rnd()<0.07)sgs.add(new CoalOreGen(this));
			if(rnd()<0.04)sgs.add(new EnergyStoneOreGen(this));
			if(rnd()<0.03)sgs.add(new LavaGen(this));
			if(rnd()<0.01)sgs.add(new BlueCrystalOreGen(this));
			if(rnd()<0.007)sgs.add(new GoldOreGen(this));
			if(rnd()<0.003)sgs.add(new DiamondOreGen(this));			
		}
		for(y=0;y<World_Height;++y)if(b[y]==null)debug.Log.i(y+":null:"+b[y]);
		return b;
	}
	class IslandGen implements Serializable{
		private static final long serialVersionUID=1844677L;
		Block default_block;
		int y0[],y1[],y2[],x;
		IslandGen(Block default_block){
			x=0;
			this.default_block=default_block;
			int y=rndi(12,90);
			int width=rndi(8,20);
			int stone_height=rndi(2,4);
			int top_height=rndi(2,3);
			y0=new int[width];
			y1=new int[width];
			y2=new int[width];
			for(int i=0;i<width;++i){
				double c=Math.sqrt(min(4,min(i,width-1-i))/4.);
				y0[i]=y-max(1,rf2i(stone_height*c+rnd(-0.3,0.3)));
				y1[i]=y;
				y2[i]=y+max(1,rf2i(top_height*c+rnd(-0.3,0.3)));
				if(rnd()<0.4)y=max(12,min(90,y+rndi(-1,1)));
			}
			for(int t=0;t<2;++t)
			for(int i=1;i<width-1;++i){
				y0[i]=rf2i((y0[i-1]+y0[i+1])/2.);
				y2[i]=rf2i((y2[i-1]+y2[i+1])/2.);
			}
		}
		boolean finished(){
			return x>=y0.length;
		}
		void gen(Block[]bs){
			int y=y0[x],d1=y1[x]-y,d2=y2[x]-y1[x];
			for(int i=0;i<d1;++i){
				bs[y++]=_StoneBlock;
			}
			for(int i=0;i<d2;++i){
				bs[y++]=default_block;
			}
			x+=1;
		}
	}
}


