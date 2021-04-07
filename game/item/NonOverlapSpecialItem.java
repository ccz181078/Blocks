package game.item;

public class NonOverlapSpecialItem<T extends Item> extends SpecialItem<T>{
	private static final long serialVersionUID=1844677L;
	int max_amount=1;
	public NonOverlapSpecialItem(Class<T> _type){
		super(_type);
		max_amount=1;
	}
	public NonOverlapSpecialItem(Class<T> _type,int cnt){
		super(_type);
		max_amount=cnt;
	}
	public int maxAmount(){return Math.max(1,max_amount);}
}