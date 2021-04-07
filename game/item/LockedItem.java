package game.item;
import util.BmpRes;

public class LockedItem extends SingleItem{
	SingleItem w;
	LockedItem(SingleItem w){super();this.w=w;}
	BmpRes getTipBmp(){
		return Disabled.bmp;
	}
	SingleItem unlock(){return w;}
	public int maxAmount(){return 0;}
	public void insert(SingleItem si){}
}