package util;

import java.io.*;
import java.util.zip.*;

public class CompressedInputStream{
	DataInputStream dis;
	ZipInputStream zis;
	public CompressedInputStream(InputStream is)throws IOException{
		zis=new ZipInputStream(is);
		ZipEntry ze=zis.getNextEntry();
		if(!ze.getName().equals("zip"))throw new IOException();
		dis=new DataInputStream(zis);
	}
	public byte[] read()throws IOException{
		byte bs[]=new byte[dis.readInt()];
		dis.readFully(bs);
		return bs;
	}
	public String readUTF()throws IOException{
		return dis.readUTF();
	}
	public void close()throws IOException{
		dis.close();
		zis.closeEntry();
		zis.close();
	}
}


