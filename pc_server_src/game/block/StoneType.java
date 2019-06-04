package game.block;

import game.item.Item;

abstract public class StoneType extends Block{
	private static final long serialVersionUID=1844677L;
	public void onPress(int x,int y,Item item){
		des(x,y,item.pickaxVal());
		item.onDesBlock(this);
	}
};
