package game.item;

import static java.lang.Math.*;
import static util.MathUtil.*;
import util.BmpRes;
import game.entity.*;

public class SimpleLauncher extends RPGLauncherDisposable implements ShootableTool,DefaultItemContainer{
	static BmpRes bmp=new BmpRes("Item/SimpleLauncher");
	public BmpRes getBmp(){return bmp;}
	public double mv2(){return EnergyTool.E0*0.5;}
};
