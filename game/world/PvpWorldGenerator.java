package game.world;
import static java.lang.Math.*;
import static util.MathUtil.*;
import static game.world.World.World_Height;
import game.block.*;
import java.util.*;
import java.io.*;
import static game.world.WorldGenerator.*;

class PvpWorldGenerator extends WorldGenerator{
	PvpWorldGenerator(){
		super();
		bedrock_y=0;
		lava_y=2;
		stone_y=4;
		dirt_d=5;
		nxt_plant=rndi(3,10);
	}
	Block[] nxt(){
		Block[] b=new Block[World_Height];
		int y=0;
		Block _StoneBlock=new StaticBlock(new SemilavaBlock());
		for(;y<bedrock_y;++y)b[y]=_BedRockBlock;
		for(;y<lava_y;++y)b[y]=_StoneBlock;
		for(;y<stone_y;++y)b[y]=rnd()*(stone_y-lava_y)>(y-lava_y+1) ? _StoneBlock : _StoneBlock;
		for(y=stone_y;y<World_Height;++y)b[y]=_AirBlock;
		ground.gen(b,this);
		if(nxt_plant<0){
			if(rnd()<0.9)nxt_plant=rndi(6,10);
			else nxt_plant=rndi(6,60);
		}
		--ground.width;
		if(ground.width<=0){
			ground=ground.change();
		}
		
		if(rnd()<0.3){
			lava_y=max(bedrock_y+2,min(lava_y+rndi(-1,1),stone_y-4));
		}
		if(rnd()<0.2){
			dirt_d=max(5,min(dirt_d+rndi(-1,1),7));
		}else if(abs(yv)>0.2||rnd()<0.3){
			stone_y+=rf2i(yv);
		}
		double mv=0.7;
		double mya=0.04;
		if(rnd()<0.03)mya=0.4;
		yv=max(-mv,min(yv+rnd(-mya,mya),mv));
		if(stone_y>20){
			stone_y=20;
			if(yv>0)yv=0;
		}
		if(stone_y<2){
			stone_y=2;
			if(yv<0)yv=0;
		}

		return b;
	}
	int getWaterY(){return 0;}
}
