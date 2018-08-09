package com.kg.projects.homework;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


public interface EntryRepository extends CrudRepository<Entry, Long> {
	
	@Query("FROM Entry ORDER BY date_posted DESC")
	//@Query( value = "SELECT * FROM ENTRIES ORDER BY date_posted DESC", nativeQuery = true)
	List<Entry> findAllByOrderByDateAsc();
	
	Optional<Entry> findById(Long id);
		
	<savedEntry extends Entry> Entry save(Entry entry);
	
	void delete(Entry entry);
	
	void deleteById(Long id);
	
}
