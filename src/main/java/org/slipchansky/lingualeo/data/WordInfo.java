package org.slipchansky.lingualeo.data;

import java.util.Date;
import java.util.List;

public class WordInfo {

	public String word;
	public Integer training_count;
	public Date created_at;
	public Date last_updated_at;
	public String sound_url;
	
	public List<String> translations;

	
	public WordInfo(String word, Integer training_count, Date created_at,
			Date last_updated_at, String sound_url, List<String> translations) {
		super();
		this.word = word;
		this.training_count = training_count;
		this.created_at = created_at;
		this.last_updated_at = last_updated_at;
		this.sound_url = sound_url;
		this.translations = translations;
	}


	@Override
	public String toString() {
		return "WordInfo [word=" + word + ", translations=" + translations
				+ "]";
	}

}
