package game.item;

public interface ItemContainer{
	//表示一个物品容器，支持插入物品和转为数组
	
	public void insert(SingleItem it);
	public SingleItem[] toArray();
}
