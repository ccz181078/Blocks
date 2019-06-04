package game.block;

import util.BmpRes;
import graphics.Canvas;

public class AirBlock extends AirType{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/AirBlock");
	public BmpRes getBmp(){return bmp;}
	public void draw(Canvas cv){}
	public void onDestroy(int x,int y){}
	public void des(int x,int y,int v){}
	public boolean onCheck(int x,int y){return false;}
	public boolean onUpdate(int x,int y){return false;}
};
