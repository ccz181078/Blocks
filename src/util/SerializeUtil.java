package util;

import java.io.*;

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
		ObjectInputStream ois=new ObjectInputStream(bais);
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
	public static Object deepCopy(Object obj){
		try{
			return bytes2obj(obj2bytes(obj));
		}catch(Exception e){}
		return null;
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
