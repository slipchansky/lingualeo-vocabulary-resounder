package org.slipchansky.lingualeo.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.slipchansky.lingualeo.tools.ResoundTool;

public class LoadServlet extends HttpServlet {
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		String[] words = req.getParameterValues("words");
		
		 byte[] zip = zipFiles(words);
		
		ServletOutputStream sos = response.getOutputStream();
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=\"dictionary.ZIP\"");
        response.setContentLength(zip.length);

        sos.write(zip);
        sos.flush();
		
		 
	}


	
	
	 /**
     * Compress the given directory with all its files.
     */
    private byte[] zipFiles(String[] words) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(baos);
        byte bytes[] = new byte[2048];
 
        Map<String, Integer> exists = new HashMap<String, Integer> ();
        for (String metaword : words) {
			String a[] = metaword.split("___");
			if (a.length<2) continue;
			String sound_url = null;
			if (a.length > 2)
				sound_url = a[2];
			
            
			String word = a[0];
			String translation = a[1];
			
			
			Integer n = exists.get(word);
			if (n == null) {
				n = 0;
			} else n++;
			
			exists.put(word, n);
			String fileName = word+(n.intValue()==0?"":(""+n))+".mp3";
 
            zos.putNextEntry(new ZipEntry(fileName));
 
			try {
				if (sound_url != null)
					ResoundTool.getResound(sound_url, zos);
				else
				    ResoundTool.getResound(word, "en", zos);
				ResoundTool.getResound(translation, "ru", zos);
			} catch (UnsupportedAudioFileException e) {
				e.printStackTrace();
			}
            
            zos.closeEntry();
        }
        zos.flush();
        baos.flush();
        zos.close();
        baos.close();
 
        return baos.toByteArray();
    }	
	

}
