package game.item;

import util.BmpRes;
import game.entity.*;
import game.block.*;
import game.world.*;
import static java.lang.Math.*;
import static util.MathUtil.*;

public class LevelEnd extends Item{
	static BmpRes bmp=new BmpRes("Entity/LevelEnd");
	public BmpRes getBmp(){return bmp;}
	public Item clickAt(double x,double y,Agent a){
		new game.entity.LevelEnd().initPos(x,y,0,0,null).add();
		return null;
	}
	
	public boolean autoUse(final Human h,final Agent a){return false;}
	@Override
	public boolean isCreative(){return true;}
}
