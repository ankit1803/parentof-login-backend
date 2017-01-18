package com.parentoff.rest.db;

import java.io.FileReader;
import java.io.Reader;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class ConnectionFactory {
	
	private static SqlSessionFactory sqlMap;

    static {
        try {
            Reader reader = new FileReader("/home/ec2-user/login-config/SqlMapConfig.xml");

            sqlMap = new SqlSessionFactoryBuilder().build(reader);
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
    
    public static SqlSessionFactory getSession(){
    	return sqlMap;
    }
}
