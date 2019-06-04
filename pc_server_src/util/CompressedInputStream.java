package util;

import java.io.*;
import java.util.*;

public class CompressedInputStream{
	static final int lens[]={2,3,4,5,6,8,12,16,32,64,128,256,512,1024,2048};
	InputStream is;
	byte[] last;
	public CompressedInputStream(InputStream _is)throws IOException{
		is=_is;
		last=new byte[0];
	}
	int readByte(InputStream is)throws IOException{
		int x=is.read();
		if(x==-1)throw new EOFException();
		return x;
	}
	int readInt(InputStream is)throws IOException{
		int x=0;
		for(int p=0;;p+=7){
			int v=readByte(is);
			x|=(v&127)<<p;
			if((v&128)!=0)return x;
		}
	}
	public byte[] read()throws IOException{
		final int n0=last.length+1,n=n0+readInt(is);
		if(n==n0)throw new EOFException();
		final byte bs[]=new byte[n];
		System.arraycopy(last,0,bs,1,last.length);
		final int sz=readInt(is);
		final byte tp[]=new byte[sz+7>>3];
		for(int i=0;i<tp.length;++i)tp[i]=(byte)readByte(is);
		int bp=n0;
		for(int i=0;i<sz;++i){
			if((tp[i>>3]>>(i&7)&1)!=0){
				int x=readInt(is);
				int d=x>>4,p=bp;
				if((x&15)==15)p+=readInt(is);
				else p+=lens[x&15];
				//System.out.println("d="+d+" len="+(p-bp));
				for(;bp!=p;++bp)bs[bp]=bs[bp-d];
			}else{
				bs[bp++]=(byte)readByte(is);
				//System.out.println(bs[bp-1]+":"+(char)(int)bs[bp-1]);
			}
		}
		if(bp!=n){
			throw new IOException("Compress Decode Error");
		}
		return last=Arrays.copyOfRange(bs,n0,n);
	}
	public void close()throws IOException{
		is.close();
	}
}


