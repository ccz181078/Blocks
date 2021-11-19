package game.item;

import util.BmpRes;
import game.entity.*;

public class TextGuidedBullet extends GuidedBullet{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/TextGuidedBullet");
	public BmpRes getBmp(){return bmp;}
	String name="呐";
	public String getName(){return name;}
	@Override
	protected Entity toEnt(){return new game.entity.TextGuidedBullet(this);}
	public void onKill(double x,double y){}
	public boolean chkBlock(){return false;}
	public void onUse(Human a){
		if(a instanceof Player){
			((Player)a).sendText((s)->{
				if(s.length()>0)name=s;
			});
		}
		a.items.getSelected().insert(this);
	}
	@Override
	public boolean isCreative(){return true;}
};
