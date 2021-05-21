package game.entity;
import graphics.Canvas;
import static java.lang.Math.*;
import static util.MathUtil.*;
import java.util.ArrayList;
public class Line extends NonInteractiveEnt{
	private static final long serialVersionUID=1844677L;
	
	float x0,y0;
	int color;
	static ArrayList<Line> ls=new ArrayList<>();
	static boolean is_test=false;
	private Line(Entity ent,int col){
		x=ent.x;
		y=ent.y;
		x0=(float)ent.xv;
		y0=(float)ent.yv;
		color=col;
		hp=60;//rnd_exp(1000);
	}
	private Line(double x1,double y1,double xd,double yd,int col){
		x=x1;
		y=y1;
		x0=-(float)xd;
		y0=-(float)yd;
		color=col;
		hp=5;
	}
	//@Override public void add(){}
	public static void gen(double x,double y,double d){
		new Line(x,y-d,0,2*d,0xffff0000).add();
		new Line(x-d,y,2*d,0,0xffff0000).add();
	}
	public static void gen(double x1,double y1,double x2,double y2){
		new Line(x1,y1,x2-x1,y2-y1,0xffff0000).add();
	}
	public static void gen(Entity ent,int col){
		Line w=new Line(ent,col);
		if(is_test)ls.add(w);
		else w.add();
		if(col!=0xff0000ff)w.hp=1;
	}
	public static void begin(){
		is_test=true;
	}
	public static void end(boolean add){
		if(add)for(Line l:ls)l.add();
		is_test=false;
		ls.clear();
	}
	
	public static void gen(Entity ent){
		gen(ent,0xff0000ff);
	}
	
	public boolean chkBlock(){return false;}
	public boolean chkRigidBody(){return false;}

	@Override
	public void update(){
		super.update();
		hp-=1;
	}
	@Override
	public double gA(){return 0;}
	
	

	@Override
	public void draw(Canvas cv){
		cv.drawLines(new float[]{0,0,-x0,-y0},color);
	}
	
	
}
