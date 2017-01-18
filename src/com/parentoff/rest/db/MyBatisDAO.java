package com.parentoff.rest.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class MyBatisDAO<T, PK> implements ParentDAO<T, PK> {
	private static final String NAMESPACE = "mappers";

	protected SqlSessionFactory sf = ConnectionFactory.getSession();
	private Class<T> type;

	public static final String PREFIX_SELECT_QUERY = "get";
	public static final String PREFIX_INSERT_QUERY = "insert";
	public static final String PREFIX_UPDATE_QUERY = "update";
	public static final String PREFIX_DELETE_QUERY = "delete";

	public MyBatisDAO(Class<T> type) {
		this.type = type;
	}

	protected SqlSessionFactory getSessionFactory() {
		return sf;
	}

	@SuppressWarnings("unchecked")
	public T get(PK id) throws PersistenceException {
		SqlSession session = sf.openSession();
		T obj = null;
		try {
			String query = NAMESPACE + "." + PREFIX_SELECT_QUERY
					+ this.type.getSimpleName();
			obj = (T) session.selectOne(query, id);
		} finally {
			session.close();
		}
		return obj;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<T> getAll() throws PersistenceException {
		SqlSession session = sf.openSession();
		ArrayList<T> list = null;
		try {
			String query = NAMESPACE + "." + PREFIX_SELECT_QUERY + "All"
					+ this.type.getSimpleName();
			list = (ArrayList<T>) session.selectList(query);
		} finally {
			session.close();
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public T getByName(String name) throws PersistenceException {
		SqlSession session = sf.openSession();
		T obj = null;
		try {
			String query = NAMESPACE + "." + PREFIX_SELECT_QUERY
					+ this.type.getSimpleName() + "ByName";
			obj = (T) session.selectOne(query, name);
		} finally {
			session.close();
		}
		return obj;
	}

	public int insert(T o) throws PersistenceException {
		SqlSession session = sf.openSession();
		Integer status = null;
		try {
			String query = NAMESPACE + "." + PREFIX_INSERT_QUERY
					+ this.type.getSimpleName();
			status = (Integer) session.insert(query, o);
			session.commit();
		} finally {
			session.close();
		}
		return status;
	}

	public int update(T o) throws PersistenceException {
		SqlSession session = sf.openSession();
		Integer status = null;
		try {
			String query = NAMESPACE + "." + PREFIX_UPDATE_QUERY
					+ this.type.getSimpleName();
			status = session.update(query, o);
			session.commit();
		} finally {
			session.close();
		}
		return status;
	}

	public int delete(PK id) throws PersistenceException {
		SqlSession session = sf.openSession();
		Integer status = null;
		try {
			String query = NAMESPACE + "." + PREFIX_DELETE_QUERY
					+ this.type.getSimpleName();
			status = session.delete(query, id);
			session.commit();
		} finally {
			session.close();
		}
		return status;
	}

	@SuppressWarnings("rawtypes")
	public int insertBatch(String id, Map map) {
		SqlSession session = sf.openSession(ExecutorType.BATCH, true);
		Integer status = null;
		try {
			status = session.insert(id, map);
			session.commit();
		} finally {
			session.close();
		}

		return status;
	}

	@SuppressWarnings("rawtypes")
	public int deleteBatch(String id, Map map) throws PersistenceException {
		SqlSession session = sf.openSession(ExecutorType.BATCH, true);
		Integer status = null;
		try {
			status = session.delete(id, map);
			session.commit();
		} finally {
			session.close();
		}
		return status;
	}

	@SuppressWarnings("unchecked")
	public T getOneByQueryId(String id, Map map) throws PersistenceException {
		SqlSession session = sf.openSession();
		T obj = null;
		try {
			obj = (T) session.selectOne(id, map);
			session.commit();
		} finally {
			session.close();
		}
		return obj;
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public List getByqueryId(String id, Map map) throws PersistenceException {

		SqlSession session = sf.openSession();
		List list = null;
		try {
			list = session.selectList(id, map);
			session.commit();
		} finally {
			session.close();
		}

		return list;

	}

	@SuppressWarnings("rawtypes")
	@Override
	public int insertByqueryId(String id, Map map) throws PersistenceException {
		SqlSession session = sf.openSession();
		Integer status = null;
		try {
			status = session.insert(id, map);
			session.commit();
		} finally {
			session.close();
		}

		return status;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public int updatetByqueryId(String id, Map map) throws PersistenceException {
		SqlSession session = sf.openSession();
		Integer status = null;
		try {
			status = session.update(id, map);
			session.commit();
		} finally {
			session.close();
		}

		return status;
	}
	
	
	@SuppressWarnings("rawtypes")
	@Override
	public int deleteByqueryId(String id, Map map) throws PersistenceException {
		SqlSession session = sf.openSession();
		Integer status = null;
		try {
			status = session.delete(id, map);
			session.commit();
		} finally {
			session.close();
		}

		return status;
	}
}
