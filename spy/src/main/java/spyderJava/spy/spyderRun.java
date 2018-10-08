package spyderJava.spy;
import java.io.IOException;
import java.util.*;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class spyderRun {
	public static void main(String[] args) throws IOException{
		String pattern="(http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?";
		FileWriter fw = new FileWriter("result.txt");
		String baseUrl="https://www.sogou.com/sogou?site=news.qq.com&query=%E5%85%BB%E8%80%81%E6%8E%A8%E8%8D%90&pid=sogou-wsse-b58ac8403eb9cf17-0004&duppid=1&idx=f&";
		String page="page=";
		String charset="&ie=utf8";
		for(int ii=1;ii<=200;ii++) {
			Document doc=Jsoup.connect(baseUrl+page+ii+charset).get();
			List<Element> title=doc.select("h3");
			//System.out.println(title.get(i).text());
			List<String> link=new ArrayList<String>();
			List<String> trueLink=new ArrayList<String>();
			for(int i=1;i<title.size();i++) {
				//System.out.println(title.get(i).text());
				Elements href=title.get(i).select("a");
				link.add(href.attr("abs:href"));
				/*List<Element> href=title.get(i).select(".href");
				System.out.println(href.get(i).text());
				for(int j=0;j<href.size();j++) {
					System.out.println(href.get(j).text());
				}*/
			}
			for(int i=0;i<link.size();i++) {
				Document doc1=Jsoup.connect(link.get(i)).followRedirects(true).get();
				//System.out.println(doc1);
				String source=doc1.getElementsByTag("script").get(0).toString();
				//System.out.println(source);
				Pattern r = Pattern.compile(pattern);
				Matcher m=r.matcher(source);
				m.find();
				trueLink.add(m.group(0));
				//System.out.println(m.find());
				//System.out.println(m.group(0));
				//System.out.println(para);
				/*for(int j=0;j<para.size();j++) {
					System.out.println(para.get(j).text());
					fw.write(para.get(j).text());
					fw.write("\n");
				}*/
			}
			for(int i=0;i<trueLink.size();i++) {
				Document doc2=Jsoup.connect(trueLink.get(i)).get();
				Elements para=doc2.select("#ArticleCnt");
				List<Element>paraa=para.select("p");
				for(int j=0;j<paraa.size();j++) {
					fw.write(paraa.get(j).text());
					fw.write("\n");
				}
				fw.write("\n");
			}
		}
		fw.close();
	}
}
