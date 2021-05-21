package graphics;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import javax.swing.JComponent;
import javax.swing.JFrame;

import com.ccz.blocks.MainActivity;
import java.util.ArrayList;
import java.io.*;
import java.util.HashMap;
import util.AssetLoader;
import util.BmpRes;
import util.SerializeUtil;

public class MyCanvas{
	//将graphics.Canvas的输出反序列化并执行绘制
	
	Canvas1 cv;
	DataInputStream dis;
	ByteArrayInputStream bais;
	Bitmap[] int2bmp;
	boolean red=false;
	public MyCanvas(Canvas1 _cv,byte[] _in,Bitmap[] _int2bmp){
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
			try{
				MainActivity._this.action.known_id.add((short)h);
			}catch(Exception e){}
			int2bmp[h]=AssetLoader.loadBmp(path);
			if(int2bmp[h]==null){
				try{
					MainActivity._this.action.addUnknownTexture((short)h);
				}catch(Exception e){}
			}
		}
		return int2bmp[h];
	}
	public float[] rFs()throws IOException{
		float ps[]=new float[rI()];
		for(int i=0;i<ps.length;++i)ps[i]=rF();
		return ps;
	}
	public static Paint default_paint,text_paint,text_stroke_paint,red_paint;
	static{
		default_paint=new Paint();
		default_paint.setAntiAlias(false);

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
		red_paint.setAntiAlias(false);
		/*red_paint.setColorFilter(new ColorMatrixColorFilter(new float[]{
			0.5f,0,0,0,127f,
			0,0.5f,0,0,0,
			0,0,0.5f,0,0,
			0,0,0,1,0,
		}));*/
	}
	public void drawBmp(Bitmap bmp,float l,float t,float r,float b){
		if(bmp!=null)cv.drawBitmap(bmp,new Rect(0,0,bmp.getWidth(),bmp.getHeight()),new RectF(l,t,r,b),red?red_paint:default_paint);
		else{
			Paint pa=new Paint();
			pa.setColor(0xff7f7f7f);
			cv.drawRect(new RectF(l,t,r,b),pa);
		}
	}
	public void drawBmpRevY(Bitmap bmp,float x,float y,float xd,float yd){
		cv.save();
		cv.translate(x,y);
		cv.scale(1,-1);
		drawBmp(bmp,-xd,-yd,xd,yd);
		cv.restore();
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
			case 21:{
				Paint p=new Paint();
				p.setColor(rI());
				p.setStrokeWidth(0.05f);
				p.setStrokeCap(Paint.Cap.ROUND);
				cv.drawLines(rFs(),p);
				break;
			}
			case 22:{
				Bitmap bmp=rBmpRes();
				float l0=rF(),t0=rF();
				float r0=rF(),b0=rF();
				float xd=rF(),yd=rF();
				if(bmp!=null){
				int W=bmp.getWidth(),H=bmp.getHeight();
				int l=(int)(l0*W),t=(int)(t0*H),r=(int)(r0*W),b=(int)(b0*H);
				cv.save();
				cv.scale(1,-1);
				cv.drawBitmap(bmp,new Rect(l,t,r,b),new RectF(-xd,-yd,xd,yd),default_paint);
				cv.restore();
				}
				break;
			}
			case 64:{
				cv.save();
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
			case 67:{
				if(!game.GlobalSetting.playing_screen_record)com.ccz.blocks.MainActivity.sendText();
				break;
			}
			case 68:{
				int id=rS();
				try{
					byte b[]=SerializeUtil.readBytes(dis);
					Bitmap bmp=BitmapFactory.decodeByteArray(b,0,b.length);
					int2bmp[id]=bmp;
				}catch(Exception e){debug.Log.i(e);}
				break;
			}
			case 69:{
				int yn=0xff&(int)rB(),xn=0xff&(int)rB();
				float x0=rF(),y0=rF();
				cv.save();
				cv.translate(x0,y0);
				int[][] v=new int[yn][xn];
				for(int i=0;i<yn*xn;++i){
					int x=i%xn,y=i/xn,col=((int)rB())<<24;
					v[y][x]=col;
				}
				Bitmap bmp=BitmapFactory.decodeMatrix(v);
				drawBmp(bmp,0,0,xn,yn);
				/*for(int i=0;i<yn*xn;++i){
					int x=i%xn,y=i/xn,col=((int)rB())<<24;
					default_paint.setColor(col);
					cv.save();
					cv.translate(x,y);
					cv.drawRect(0,0,1,1,default_paint);
					cv.restore();
				}
				default_paint.setAlpha(255);*/
				cv.restore();
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