package spyderJava.spy;
import java.util.*;
import java.io.*;
public class fileCombiner {
	public String tempPath;
	//缓存目录
	public String outputPath;
	//输出目录
	public fileCombiner(String tempPath,String outputPath) {
		this.tempPath=tempPath;
		this.outputPath=outputPath;
	}
	public void combiner() throws IOException {
		File file=new File(this.tempPath);
		File[] fileList=file.listFiles();
		String encoding = "UTF-8"; 
		FileWriter fw=new FileWriter(outputPath);
		for(int i=0;i<fileList.length;i++) {
			Long filelength=fileList[i].length();
			byte[] filecontent = new byte[filelength.intValue()]; 
			FileInputStream in=new FileInputStream(fileList[i]);
			in.read(filecontent);
			in.close();
			String content=new String(filecontent,encoding);
			fw.write(content);
			fw.write("\n");
		}
		fw.close();
	}
	public void deleteFile() throws IOException{
		File file=new File(this.tempPath);
		File[] fileList=file.listFiles();
		for(int i=0;i<fileList.length;i++) {
			fileList[i].delete();
		}
	}
}
