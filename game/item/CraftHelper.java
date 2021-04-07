package game.item;

public interface CraftHelper extends ItemReceiver,EnergyContainer{
	//能执行Craft，接受Craft完毕/中断得到的物品，但不一定能提供启动Craft的物品
	public boolean startCraft(Craft craft);
	public float getCraftProcess();
}
