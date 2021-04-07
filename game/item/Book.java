package game.item;
import game.entity.*;
import static util.MathUtil.*;
import util.BmpRes;

public class Book extends Item{
	private static final long serialVersionUID=1844677L;
	private static BmpRes bmp=new BmpRes("Item/Book");
	public BmpRes getBmp(){return bmp;}
	@Override
	public void onAttack(Entity e,Source src){
		if(e instanceof Human){
			Human hu=(Human)e;
			if(hu.name==null)hu.name="N"+rndi(0,9999);
		}
	}
}
