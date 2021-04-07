package game.item;

import util.BmpRes;
import static util.MathUtil.*;
import static java.lang.Math.*;
import game.entity.*;

public abstract class LaunchableItem extends Item{
private static final long serialVersionUID=1844677L;
	@Override
	public void onLaunchAtPos(Agent a,int dir,double x,double y,double slope,double mv2){
		a.throwEntAtPos(toEnt(),dir,x,y,slope,mv2);
	}
	protected abstract Entity toEnt();
	public Entity asEnt(){return toEnt();}
}