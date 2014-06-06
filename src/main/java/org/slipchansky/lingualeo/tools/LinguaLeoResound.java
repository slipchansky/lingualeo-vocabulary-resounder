package org.slipchansky.lingualeo.tools;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.sound.sampled.UnsupportedAudioFileException;

import org.slipchansky.lingualeo.data.WordInfo;




public class LinguaLeoResound {
	
	public static void resound ( List<WordInfo> dictionary, OutputStream out, int delay, int limit) throws IOException, UnsupportedAudioFileException {
		
		int n = 0;
		for (WordInfo word : dictionary) {
			if (limit > 0)
			if (++n >= limit) return;
			for (String translation : word.translations) {
			   resound (word, translation, out);
			}
		}
		
	}

	private static void resound(WordInfo word, String translation, OutputStream out) throws IOException, UnsupportedAudioFileException {
		ResoundTool.getResound(word.sound_url, out);
		ResoundTool.getResound(translation, "ru", out);
		ResoundTool.silent (1, out);
	}

	
	

}
