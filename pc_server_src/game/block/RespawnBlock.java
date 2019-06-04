package game.block;

import util.BmpRes;
import game.entity.Entity;
import game.entity.Player;

public class RespawnBlock extends StoneType{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp[]=BmpRes.load("Block/RespawnBlock_",2);
	public BmpRes getBmp(){return bmp[on?1:0];}
	int maxDamage(){return 320;}
	private boolean on=false;
	public void onPlace(int x,int y){on=false;}
	public void onDestroy(int x,int y){
		on=false;
		super.onDestroy(x,y);
	}
	public void touchEnt(int x,int y,Entity ent){
		if(!on && ent instanceof Player){
			on=true;
			((Player)ent).setRespawnPoint();
		}
		super.touchEnt(x,y,ent);
	}
	
}
