package game.entity;
import graphics.Canvas;
import static util.MathUtil.*;
public class Text extends NonInteractiveEnt{
	private static final long serialVersionUID=1844677L;
	
	Agent src;
	String text;
	double x0,y0;
	
	private Text(double _x,double _y,String _text,Agent _src){
		x=_x;y=_y;text=_text;
		src=_src;
		if(src!=null){
			x0=x-src.x;
			y0=y-src.y;
		}
		xv=0;
		yv=0;
		hp=40+rnd_gaussion()*4;
	}
	
	public static void gen(double x,double y,String text,Agent src){
		new Text(x,y,text,src).add();
	}
	
	public boolean chkBlock(){return false;}
	public boolean chkRigidBody(){return false;}

	@Override
	public void update(){
		super.update();
		//System.out.println("U:"+game.world.World.cur.time+"::"+this+":"+hp+":"+x+","+y+":"+text);
		//System.out.println(this+":"+hp+":"+x+","+y+":"+text);
		hp-=1;
		y0+=0.01;
		if(src!=null&&src.isRemoved())src=null;
		if(src!=null){
			x=src.x+x0;
			y=src.y+y0;
		}else{
			y+=0.01;
		}
	}

	@Override
	public double gA(){
		return 0;
	}
	
	

	@Override
	public void draw(Canvas cv){
		//System.out.println("D:"+game.world.World.cur.time+"::"+this+":"+hp+":"+x+","+y+":"+text);
		cv.save();
		cv.scale(1,-1);
		cv.drawText(text,0,0,0.3f*game.world.NearbyInfo.BW/12,0);
		cv.restore();
	}
	
	
}
