package util;

import graphics.Canvas;
import java.util.HashMap;
import graphics.Bitmap;
import game.item.*;

public class BmpRes{
	private static final long serialVersionUID=1844677L;
	//抽象表示一张贴图（实际仅储存了贴图的路径）
	
	private String path;
	
	public static HashMap<String,Integer> path2int;
	public static String[] int2path;
	public static Bitmap[] int2bmp;
	public static int idp;
	public static HashMap<Class,BmpRes> tipmap=new HashMap<>();
	static{
		tipmap.put(AirshipFlank.class,new BmpRes("Armor/AirshipFlankTip"));
		tipmap.put(Pipeline_5.class,new BmpRes("Armor/PipelineTip"));
		tipmap.put(Bow.class,new BmpRes("Item/BowTip"));
		tipmap.put(StoneBall.class,new BmpRes("Item/BallTip"));
		tipmap.put(Tool.class,new BmpRes("Item/ToolTip"));
		tipmap.put(Armor.class,new BmpRes("Item/ArmorTip"));
		tipmap.put(Shoes.class,new BmpRes("Item/ShoesTip"));
		tipmap.put(Arrow.class,new BmpRes("Item/ArrowTip"));
		tipmap.put(Bullet.class,new BmpRes("Item/BulletTip"));
		tipmap.put(Warhead.class,new BmpRes("Item/WarheadTip"));
		tipmap.put(Disabled.class,new BmpRes("Item/DisabledTip"));
		tipmap.put(StoneBall.class,new BmpRes("Item/BallTip"));
		tipmap.put(EnergyCell.class,new BmpRes("Item/EnergyCellTip"));
		tipmap.put(EnergyBallLauncher.class,new BmpRes("Item/LauncherTip"));
		tipmap.put(game.block.Block.class,new BmpRes("Item/BlockTip"));
	}
	static boolean inited=false;
	public static void init(){
		if(inited)return;
		inited=true;
		idp=3;
		path2int=new HashMap<>();
		int2path=new String[32768];
		int2bmp=new Bitmap[32768];
		int2bmp[0]=AssetLoader.loadBmp("UI/cancel");
		int2bmp[1]=AssetLoader.loadBmp("UI/selected_item_frame");
		int2bmp[2]=AssetLoader.loadBmp("UI/item_frame");
	}
	
	//得到一张贴图，路径为_path+".png"
	public BmpRes(String _path){
		path=_path;
	}
	
	//获取路径
	public String getPath(){
		return path;
	}
	
	//获取编号，每次运行时编号可能不同
	public int hashCode(){
		if(!path2int.containsKey(path)){
			path2int.put(path,idp);
			int2path[idp]=path;
			++idp;
		}
		return path2int.get(path);
	}
	
	//得到一组贴图，路径形如_path+x+".png"，x=0..n-1，路径不包含.png
	public static BmpRes[] load(String _path,int n){
		BmpRes s[]=new BmpRes[n];
		for(int i=0;i<n;++i)s[i]=new BmpRes(_path+i);
		return s;
	}
	
	//x轴向右，y轴向上，主要用于游戏场景
	//绘制在(x-xd,y-yd)和(x+xd,y+yd)间的矩形内
	public void draw(Canvas cv,float x,float y,float xd,float yd){
		cv.drawBitmapRevY(this,x,y,xd,yd);
	}
	
	//x轴向右，y轴向上，主要用于游戏场景
	//绘制在给定两点间的平行于坐标轴的矩形内
	public void drawR(Canvas cv,float x1,float y1,float x2,float y2){
		draw(cv,(x1+x2)/2,(y1+y2)/2,(x2-x1)/2,(y2-y1)/2);
	}
	
	
	//x轴向右，y轴向下，主要用于用户界面
	//绘制给定两点间的平行于坐标轴的矩形内
	public void drawInRect(Canvas cv,float x1,float y1,float x2,float y2){
		cv.drawBitmap(this,x1,y1,x2,y2);
	}
}
