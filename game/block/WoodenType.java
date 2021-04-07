package game.block;

import game.item.Item;

public abstract class WoodenType extends Block{
	private static final long serialVersionUID=1844677L;
	public int fuelVal(){return maxDamage();}
	public void onPress(int x,int y,Item item){
		des(x,y,item.axVal(),item);
		item.onDesBlock(this);
	}
};
