package graphics;

import static java.lang.Math.*;
import java.util.Stack;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.font.GlyphVector;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Shape;
import java.awt.geom.*;
import java.io.*;
import javax.swing.JComponent;
import javax.swing.JFrame;

public final class Canvas1{
	private static final float C1=1+2e-4f;
	Graphics2D g2;
	Stack<AffineTransform> stack;
	public boolean skip=false;
	int W,H;
	public Canvas1(Graphics2D _g2,int W,int H){
		g2=_g2;
		stack=new Stack<>();
		this.W=W;this.H=H;
	}
	public void save(){
		if(skip)return;
		stack.push(g2.getTransform());
	}
	public void restore(){
		if(skip)return;
		g2.setTransform(stack.pop());
	}
	public void rotate(float angle){
		if(skip)return;
		g2.rotate((float)toRadians(angle));
	}
	public void rotate(float angle,float x,float y){
		if(skip)return;
		g2.rotate((float)toRadians(angle),x,y);
	}
	public void translate(float x,float y){
		if(skip)return;
		g2.translate(x,y);
	}
	public void scale(float x,float y){
		if(skip)return;
		g2.scale(x,y);
	}
	void drawBitmap(Bitmap bmp,Rect src,RectF dst,Paint paint){
		if(skip)return;
		boolean full=(src.x==0&&src.y==0&&src.w==bmp.getWidth()&&src.h==bmp.getHeight());
		save();
		translate(dst.x,dst.y);
		scale(dst.w/src.w,dst.h/src.h);
		if(full)g2.drawImage(bmp.img,0,0,src.w,src.h,null);
		else g2.drawImage(bmp.img.getSubimage(src.x,src.y,src.w,src.h),0,0,src.w,src.h,null);

		/*g2.setPaint(new Color(0xff00ff00,true));
		g2.setStroke(new BasicStroke(0.05f));
		g2.draw(new Rectangle2D.Float(0,0,src.w,src.h));*/

		restore();
	}
	void drawRect(RectF dst,Paint paint){
		if(skip)return;
		g2.setPaint(new Color(paint.color,true));
		//if(paint.color==0xff000000)g2.fill(new Rectangle2D.Float(dst.x-1e-3f,dst.y-1e-3f,dst.w+2e-3f,dst.h+2e-3f));
		g2.fill(new Rectangle2D.Float(dst.x,dst.y,dst.w,dst.h));
	}
	void drawRect(float l,float t,float r,float b,Paint paint){
		if(skip)return;
		drawRect(new RectF(l,t,r,b),paint);
	}
	void drawLines(float ps[],Paint paint){
		if(skip)return;
		g2.setStroke(new BasicStroke(paint.width));
		g2.setPaint(new Color(paint.color,true));
		for(int i=0;i<ps.length;i+=4){
			g2.draw(new Line2D.Float(ps[i],ps[i+1],ps[i+2],ps[i+3]));
		}
	}
	void drawColor(int color){
		if(skip)return;
		g2.setPaint(new Color(color,true));
		g2.fill(new Rectangle2D.Float(-1e6f,-1e6f,2e6f,2e6f));
	}
	void drawText(String text,float x,float y,Paint paint){
		if(skip)return;
		save();
		Font font=new Font("黑体",Font.PLAIN,paint.text_size);
		GlyphVector v = font.createGlyphVector(g2.getFontMetrics(font).getFontRenderContext(),text);
		Shape shape = v.getOutline();
		x-=g2.getFontMetrics(font).stringWidth(text)*paint.align;
		g2.translate(x,y);
		if(paint.style==Paint.Style.FILL){
			g2.setPaint(new Color(paint.color,true));
			g2.fill(shape);
		}else{
			g2.setStroke(new BasicStroke(paint.width));
			g2.setPaint(new Color(paint.color,true));
			g2.draw(shape);
		}
		restore();
	}
}

class Rect{
	int x,y,w,h;
	Rect(int l,int t,int r,int b){
		x=l;y=t;w=r-l;h=b-t;
	}
}

class RectF{
	float x,y,w,h;
	RectF(float l,float t,float r,float b){
		x=l;y=t;w=r-l;h=b-t;
	}
}

class Paint{
	int color=0,text_size=12;
	float width=1,align=0;
	Style style=Style.FILL;
	enum Align{
		LEFT,CENTER,RIGHT
	};
	enum Cap{
		ROUND
	};
	enum Style{
		STROKE,FILL
	};
	void setAlpha(int alpha){
		color=color&0xffffff|alpha<<24;
	}
	void setColor(int color){
		this.color=color;
	}
	void setStyle(Style style){
		this.style=style;
	}
	void setStrokeWidth(float width){
		this.width=width;
	}
	void setStrokeCap(Cap cap){
		
	}
	void setTextAlign(Align align){
		if(align==Align.LEFT)this.align=0;
		if(align==Align.CENTER)this.align=0.5f;
		if(align==Align.RIGHT)this.align=1;
	}
	void setAntiAlias(boolean flag){
		
	}
	void setTextSize(int size){
		text_size=size;
	}
	void setTypeface(Typeface tf){
		
	}
}
enum Typeface{
	MONOSPACE
};