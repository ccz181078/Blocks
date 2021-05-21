package game.world;

import static java.lang.Math.*;
import static util.MathUtil.*;
import static game.world.World.*;
import game.block.*;
import java.util.*;
import game.entity.*;

public class LevelMode extends GameMode{
	private static double[] prob_of_energy={
		1,0,0,0,0,
	};
	public boolean forceOnline(){return false;}
	
	int getWorldWidth(){return 32;}
	
	double[] getWeatherProb(){
		return prob_of_energy;
	}
	
	int resourceRate(){
		return 1;
	}
	
	void newWorld(World world){}
	void update(Chunk chunk){}
	void genEnt(Chunk chunk){
		//if(!chunk.gen_ent)return;
		chunk.gen_ent=false;
		if(rnd()>0.0002)return;
		if(chunk.agents.size()>16)return;
		//defaultGenEnt(chunk);
	}
	
	public void onPlayerDead(Player player){
		player.dropItems();
		player.respawn_time=150;
		player.locked=true;
	}
	public void onPlayerRespawn(Player player){
		player.dead=false;
		ProtectionZone.setPlayer(player);
	}
	public void touchLevelEnd(LevelEnd e,Player pl){
		super.touchLevelEnd(e,pl);
		if(!pl.creative_mode){
			pl.showText("成功通过关卡");
		}
	}
}
