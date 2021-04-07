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
interface DefaultEnergyContainer extends EnergyContainer{
	public EnergyContainer getEnergyContainer();
	default public int getEnergy(){
		EnergyContainer e=getEnergyContainer();
		if(e!=null)return e.getEnergy();
		return 0;
	}
	default public void loseEnergy(int x){
		EnergyContainer e=getEnergyContainer();
		if(e!=null)e.loseEnergy(x);
	}
	default public void gainEnergy(int v){
		EnergyContainer e=getEnergyContainer();
		if(e!=null)e.gainEnergy(v);
	}
	default public int resCap(){
		EnergyContainer e=getEnergyContainer();
		if(e!=null)return e.resCap();
		return 0;
	}
}
