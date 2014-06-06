package org.slipchansky.lingualeo.tools;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sound.sampled.UnsupportedAudioFileException;

import org.codehaus.jackson.map.ObjectMapper;
import org.slipchansky.lingualeo.data.WordInfo;

public class ResourceAccessor {

	private static final String LINGUALEO_DASHBOARD_URI = "http://lingualeo.com/ru/dashboard";
	private static final String HASH_TAG = "\"serverHash\":\"";
	private String cookieString;
	private String hashCode;

	public ResourceAccessor(String cookies) {
		cookieString = cookies;
		hashCode = getHashCode ();
	}

	public ResourceAccessor(Map<String, String> cookies) {
		super();
		this.cookieString = "";
		
		for (String k : cookies.keySet()) {
			cookieString+=k+'='+URLEncoder.encode(cookies.get(k))+';';
		}
		hashCode = getHashCode (); 
	}
	
	public Map getRawDictionary (int page) throws IOException {
		String uri = "http://lingualeo.com/ru/userdict/json/?sortBy=date&wordType=0&filter=all&page="+page+"&groupId=dictionary&_hash="+hashCode;
		String content = getContent (uri);
		System.out.println(content);
		
		ObjectMapper mapper = new ObjectMapper ();
		HashMap raw = mapper.readValue(content.getBytes("UTF-8"), HashMap.class);
		
		return raw;
	}
	
	
	private String getContent (String urs) throws IOException {
		
		URL url = new URL(urs);
		URLConnection conn = url.openConnection();
		conn.setRequestProperty("Cookie", cookieString);
		conn.setRequestProperty("Accept","text/html,application/xhtml+xml,application/xml;application/json;q=0.9,image/webp,*/*;q=0.8");
//		conn.setRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
		conn.setRequestProperty("Accept-Language", "ru-RU,ru;q=0.8,en-US;q=0.6,en;q=0.4,uk;q=0.2");
		conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.114 Safari/537.36");
		conn.setRequestProperty("Connection", "keep-alive");
		
		conn.connect();
		InputStream is = conn.getInputStream();
		
		
		ByteArrayOutputStream ous = new ByteArrayOutputStream ();
		int c;
		while ((c = is.read()) > 0) {
			ous.write (c);
			//sb.append(charArray, 0, numCharsRead);
		}
		is.close();
		return new String (ous.toByteArray(), "UTF-8");
	}
	
	private String getHashCode () {
		try {
			String content = getContent (LINGUALEO_DASHBOARD_URI);
			int pos = content.indexOf (HASH_TAG);
			content = content.substring(pos+HASH_TAG.length());
			pos = content.indexOf('"');
			content = content.substring(0, pos);
			return content;		
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public List<WordInfo> getDictionary (Date limit) throws IOException {
		List<WordInfo> result = new ArrayList<WordInfo> ();
		for (int page = 0;; page++) {
			Map d = getRawDictionary(page);
			if (!addWords(d, result, limit)) return result;
		}
	}
	
	
	public List<WordInfo> getDictionary (int daysCount) throws IOException {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date ());
	    cal.add(Calendar.DAY_OF_MONTH, -1*daysCount);
		Date limit = cal.getTime();
		return getDictionary (limit);
		
	} 
	
	private boolean addWords(Map d, List<WordInfo> result, Date limit) {
		List<Map> l = (List<Map>)d.get("userdict3");
		if (l==null) return false;
		if (l.size()==0) return false;
		
		for (Map m : l) {
			if (m==null)
				continue;
			List<Map> words = (List<Map>)m.get("words");
			for (Map word : words) {
				//training_count=4, created_at=1401980851, last_updated_at=1402032884
				Integer training_count = (Integer) word.get("training_count");
				Date created_at = new Date (new Long(1000L*(Integer)word.get("created_at")));
				if (created_at.compareTo(limit) < 0) return false;
				Date last_updated_at = new Date (new Long(1000L*(Integer)word.get("last_updated_at")));
				String word_value=(String)word.get("word_value");
				String sound_url = (String)word.get("sound_url");
				List<Map> user_translates = (List<Map>)word.get("user_translates");
				List<String> translations = new ArrayList<String> ();
				for (Map translation : user_translates) {
					String translate_value = (String)translation.get("translate_value");
					if (!translations.contains(translate_value)) {
						translations.add(translate_value);
					}	
				}
				
				WordInfo wordInfo = new WordInfo(word_value, training_count, created_at, last_updated_at, sound_url, translations);
				result.add(wordInfo);
				
			}
		}
		
		return true;
		
	}
	
	
	
	public static void main(String[] args) throws IOException, UnsupportedAudioFileException {

		
		
		Map<String, String> cookies = new HashMap<String, String> () {{
			put(" AWSELB", "75C701150A9420ACA77B49A59BB2636792D3E5911E337C526E7D4F368B439942289F360C054B5F298A6B745045D72F353AAA2F55B909DD036676008119681317C4B4EEC98A");
			put(" SnapABugHistory", "1#");
			put(" __auc", "7a7e5bc11450757ef696d4879dd");
			put(" __utma", "13441322.2103395756.1386071872.1402030730.1402037386.68");
			put(" __utmc", "13441322");
			put(" __utmv", "13441322.|1");
			put(" __utmz", "13441322.1402030730.67.37.utmcsr");
			put(" browser-plugins-msg-hide", "1");
			put(" iface", "ru");
			put(" lang", "ru");
			put(" optimizelyBuckets", "{}");
			put(" optimizelyEndUserId", "oeu1401084087769r0.4176894500851631");
			put(" optimizelySegments", "{\"1027890468\":\"campaign\",\"1036670278\":\"gc\",\"1037430770\":\"false\"}");
			put(" pic_day", "1");
			put(" promo_redirect_march8update_seen", "1");
			put(" remember", "726420001ab0572cb6317a0ac29539f4a0adb219560a68497ac7a5cac3567e9601462f87c99997a7");
			put(" userid", "2122866");
			put("lingualeouid", "1380546704262052");
			
		}};
		
		ResourceAccessor acc = new ResourceAccessor (cookies);
		List<WordInfo> dictionary = acc.getDictionary(10);
		
		OutputStream out = new FileOutputStream ("d:\\leo.mp3");
		
		LinguaLeoResound.resound(dictionary, out, 1, 10);
		
		out.close (); 
		
		int k = 0;
		k++;
		
	}


}
