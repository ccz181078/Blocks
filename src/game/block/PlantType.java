package game.block;
import graphics.*;
import static java.lang.Math.*;

public abstract class PlantType extends WoodenType{
	private static final long serialVersionUID=1844677L;
	float light_v,dirt_v;
	public boolean isSolid(){return false;}
	public void onLight(int x,int y,double v){
		light_v+=v;
		if(light_v>5)light_v=5;
	}
	void repair(double d,double l){
		if(damage>0&&dirt_v>=d&&light_v>=l){
			dirt_v-=d/2;
			light_v-=l/2;
			--damage;
		}
	}
	void spread(PlantType w,float k){
		float d=(light_v-w.light_v)*k;
		light_v-=d;
		w.light_v+=d;
		d=(dirt_v-w.dirt_v)*k;
		dirt_v-=d;
		w.dirt_v+=d;
	}
	/*public void draw(Canvas cv){
		//debug output dirt_v and light_v
		super.draw(cv);
		game.ui.UI.drawProgressBar(cv,0xffffffff,0xff7f7f7f,light_v/5,-0.4f,0.1f,0.4f,0.2f);
		game.ui.UI.drawProgressBar(cv,0xffff7f00,0xff7f3f00,dirt_v/5,-0.4f,0.25f,0.4f,0.35f);
	}*/
}
