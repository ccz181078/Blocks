package game.entity;

public interface NormalAttacker{
	public double hardness();
	static final double 
	POWDER=-1,
	LEAF=0,
	DIRT=0.8,
	AGENT=1,
	PAPER=1.5,
	WOODEN=2,
	CACTUS=2.5,
	GOLD=2.7,
	STONE=3.2,
	IRON=4,
	QUARTZ=6,
	AP=7,
	DIAMOND=8,
	HD=12;
	public static String getHardnessName(double x){
		if(x==POWDER)return "POWDER";
		if(x==LEAF)return "LEAF";
		if(x==DIRT)return "DIRT";
		if(x==AGENT)return "AGENT";
		if(x==WOODEN)return "WOODEN";
		if(x==CACTUS)return "CACTUS";
		if(x==GOLD)return "GOLD";
		if(x==STONE)return "STONE";
		if(x==IRON)return "IRON";
		if(x==QUARTZ)return "QUARTZ";
		if(x==AP)return "AP";
		if(x==DIAMOND)return "DIAMOND";
		if(x==HD)return "HD";
		return "?";
	}
	public default double getWeight(NormalAttacker target){
		double x=(hardness()-target.hardness());
		return 1/(1+Math.exp(-x));
	}
}
