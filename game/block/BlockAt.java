package game.block;

import game.world.World;

public class BlockAt implements java.io.Serializable{
	private static final long serialVersionUID=1844677L;
	//描述一个存在于世界中某个整数坐标的方块
	
	public int x,y;
	public Block block;
	public BlockAt(int _x,int _y){
		this(_x,_y,World.cur.get(_x,_y));
	}
	public BlockAt(int _x,int _y,Block _block){
		x=_x;
		y=_y;
		block=_block;
	}
	
	//检查这个方块是否仍存在于原位
	public boolean exist(){
		return World.cur.get(x,y).rootBlock()==block.rootBlock();
	}
}
