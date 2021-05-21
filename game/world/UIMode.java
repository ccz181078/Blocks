package game.world;

import static java.lang.Math.*;
import static util.MathUtil.*;
import static game.world.World.*;
import game.block.*;
import java.util.*;
import game.entity.*;

public class UIMode extends GameMode{
	private static double[] prob_of_energy={
		1,0,0,0,0,
	};
	public boolean forceOnline(){return false;}
	
	int getWorldWidth(){return 128;}
	
	double[] getWeatherProb(){
		return prob_of_energy;
	}
	
	int resourceRate(){
		return 1;
	}
	
	void newWorld(World world){
		
	}
	void update(Chunk chunk){}
	void genEnt(Chunk chunk){
	}
	
	public void onPlayerDead(Player player){
	}
	public void onPlayerRespawn(Player player){
	}
}
