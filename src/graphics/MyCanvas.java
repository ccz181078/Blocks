package graphics;

import android.graphics.*;
import java.util.ArrayList;
import java.io.*;
import java.util.HashMap;
import util.AssetLoader;
import util.BmpRes;
import com.ccz.blocks.MainActivity;

public class MyCanvas{
	//将graphics.Canvas的输出反序列化并执行绘制
	
	android.graphics.Canvas cv;
	ArrayList<Matrix>mats;
	DataInputStream dis;
	ByteArrayInputStream bais;
	Bitmap[] int2bmp;
	boolean red=false;
	public MyCanvas(android.graphics.Canvas _cv,byte[] _in,Bitmap[] _int2bmp){
		cv=_cv;
		bais=new ByteArrayInputStream(_in);
		dis=new DataInputStream(bais);
		int2bmp=_int2bmp;
	}
	public float rF()throws IOException{
		return dis.readFloat();
	}
	public int rI()throws IOException{
		return dis.readInt();
	}
	public int rS()throws IOException{
		return dis.readShort();
	}
	public byte rB()throws IOException{
		return dis.readByte();
	}
	public String rStr()throws IOException{
		return dis.readUTF();
	}
	public Bitmap rBmpRes()throws IOException{
		int h=rS();
		if(h==0){
			String path=rStr();
			h=rS();
			//android.util.Log.i("rBmpRes",h+" "+path);
			try{
				MainActivity._this.action.known_id.add((short)h);
			}catch(Exception e){}
			int2bmp[h]=AssetLoader.loadBmp(path);
		}
		return int2bmp[h];
	}
	public static Paint default_paint,text_paint,text_stroke_paint,red_paint;
	static{
		default_paint=new Paint();

		text_paint=new Paint();
		text_paint.setColor(0xffffffff);
		text_paint.setTextSize(32);
		text_paint.setTextAlign(Paint.Align.LEFT);
		text_paint.setTypeface(Typeface.MONOSPACE);

		text_stroke_paint=new Paint();
		text_stroke_paint.setStyle(Paint.Style.STROKE);
		text_stroke_paint.setStrokeWidth(4);
		text_stroke_paint.setColor(0xff000000);
		text_stroke_paint.setTextSize(32);
		text_stroke_paint.setTextAlign(Paint.Align.LEFT);
		text_stroke_paint.setTypeface(Typeface.MONOSPACE);
		
		red_paint=new Paint();
		red_paint.setColorFilter(new ColorMatrixColorFilter(new float[]{
			0.5f,0,0,0,127f,
			0,0.5f,0,0,0,
			0,0,0.5f,0,0,
			0,0,0,1,0,
		}));
	}
	public void drawBmp(Bitmap bmp,float l,float t,float r,float b){
		if(bmp!=null)cv.drawBitmap(bmp,new Rect(0,0,bmp.getWidth(),bmp.getHeight()),new RectF(l,t,r,b),red?red_paint:default_paint);
		else{
			Paint pa=new Paint();
			pa.setColor(0xff00ff00);
			cv.drawRect(new RectF(l,t,r,b),pa);
		}
	}
	public void drawBmpRevY(Bitmap bmp,float x,float y,float xd,float yd){
		cv.translate(x,y);
		cv.scale(1,-1);
		drawBmp(bmp,-xd,-yd,xd,yd);
		cv.scale(1,-1);
		cv.translate(-x,-y);
	}
	public boolean exec(int cmd)throws IOException{
		switch(cmd){
			case 0:{
				float x=rF(),y=rF();
				cv.scale(x,y);
				break;
			}case 1:{
				cv.rotate(rF());
				break;
			}case 2:{
				float a=rF(),x=rF(),y=rF();
				cv.rotate(a,x,y);
				break;
			}case 3:{
				float x=rF(),y=rF();
				cv.translate(x,y);
				break;
			}case 4:{
				cv.drawColor(rI());
				break;
			}case 5:{
				float l=rF(),t=rF(),r=rF(),b=rF();
				default_paint.setColor(rI());
				cv.drawRect(l,t,r,b,default_paint);
				default_paint.setAlpha(255);
				break;
			}case 6:{
				String str=rStr();
				float x=rF(),y=rF(),size=rF()/32f;
				int _al=rB();
				cv.save();
				cv.translate(x,y);
				cv.scale(size,size);
				Paint.Align al=Paint.Align.CENTER;
				if(_al==-1)al=Paint.Align.LEFT;
				if(_al==1)al=Paint.Align.RIGHT;
				text_stroke_paint.setTextAlign(al);
				text_paint.setTextAlign(al);
				cv.drawText(str,0,0,text_stroke_paint);
				cv.drawText(str,0,0,text_paint);
				cv.restore();
				break;
			}case 7:{
				Bitmap bmp=rBmpRes();
				float l=rF(),t=rF(),r=rF(),b=rF();
				drawBmp(bmp,l,t,r,b);
				break;
			}case 8:{
				Bitmap bmp=rBmpRes();
				drawBmpRevY(bmp,0,0,0.5f,0.5f);
				break;
			}case 9:{
				Bitmap bmp=rBmpRes();
				float xd=rF(),yd=rF();
				drawBmpRevY(bmp,0,0,xd,yd);				
				break;
			}case 10:{
				Bitmap bmp=rBmpRes();
				float x=rF(),y=rF();
				float xd=rF(),yd=rF();
				drawBmpRevY(bmp,x,y,xd,yd);
				break;
			}case 11:{
				drawBmp(int2bmp[1],-0.45f,-0.45f,0.45f,0.45f);
				break;
			}case 12:{
				drawBmp(int2bmp[2],-0.45f,-0.45f,0.45f,0.45f);
				break;
			}case 13:{
				Bitmap bmp=rBmpRes();
				drawBmp(bmp,-.25f,-.25f,.25f,.25f);
				break;
			}case 14:{
				Bitmap bmp=rBmpRes();
				default_paint.setAlpha(127);
				drawBmp(bmp,-.25f,-.25f,.25f,.25f);
				default_paint.setAlpha(255);
				break;
			}
			case 16:{
				float x=rF(),y=rF();
				grid(x,y);
				break;
			}
			case 20:{
				default_paint.setColor(rI());
				cv.drawRect(-.5f,-.5f,.5f,.5f,default_paint);
				default_paint.setAlpha(255);
				break;
			}
			case 64:{
				cv.save(cv.MATRIX_SAVE_FLAG);
				break;
			}
			case 65:{
				cv.restore();
				break;
			}
			case 66:{
				red=!red;
				break;
			}
			default:
				return false;
		}
		return true;
	}
	public boolean grid(float x0,float y0)throws IOException{
		cv.save();
		cv.translate(x0,y0);
		cv.save();
		int y=0,x=0;
		for(;;){
			int cmd=rB();
			if(cmd==17){
				++x;
				cv.restore();
				cv.save();
				cv.translate(x,y);
			}else if(cmd==18){
				x=0;
				++y;
				cv.restore();
				cv.save();
				cv.translate(x,y);
			}else if(!exec(cmd)){
				cv.restore();
				cv.restore();
				return true;
			}
		}
	}
	public void draw()throws IOException{
		while(exec(rB()));
	}
}
