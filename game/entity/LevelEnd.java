package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import graphics.Canvas;
import game.world.World;
import game.block.Block;
import util.BmpRes;
import util.MathUtil;

public class LevelEnd extends Entity{
	public double width(){return 0.45;}
	public double height(){return 0.45;}
	public double mass(){return 0.5;}
	public boolean chkBlock(){return false;}
	public boolean chkRigidBody(){return false;}
	public boolean chkAgent(){return false;}
	public boolean chkEnt(){return false;}
	@Override
	public double RPG_ExplodeProb(){return 0;}
	
	private static final BmpRes bmp = new BmpRes("Entity/LevelEnd");
	public BmpRes getBmp(){return bmp;}
	public LevelEnd(){
		hp=1./0.;
	}
	@Override
	public boolean harmless(){return true;}
	
	public double hardness(){return game.entity.NormalAttacker.POWDER;}
	@Override
	public void update(){
		shadowed=false;
		for(Player pl:World.cur.getPlayers()){
			if(hitTest(pl)){
				World.cur.getMode().touchLevelEnd(this,pl);
			}
		}
	}
	
	public double gA(){return 0;}
}
