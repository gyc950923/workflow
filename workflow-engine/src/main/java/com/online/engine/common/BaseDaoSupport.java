package com.online.engine.common;

import javax.annotation.Resource;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate5.HibernateTemplate;

public class BaseDaoSupport {

    @Resource(name = "hibernateTemplate")
	private HibernateTemplate hibernateTemplate;

	private Session session;

	public SQLQuery createSqlQuery(String sql) {

		return getSession().createSQLQuery(sql);
	}

	public Session getSession() {

		session = this.hibernateTemplate.getSessionFactory().getCurrentSession();
		return session;

	}
	
	public Object sqlParamFilter(Object obj){
	    return obj == null ? "" : obj ;
	}

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

}
