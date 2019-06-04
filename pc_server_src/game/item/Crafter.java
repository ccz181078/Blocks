package game.item;

public interface Crafter extends CraftHelper{
	//能执行一次完整的Craft，也可以给Craft提供物品然后转交给某个CraftHelper
	public ItemContainer getItems();
	public int getCraftType();
}
