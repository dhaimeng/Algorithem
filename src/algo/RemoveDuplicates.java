package algo;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

/** 对btc搜索到的记录化简成S_O格式 */
public class RemoveDuplicates {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File directory = new File("E:\\entity\\obResolution\\121\\");
		String PathTO = "E:\\entity\\obResolution\\after121\\";
		File[] files = directory.listFiles();
		String domainSP = "Predicate,data.nytimes.com,dbpedia.org,rdf.freebase.com,sws.geonames.org,yago-knowledge.org";
		try {
			for (File f : files) {
				String filename = f.getName();
				File fileTo = new File(PathTO + filename);
				FileUtils.write(fileTo, domainSP, true);
				List<String> list = FileUtils.readLines(f);
				for (String lines : list) {
					if (!lines.equals("")) {
						if (!lines.startsWith("<http")) {
							String predicate = lines.substring(0, lines.indexOf(" "));
							FileUtils.write(fileTo, "\n" + predicate + " ", true);
						} else {
							int have = StringUtils.countMatches(lines, "<");
							System.out.println(lines + " " + have);
							if (have > 1) {
								String object = lines.substring(lines.lastIndexOf("> ") + 2, lines.length()).trim();
								if(!object.contains("<")){
									object="<"+object+">";
								}
								
								FileUtils.write(fileTo, object + " ", true);
							} else {
								FileUtils.write(fileTo, "<null> ", true);
							}
						}

					}
				}

			}
		} catch (IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
