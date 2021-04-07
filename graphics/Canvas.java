package graphics;

import java.io.*;
import util.BmpRes;
import java.util.BitSet;
import game.GameSetting;
import util.SerializeUtil;

public class Canvas{
	//支持简单的绘图
	//将绘图过程序列化输出
	
	DataOutputStream dos;
	ByteArrayOutputStream baos;
	BitSet known_id;
	public GameSetting gs;
	public Canvas(BitSet _known_id,GameSetting _gs){
		baos=new ByteArrayOutputStream();
		dos=new DataOutputStream(baos);
		known_id=_known_id;
		gs=_gs;
	}
	public byte[] getBytes(){
		byte[] ret=null;
		try{
			dos.flush();
			ret=baos.toByteArray();
			baos.close();
		}catch(Exception e){e.printStackTrace();}
		return ret;
	}
	public void wF(float a){
		try{
			dos.writeFloat(a);
		}catch(Exception e){}
	}
	public void wI(int a){
		try{
			dos.writeInt(a);
		}catch(Exception e){}
	}
	public void wS(int a){
		try{
			dos.writeShort(a);
		}catch(Exception e){}
	}
	public void wB(int a){
		try{
			dos.writeByte(a);
		}catch(Exception e){}
	}
	public void wStr(String a){
		try{
			dos.writeUTF(a);
		}catch(Exception e){}
	}
	public void wBmpRes(BmpRes bmp){
		int h=bmp.hashCode();
		if(!known_id.get(h)){
			known_id.set(h);
			wS(0);
			wStr(bmp.getPath());
		}
		wS(h);
	}
	public void wFs(float ps[]){
		wI(ps.length);
		for(int i=0;i<ps.length;++i)wF(ps[i]);
	}
	public void end(){
		wB(255);
	}
	
	public void scale(float x,float y){
		wB(0);
		wF(x);wF(y);
	}
	public void rotate(float a){
		wB(1);
		wF(a);
	}
	public void rotate(float a,float x,float y){
		wB(2);
		wF(a);wF(x);wF(y);
	}
	public void translate(float x,float y){
		wB(3);
		wF(x);wF(y);
	}
	public void drawColor(int c){
		wB(4);
		wI(c);
	}
	public void drawRect(float l,float t,float r,float b,int col){
		wB(5);
		wF(l);wF(t);
		wF(r);wF(b);
		wI(col);
	}
	public void drawText(String s,float x,float y,float size,int align){
		wB(6);
		wStr(s);
		wF(x);wF(y);wF(size);
		wB(align);		
	}
	public void drawBitmap(BmpRes bmp,float l,float t,float r,float b){
		wB(7);
		wBmpRes(bmp);
		wF(l);wF(t);
		wF(r);wF(b);
	}
	public void drawBitmapRevY(BmpRes bmp,float x,float y,float xd,float yd){
		if(x==0&&y==0){
			if(xd==0.5f&&yd==0.5f){
				wB(8);
				wBmpRes(bmp);
			}else{
				wB(9);
				wBmpRes(bmp);
				wF(xd);wF(yd);
			}
		}else{
			wB(10);
			wBmpRes(bmp);
			wF(x);wF(y);
			wF(xd);wF(yd);
		}
	}
	public void drawItemFrame(boolean selected){
		wB(selected?11:12);
	}
	public void drawItem(BmpRes bmp,boolean real){
		wB(real?13:14);
		wBmpRes(bmp);
	}
	public void gridBegin(float x,float y){
		wB(16);
		wF(x);wF(y);
	}
	public void gridNext(){
		wB(17);
	}
	public void gridNewLine(){
		wB(18);
	}
	public void drawRect(int col){
		if(col==0)return;
		wB(20);
		wI(col);
	}
	public void drawLines(float xys[],int col){
		wB(21);
		wI(col);
		wFs(xys);
	}
	public void drawBitmapRevY(BmpRes bmp,float l0,float t0,float r0,float b0,float xd,float yd){
		wB(22);
		wBmpRes(bmp);
		wF(l0);wF(t0);wF(r0);wF(b0);
		wF(xd);wF(yd);
	}
	public void save(){
		wB(64);
	}
	public void restore(){
		wB(65);
	}
	public void red(){
		wB(66);
	}
	public void sendText(){
		wB(67);
	}
	public void wBmp(short id){
		String path=BmpRes.int2path[id];
		try{
			//byte b[]=SerializeUtil.readBytesFromStream(util.AssetLoader.class.getResource("/assets/"+path+".png").openStream());
			byte b[]=SerializeUtil.texture_bytes.get(path+".png");
			wB(68);
			wS(id);
			SerializeUtil.writeBytes(dos,b);
		}catch(Exception e){e.printStackTrace();}
	}
	public void drawLight(int light[][],int yn,int xn,float x0,float y0){
		wB(69);
		wB((byte)yn);wB((byte)xn);
		wF(x0);wF(y0);
		for(int y=0;y<yn;++y){
			for(int x=0;x<xn;++x){
				wB((byte)light[y][x]);
			}
		}
	}
}
