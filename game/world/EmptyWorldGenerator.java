package game.world;
import static java.lang.Math.*;
import static util.MathUtil.*;
import game.block.*;
import java.util.*;
import java.io.*;
import static game.world.WorldGenerator.*;

class EmptyWorldGenerator extends WorldGenerator{
	Block[] nxt(){
		Block[] b=new Block[World.cur.World_Height];
		for(int y=0;y<World.cur.World_Height;++y)b[y]=_AirBlock;
		return b;
	}	
}

