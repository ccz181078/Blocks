package game.item;

import util.BmpRes;
import game.block.Block;
import static util.MathUtil.*;
import static java.lang.Math.*;
import game.entity.*;
import game.world.World;

public class EnergyAntiMaterialGun extends EnergyGun{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/EnergyAntiMaterialGun");
	public BmpRes getBmp(){return bmp;}
	public int maxDamage(){return 100;}
	public int energyCost(){return 25;}
	int extra_energy=0;
	public double mv2(){return E0*(25+extra_energy);}
	EnergyAntiMaterialGun(){
		bullet=new NonOverlapSpecialItem<Bullet>(Bullet.class,2);
	}
	int getCd(){return 45;}
	public boolean onLongPress(Agent a,double tx,double ty){
		if(hasEnergy(5+2)&&extra_energy<=1000){
			++extra_energy;
			loseEnergy(2);
		}
		return true;
	}
	protected void shoot(double s,Agent w){
		super.shoot(s,w);
		damage+=f2i(extra_energy/20.);
		extra_energy=0;
	}
	public void drawInfo(graphics.Canvas cv){
		super.drawInfo(cv);
		double c=sqrt(extra_energy/1000.);
		game.ui.UI.drawProgressBar(cv,0xff00ffff,0,(float)c,-0.4f,-0.2f,0.4f,-0.13f);
	}
}

