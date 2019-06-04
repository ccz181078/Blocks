package game.item;

public class SpecialItem<T extends Item> extends SingleItem{
	private static final long serialVersionUID=1844677L;
	//特殊的物品容器，只能放入指定类的子类的物品
	//在编译器和运行期都会进行类型检查
	Class<T> type;
	public SpecialItem(Class<T> _type){
		type=_type;
	}
	public void insert(SingleItem si){
		Item it=si.get();
		if(it!=null)
			if(type.isAssignableFrom(it.getClass()))
				super.insert(si);
	}
	public T get(){
		return type.cast(super.get());
	}
	public T popItem(){
		return type.cast(super.popItem());
	}
}
