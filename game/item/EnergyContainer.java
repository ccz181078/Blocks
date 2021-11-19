package game.item;

public interface EnergyContainer{
	public int getEnergy();
	default public boolean hasEnergy(int x){
		return getEnergy()>=x;
	}
	public void loseEnergy(int v);
	public int resCap();
	public void gainEnergy(int v);
}
