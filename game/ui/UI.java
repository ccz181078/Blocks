package game.ui;
import graphics.Canvas;
import static java.lang.Math.*;
import static util.MathUtil.*;
import util.BmpRes;
import game.entity.Player;
import game.item.*;

public abstract class UI{
	private static final long serialVersionUID=1844677L;
	//描述用户界面中的一个控件或容器
	
	public static BmpRes item_frame=new BmpRes("UI/item_frame");
	public static BmpRes selected_item_frame=new BmpRes("UI/selected_item_frame");
	static BmpRes item_frame_left=new BmpRes("UI/item_frame_left");
	static BmpRes selected_item_frame_left=new BmpRes("UI/selected_item_frame_left");
	public static BmpRes
	left_btn=new BmpRes("UI/left"),
	right_btn=new BmpRes("UI/right"),
	empty_btn=new BmpRes("UI/empty");
	public static Player pl;
	public static float H_div_W=1;
	protected float x,y;
	private game.block.BlockAt __block;
	private CraftHelper __craft_helper;
	private int bg_color=0;
	
	//事件：得到焦点，此事件对静态控件无效
	public void open(){}
	
	//事件：每帧更新
	public void update(){}
	
	
	public final boolean touch(float tx,float ty,int tp){
		if(!exist())return false;
		return onTouch(tx-x,ty-y,tp);
	}
	public final void draw(Canvas cv){
		if(!exist())return;
		cv.save();
		cv.translate(x,y);
		if(bg_color!=0)cv.drawRect(0,0,4,4,bg_color);
		if(__block!=null)cv.drawBitmap(__block.block.getBmp(),0,0,4,4);
		onDraw(cv);
		cv.restore();
	}
	
	//设置4x4的背景色
	public void setBgColor(int col){bg_color=col;}
	
	public final int getCraftType(){return __block==null?0:__block.block.getCraftType();}
	
	//事件：绘制
	abstract protected void onDraw(Canvas cv);
	
	//事件：按下，坐标相对于this的左上角
	protected boolean onTouch(float tx,float ty,int tp){return false;}
	
	//存在性检查，如果不存在将不会被绘制，不能处理触摸事件，不存在的对话框会被关闭
	public boolean exist(){
		if(__block!=null)return pl.check(__block);
		return true;
	}
	//事件：失去焦点，此事件对静态控件无效
	public void close(){}
	
	//设置4x4的背景方块
	//并使用默认的依赖方块的存在性检验策略：方块仍存在，且离主角不远
	public final UI setBlock(game.block.BlockAt ba){__block=ba;return this;}
	public final game.block.BlockAt getBlock(){return __block;}
	
	public final UI setCraftHelper(CraftHelper ch){
		__craft_helper=ch;
		return this;
	}
	
	public final CraftHelper getCraftHelper(){return __craft_helper;}
	
	//绘制进度条，画布，前景色，背景色，0和1间的进度，矩形的左、上、右、下
	public static void drawProgressBar(Canvas cv,int color,int bg_color,float v,float left,float top,float right,float bottom){
		v=left+(right-left)*max(0,min(v,1));
		cv.drawRect(left,top,v,bottom,color);
		cv.drawRect(v,top,right,bottom,bg_color);
	}
}

