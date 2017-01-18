package com.parentoff.rest.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.exceptions.PersistenceException;

public interface ParentDAO<T, PK> {
	public T get(PK id) throws PersistenceException;

	public T getByName(String name) throws PersistenceException;

	public ArrayList<T> getAll() throws PersistenceException;

	public int insert(T objInstance) throws PersistenceException;

	int update(T transientObject) throws PersistenceException;

	int delete(PK id) throws PersistenceException;

	int insertBatch(String id, @SuppressWarnings("rawtypes") Map map)
			throws PersistenceException;

	int deleteBatch(String id, @SuppressWarnings("rawtypes") Map map)
			throws PersistenceException;

	@SuppressWarnings("rawtypes")
	T getOneByQueryId(String id, Map map) throws PersistenceException;

	@SuppressWarnings("rawtypes")
	List getByqueryId(String id, Map map) throws PersistenceException;

	@SuppressWarnings("rawtypes")
	int insertByqueryId(String id, Map map) throws PersistenceException;

	int updatetByqueryId(String id, Map map) throws PersistenceException;

	@SuppressWarnings("rawtypes")
	int deleteByqueryId(String id, Map map) throws PersistenceException;

}
