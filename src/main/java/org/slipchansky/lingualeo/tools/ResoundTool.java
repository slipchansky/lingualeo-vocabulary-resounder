package org.slipchansky.lingualeo.tools;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.sound.sampled.UnsupportedAudioFileException;

public class ResoundTool {
	
	public static void getResound (String uri, OutputStream out) throws IOException, UnsupportedAudioFileException {
		URL url = new URL(uri);
		
        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
        urlConn.addRequestProperty("User-Agent", "Mozilla/4.76");
        InputStream audioSrc = urlConn.getInputStream();
        DataInputStream read = new DataInputStream(audioSrc);
        byte[] buffer = new byte[1024];
        int len;
        while ((len = read.read(buffer)) > 0) {
                out.write(buffer, 0, len);                    
        }		
        audioSrc.close();
	}
	
	public static void getResound (String text, String lang, OutputStream out) throws UnsupportedEncodingException, IOException, UnsupportedAudioFileException {
		getResound ("http://translate.google.com/translate_tts?ie=UTF-8&q="+java.net.URLEncoder.encode(text, "UTF-8")+"&tl="+lang, out);
		
	}
	
	public static void main(String[] args) throws IOException, UnsupportedAudioFileException {
		
		OutputStream out = new FileOutputStream ("d:\\sound.mp3");
		//getResound ("http://translate.google.com/translate_tts?ie=UTF-8&q="+java.net.URLEncoder.encode("фактически", "UTF-8")+"&tl=ru", out);
		//getResound ("http://d2x1jgnvxlnz25.cloudfront.net/v2/2/1926-631152008.mp3", out);
		out.close ();
	}

	public static void silent(int delay, OutputStream out) throws IOException {
		InputStream silentStream = ResoundTool.class.getClassLoader().getResourceAsStream("delay1sec.mp3");
		int c;
		while ((c = silentStream.read())!=-1) {
			out.write(c);
		}
	}
	

}
