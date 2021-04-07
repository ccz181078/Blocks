package game.world;

import static util.MathUtil.*;

public class Weather{
	//描述天的颜色
	
	public static final int 
		_energystone=0,
		_dark=1,
		_fire=2,
		_plant=3,
		_blood=4;
	
	public static int[] color_of_energy={
		0xff00cccc,
		0xff4f004f,
		0xffff7f00,
		0xff00ff00,
		0xffff0000,
	};
	
	public static String[] name_of_energy={
		"EnergyStone",
		"Dark",
		"Fire",
		"Plant",
		"Blood",
	};
	
	
	public static int randomWeather(){
		double p=rnd();
		double[] prob_of_energy=World.cur.getMode().getWeatherProb();
		for(int i=0;i<4;++i){
			p-=prob_of_energy[i];
			if(p<0)return i;
		}
		return 4;
	}
	
}
