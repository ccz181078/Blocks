package game.item;
import util.*;
import static util.MathUtil.*;
import game.world.World;
import game.entity.*;
import game.block.PaperBlock;

public class Paper extends Item{
	private static final long serialVersionUID=1844677L;
	private static BmpRes bmp=new BmpRes("Item/Paper");
	public BmpRes getBmp(){return bmp;}
	public int fuelVal(){return 50;}

	public Item clickAt(double x,double y,Agent a){
		int px=f2i(x),py=f2i(y);
		if(World.cur.placeable(px,py)){
			PaperBlock b=new PaperBlock();
			World.cur.place(px,py,b);
			if(a instanceof Player){
				((Player)a).sendText((s)->{b.text=s;});
			}
			return null;
		}
		return super.clickAt(x,y,a);
	}
}
