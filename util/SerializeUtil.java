package util;

import java.io.*;
import java.util.*;
import java.util.zip.*;
import java.nio.charset.StandardCharsets;
import graphics.*;

public class SerializeUtil{
	private static final long serialVersionUID=1844677L;

	//对象转为字节数组，要求实现Serializable接口
	public static byte[] obj2bytes(Object object)throws Exception{
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		ObjectOutputStream oos=new ObjectOutputStream(baos);
		oos.writeObject(object);
		return baos.toByteArray();
	}
	
	//字节数组转为对象
	public static Object bytes2obj(byte[] bytes)throws Exception{
		ByteArrayInputStream bais=new ByteArrayInputStream(bytes);
		ObjectInputStream ois=new UncheckedObjectInputStream(bais);
		return ois.readObject();
	}
	
	//从流读字节数组
	public static byte[] readBytes(DataInputStream is)throws Exception{
		byte[] b=new byte[is.readInt()];
		is.readFully(b);
		return b;
	}
	
	//向流写字节数组
	public static void writeBytes(DataOutputStream os,byte[] b)throws Exception{
		os.writeInt(b.length);
		os.write(b);
	}
	
	//对象深拷贝（使用序列化实现，较为低效，慎用）
	public static <T> T deepCopy(T obj){
		try{
			return (T)bytes2obj(obj2bytes(obj));
		}catch(Exception e){}
		return null;
	}
	public static HashMap<String,Bitmap> texture;
	public static HashMap<String,byte[]> texture_bytes;
	public static void loadTexture(){
		texture=new HashMap<>();
		texture_bytes=new HashMap<>();
		try{
			ZipInputStream zin = new ZipInputStream(SerializeUtil.class.getResource("/assets/texture.zip").openStream(),StandardCharsets.UTF_8);
			ZipEntry ze;
			while((ze=zin.getNextEntry())!=null){
				String name=ze.getName();
				long size=ze.getSize();
				if(ze.isDirectory())continue;
				try{
					byte[] buffer=new byte[(int)size];
					long pos=0;
					while(pos<size){
						pos+=zin.read(buffer,(int)pos,(int)(size-pos));
					}
					//if(cnt!=size)System.err.println("bad texture: "+name+" "+cnt+"/"+size);
					texture_bytes.put(name,buffer);
					texture.put(name,BitmapFactory.decodeStream(new ByteArrayInputStream(buffer)));
				}catch(Exception e){/*System.err.println("bad texture: "+name);*/}
			}
		}catch(Exception e){e.printStackTrace(System.err);}
	}

	public static byte[] readBytesFromStream(InputStream inputStream) throws Exception{
		byte[] buffer = new byte[1024];
		int len = -1;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		while((len = inputStream.read(buffer)) != -1){
			outputStream.write(buffer, 0, len);
		}
		outputStream.close();
		inputStream.close();
		return outputStream.toByteArray();
	}
	public static byte[] readBytesFromFile(String file)throws Exception{
		File f=new File(file);
		byte[] b=new byte[(int)f.length()];
		FileInputStream fis=new FileInputStream(f);
		int len=fis.read(b,0,b.length);
		if(len!=b.length)throw new IOException("EOF:"+b.length);
		fis.close();
		return b;
	}
	public static void readStringFromFile(StringBuffer sb,String file)throws Exception{
		FileInputStream fis=new FileInputStream(file);
		BufferedReader br=new BufferedReader(new InputStreamReader(fis));
		for(String tmp;(tmp=br.readLine())!=null;sb.append(tmp),sb.append('\n'));
		br.close();
		fis.close();
	}
	public static void writeStringToFile(String str,String file)throws Exception{
		FileOutputStream fos=new FileOutputStream(file);
		fos.write(str.getBytes());
		fos.close();
	}
}
