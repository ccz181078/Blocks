package game.item;

import util.BmpRes;
import game.entity.Agent;
import game.world.World;

public class IronStick extends Item{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/IronStick");
	public BmpRes getBmp(){return bmp;}
	static long last_click_t=-100;
	static int click_t=0;
	public Item clickAt(double x,double y,Agent a){
		if(a==World._.getRootPlayer()){
			if(World._.time<last_click_t+10){
				++click_t;
				if(click_t>5){
					click_t=0;
					last_click_t=-100;
					new debug.script.ScriptEditor().show();
				}
			}else click_t=0;
			last_click_t=World._.time;
		}
		//game.world.World.showText(debug.ObjectInfo.obj2str(game.world.World._.get(x,y)));
		return super.clickAt(x,y,a);
	}
	
}
