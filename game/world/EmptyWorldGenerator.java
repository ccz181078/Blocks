package game.world;
import static java.lang.Math.*;
import static util.MathUtil.*;
import static game.world.World.World_Height;
import game.block.*;
import java.util.*;
import java.io.*;
import static game.world.WorldGenerator.*;

class EmptyWorldGenerator extends WorldGenerator{
	Block[] nxt(){
		Block[] b=new Block[World_Height];
		for(int y=0;y<World_Height;++y)b[y]=_AirBlock;
		return b;
	}	
}

