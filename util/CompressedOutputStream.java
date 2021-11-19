package util;

import java.io.*;
import java.util.*;

public class CompressedOutputStream{
	static final int lens[]={2,3,4,5,6,8,12,16,32,64,128,256,512,1024,2048};
	OutputStream os;
	byte[] last;
	public CompressedOutputStream(OutputStream _os)throws IOException{
		os=_os;
		last=new byte[0];
	}
	int getLen(int x){
		if(x<0)x/=0;
		for(int i=0;;){
			++i;
			x>>=7;
			if(x==0)return i;
		}
	}
	void writeInt(OutputStream os,int x)throws IOException{
		if(x<0)x/=0;
		for(;;){
			int v=x&127;
			x>>=7;
			if(x==0){
				os.write(v|128);
				break;
			}
			os.write(v);
		}
	}
	int mp[]=new int[0x10000<<2],mp_tk=1;
	private void mp_clr(){
		++mp_tk;
	}
	private int mp_get(int x){
		int w=(x&0xffff)<<2;
		if(mp[w]==mp_tk&&mp[w+1]==x)return mp[w+2];
		return 0;
	}
	private void mp_put(int x,int y){
		int w=(x&0xffff)<<2;
		mp[w]=mp_tk;
		mp[w+1]=x;
		mp[w+2]=y;
	}
	public int write(byte b[])throws IOException{
		b=Arrays.copyOf(b,b.length);
		long tt=System.currentTimeMillis();
		final int n0=last.length+1,n=n0+b.length;
		final byte bs[]=new byte[n];
		final int hs[]=new int[n],pp[]=new int[n],P=29399;
		final ByteArrayOutputStream baos=new ByteArrayOutputStream();
		System.arraycopy(last,0,bs,1,last.length);
		System.arraycopy(b,0,bs,n0,b.length);
		last=b;
		pp[0]=1;
		for(int i=1;i<n;++i){
			pp[i]=pp[i-1]*P;
			hs[i]=hs[i-1]*P+bs[i];
		}
		mp_clr();
		final IntArrayList ls=new IntArrayList(),ds=new IntArrayList();
		int bp=n0;
		for(int i=1;i<n;++i){
			o1:
			if(i==bp){
				for(int j=lens.length-1;j>=0;--j){
					int d=lens[j];
					if(i+d>n)continue;
					int p=mp_get(hs[i+d-1]-hs[i-1]*pp[d]);
					o2:
					if(p!=0){
						for(int t=0;t<d;++t)if(bs[p+t]!=bs[i+t])break o2;//hash error
						if(d>=8&&i+d<n&&bs[p+d]==bs[i+d]){
							for(;i+d<n&&bs[p+d]==bs[i+d];++d);
							ls.push((i-p)*16+15);
							ds.push(d);
						}else{
							int x=(i-p)*16+j;//offset=i-p,len=lens[j]
							if(getLen(x)>=d)break o2;
							ls.push(x);
						}
						bp+=d;
						break o1;
					}
				}
				//normal
				ls.push(-1-(255&bs[bp++]));
			}
			for(int j=0;j<lens.length;++j){
				int d=lens[j];
				if(i+d>n)break;
				mp_put(hs[i+d-1]-hs[i-1]*pp[d],i);
			}
		}
		byte tp[]=new byte[ls.n+7>>3];
		for(int i=0;i<ls.n;++i)if(ls.a[i]>=0)tp[i>>3]|=1<<(i&7);
		writeInt(baos,b.length);
		writeInt(baos,ls.n);
		baos.write(tp);
		for(int i=0;i<ls.n;++i){
			int x=ls.a[i];
			if(x>=0){
				writeInt(baos,x);
				if((x&15)==15)writeInt(baos,ds.next());
			}else baos.write((byte)(-1-x));
		}
		byte[]tmp=baos.toByteArray();
		//debug.Log.i(tmp.length+"/"+b.length+": "+b.length*1f/tmp.length+" "+(System.currentTimeMillis()-tt)+"ms");
		os.write(tmp);
		//for(byte by:tmp)System.out.print(((by&128)!=0?"*":"")+(by&127)+",");System.out.println();
		os.flush();
		return tmp.length;
	}
	public void close()throws IOException{
		writeInt(os,0);
		os.close();
	}
}

