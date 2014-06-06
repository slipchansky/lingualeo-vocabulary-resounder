package org.slipchansky.lingualeo.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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
		
		boolean forward = true;
		if ("backward".equals(req.getParameter("direction"))) forward = false;
		
		String filename=req.getParameter("filename");
		if (filename != null) {
			if (filename.toLowerCase().endsWith (".zip")) 
				filename = filename.substring(0, filename.length()-".zip".length());
		}
		
		byte[] zip = zipFiles(words, forward);
		
		ServletOutputStream sos = response.getOutputStream();
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=\""+filename+".zip\"");
        response.setContentLength(zip.length);

        sos.write(zip);
        sos.flush();
		
		 
	}


	
	
	 /**
     * Compress the given directory with all its files.
     */
    private byte[] zipFiles(String[] words, boolean forward) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(baos);
        byte bytes[] = new byte[2048];
 
        Set<String> exists = new HashSet<String> ();
        for (String metaword : words) {
        	if (exists.contains(metaword)) continue;
        	exists.add(metaword);
			String a[] = metaword.split("___");
			if (a.length<2) continue;
			String sound_url = null;
			if (a.length > 2)
				sound_url = a[2];
			
            
			String word = a[0];
			String translation = a[1];
			
			if (forward)
			    addForward(zos, sound_url, word, translation);
			else
			    addBackward(zos, sound_url, word, translation);
            
            zos.closeEntry();
        }
        zos.flush();
        baos.flush();
        zos.close();
        baos.close();
 
        return baos.toByteArray();
    }




	private String addForward(ZipOutputStream zos, String sound_url,
			String word, String translation) throws IOException,
			UnsupportedEncodingException {
		String fileName = newFileName(word, translation);
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
		return fileName;
	}
	
	private String addBackward(ZipOutputStream zos, String sound_url,
			String word, String translation) throws IOException,
			UnsupportedEncodingException {
		String fileName = newFileName(translation, word);
		zos.putNextEntry(new ZipEntry(fileName));
		try {
			ResoundTool.getResound(translation, "ru", zos);
			if (sound_url != null)
				ResoundTool.getResound(sound_url, zos);
			else
			    ResoundTool.getResound(word, "en", zos);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		}
		return fileName;
	}




	private String newFileName(String a, String b) {
		String fileName = a+" - "+b;
		fileName = fileName.replace('/', '-').replace('\\', '-').replace('|', '-').replace('+', '-').replace('%', '-').replace('&', '-');
		fileName+=".mp3";
		return fileName;
	}	
	

}
