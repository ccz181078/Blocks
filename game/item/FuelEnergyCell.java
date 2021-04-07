package game.item;

import util.BmpRes;
import static java.lang.Math.*;
import static util.MathUtil.*;
import game.world.World;

public class FuelEnergyCell extends EnergyCell{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/FuelEnergyCell");
	public BmpRes getBmp(){return bmp;}
	public int maxEnergy(){return 120*2*4*5*4;}
	// carbon_powder_fuelval * heating_time_per_fuelval * furnace_slot_num * energy_gain_per_frame_per_slot * carbon_powder_per_cell
	@Override
	public int resCap(){return 0;}
	@Override
	public void gainEnergy(int v){}
	@Override
	public int getEnergy(){
		int c=0,v=super.getEnergy();
		if(first_used_time!=-1){
			c=(int)min(v,World.cur.time-first_used_time);
			if(c==v)first_used_time=-1;
			else first_used_time=World.cur.time;
		}
		loseEnergy(c);
		return super.getEnergy();
	}
	@Override
	public void loseEnergy(int v){
		if(v>0&&first_used_time==-1){
			first_used_time=World.cur.time;
		}
		super.loseEnergy(v);
	}
	long first_used_time=-1;
	public static FuelEnergyCell full(){
		FuelEnergyCell h=new FuelEnergyCell();
		h.setFull();
		h.first_used_time=-1;
		return h;
	}
};