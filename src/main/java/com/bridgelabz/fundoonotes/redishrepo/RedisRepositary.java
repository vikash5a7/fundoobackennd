package com.bridgelabz.fundoonotes.redishrepo;

import java.util.Map;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundoonotes.Entity.NoteInformation;

@Repository
public class RedisRepositary {
	private RedisTemplate<String, Object> redisTemplate;
	private static final String KEY = "notes";

	private HashOperations<String, Long, Object> hashOperations;

	public RedisRepositary(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;

		hashOperations = redisTemplate.opsForHash();
	}

	public Map<Long, Object> getAllItems() {
		return hashOperations.entries(KEY);
	}


	// Adding an item into reds database
	public void addItem(NoteInformation item) {
		System.out.println("item are following........" + item);
		hashOperations.put(KEY, item.getId(), item);
		System.out.println("Data inserted");

	}

	// delete item from the radis database
	public void deleteItem(int id) {
		hashOperations.delete(KEY, id);
	}

	// update an item from database
	public void updateItem(NoteInformation item) {
		addItem(item);
	}
}
