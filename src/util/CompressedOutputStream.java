package util;

import java.io.*;
import java.util.zip.*;

public class CompressedOutputStream{
	final int BLOCK_SIZE[]={16,32,64,128};
	DataOutputStream dos;
	ZipOutputStream zos;
	public CompressedOutputStream(OutputStream os)throws IOException{
		zos=new ZipOutputStream(os);
		zos.putNextEntry(new ZipEntry("zip"));
		dos=new DataOutputStream(zos);
	}
	public void write(byte b[])throws IOException{
		dos.writeInt(b.length);
		dos.write(b);
		flush();
	}
	public void flush()throws IOException{
		dos.flush();
		zos.flush();
	}
	public void writeUTF(String str)throws IOException{
		dos.writeUTF(str);
		flush();
	}
	public void close()throws IOException{
		dos.close();
		zos.closeEntry();
		zos.close();
	}
}

