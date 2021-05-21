package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import graphics.Canvas;
import game.world.World;
import game.block.Block;
import util.BmpRes;
import util.MathUtil;

public class UIEnt extends Entity{
	public double width(){return 1;}
	public double height(){return 1;}
	String text(){return "[UIEnt]";}
	public double mass(){return 1;}
	public boolean chkBlock(){return false;}
	public boolean chkRigidBody(){return false;}
	public boolean chkAgent(){return false;}
	public boolean chkEnt(){return false;}
	
	private static final BmpRes bmp = new BmpRes("UI/empty");
	public BmpRes getBmp(){return bmp;}
	public UIEnt(){
		hp=1./0.;
	}
	@Override
	public boolean harmless(){return true;}
	
	public double hardness(){return game.entity.NormalAttacker.POWDER;}
	@Override
	public void update(){
		shadowed=false;
	}
	
	public void onClick(){}
	
	public double gA(){return 0;}
	
	public void draw(Canvas cv){//绘制
		float w=(float)width(),h=(float)height();
		super.draw(cv);
		String _text=text();
		cv.save();{
			cv.scale(1,-1);
			cv.drawText(_text,0,0,h,0);
		}cv.restore();
		
	}
}
class WorldEntryEnt extends UIEnt{
	public double width(){return 1;}
	public double height(){return 0.4;}
	String text(){return world_name;}
	String world_name;
	WorldEntryEnt(String name){
		world_name=name;
	}
	public void onClick(){
		try{
			World.save();
			String path=debug.Log.MAIN_DIR+world_name+debug.Log.FILE_PATH_SEPARATOR;
			World.restore(path);
			World.cur.save_path=debug.Log.MAIN_DIR+"__tmp__"+debug.Log.FILE_PATH_SEPARATOR;
			World.cur.restart();
		}catch(Exception e){
			debug.Log.i(e);
		}
	}
}