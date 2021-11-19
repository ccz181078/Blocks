package game.world;
import static java.lang.Math.*;
import static util.MathUtil.*;
import game.block.*;
import java.util.*;
import java.io.*;
import static game.world.WorldGenerator.*;

class FlatWorldGenerator extends WorldGenerator{
	Block[] nxt(){
		Block[] b=new Block[World.cur.World_Height];
		int y=0;
		for(;y<bedrock_y;++y)b[y]=_BedRockBlock;
		for(;y<lava_y;++y)b[y]=_LavaBlock;
		for(;y<stone_y;++y)b[y]=_StoneBlock;
		for(y=stone_y;y<World.cur.World_Height;++y)b[y]=_AirBlock;
		ground.gen(b,this);
		--nxt_plant;
		if(nxt_plant<0){
			if(rnd()<0.9)nxt_plant=rndi(6,10);
			else nxt_plant=rndi(6,60);
		}
		--ground.width;
		if(ground.width<=0){
			ground=ground.change();
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
		return b;
	}	
}

