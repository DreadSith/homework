package com.kg.projects.homework;

import java.util.List;

import org.springframework.stereotype.Service;

public interface EntryService {
	
	List<Entry> findAll();
	
	<savedEntry extends Entry> Entry save(Entry entry);
	
	void delete(Entry entry);
	
}
