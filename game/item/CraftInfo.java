package game.item;

public class CraftInfo implements java.io.Serializable{
private static final long serialVersionUID=1844677L;
	public static int
		_heat=1,
		_complex=2,
		_cut=4,
		_diamond=8,
		_energy=16,
		_block=32,
		_compress=64,
		_pulverize=128;
	public int time,energy,type;
	CraftInfo(int _time,int _energy,int _type){
		time=_time;
		energy=_energy;
		type=_type;
	}
}
