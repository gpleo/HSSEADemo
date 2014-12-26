package com.gopersist.service;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * 增删改查分页等基本功能抽象类，使用hibernate做dao层
 * @author Leo
 * @date 2009-12-31
 * @param <T> entity类
 */
public abstract class BaseServiceForHibernate<T> extends HibernateDaoSupport{
	static Logger logger = Logger.getLogger(BaseServiceForHibernate.class.getName());

	//为父类HibernateDaoSupport注入sessionFactory的值
	@Resource(name="sessionFactory")
    public void setSuperSessionFactory(SessionFactory sessionFactory){
    	logger.debug("为父类HibernateDaoSupport注入sessionFactory的值["+sessionFactory+"]");
        super.setSessionFactory(sessionFactory);
    }

	private Class<T> entityClass;

	@SuppressWarnings("unchecked")
	public BaseServiceForHibernate(){
		entityClass =(Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
		logger.debug("得到entity对象类实例["+entityClass+"]");
	}
	
	/**
	 * 根据对象是否存在ID新增或更新记录
	 * @param entity对象
	 */
    public void save(T o){
		logger.debug("保存数据，对象："+o);
        super.getHibernateTemplate().saveOrUpdate(o);
    }
    
    /**
     * 根据主键删除记录
     * @param 主键
     */
    public void delete(Serializable id){
		logger.debug("根据主键删除数据，主键："+id);
        super.getHibernateTemplate().delete(super.getHibernateTemplate().load(entityClass, id));
    }
    
    /**
     * 根据条件查询记录
     * @param 存储条件的容器
     * @return 数据列表
     */
    @SuppressWarnings("unchecked")
    public List<T> query(Set<Criterion> criterionSet){
		logger.debug("根据条件查询数据！条件数："+criterionSet.size());

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(entityClass);
    	for(Criterion o : criterionSet){
    		detachedCriteria.add(o);
    	}
        return super.getHibernateTemplate().findByCriteria(detachedCriteria);
    }

    /**
     * 根据条件查询记录
     * @param 存储条件的容器
     * @param 存储排序的容器
     * @return 数据列表
     */
    @SuppressWarnings("unchecked")
    public List<T> query(Set<Criterion> criterionSet, Set<Order> orderSet){
		logger.debug("根据条件和排序查询数据！条件数："+criterionSet.size()+"，排序数："+orderSet.size());

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(entityClass);
    	for(Criterion o : criterionSet){
    		detachedCriteria.add(o);
    	}
    	for(Order o : orderSet){
    		detachedCriteria.addOrder(o);
    	}
        return super.getHibernateTemplate().findByCriteria(detachedCriteria);
    }

    /**
     * 根据条件分页查询记录
     * @param 存储条件的容器
     * @param 数据开始位置（第一条记录为0）
     * @param 最大数据数
     * @return 数据列表
     */
    @SuppressWarnings("unchecked")
    public List<T> query(Set<Criterion> criterionSet, int firstResult, int maxResults){
		logger.debug("根据条件分页查询数据！条件数："+criterionSet.size()+"，记录开始序号："+firstResult+"，最大记录数："+maxResults);

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(entityClass);
    	for(Criterion o : criterionSet){
    		detachedCriteria.add(o);
    	}
        return super.getHibernateTemplate().findByCriteria(detachedCriteria, firstResult, maxResults);
    }

    /**
     * 根据条件分页查询记录
     * @param 存储条件的容器
     * @param 存储排序的容器
     * @param 数据开始位置（第一条记录为0）
     * @param 最大数据数
     * @return 数据列表
     */
    @SuppressWarnings("unchecked")
    public List<T> query(Set<Criterion> criterionSet, Set<Order> orderSet, int firstResult, int maxResults){
		logger.debug("根据条件和排序分页查询数据！条件数："+criterionSet.size()+"，排序数："+orderSet.size()+"，记录开始序号："+firstResult+"，最大记录数："+maxResults);

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(entityClass);
    	for(Criterion o : criterionSet){
    		detachedCriteria.add(o);
    	}
    	for(Order o : orderSet){
    		detachedCriteria.addOrder(o);
    	}
        return super.getHibernateTemplate().findByCriteria(detachedCriteria, firstResult, maxResults);
    }

    /**
     * 根据条件取得记录总数[性能严重问题，需改]
     * @param 存储条件的容器
     * @return 记录总数
     */
    public int totalSize(Set<Criterion> criterionSet){
		logger.debug("根据条件取记录总数！条件数："+criterionSet.size());

    	List<T> list = query(criterionSet);
    	return list!=null?list.size():0;
    }
    
    /**
     * 根据主键取得数据
     * @param 主键
     * @return entity对象
     */
    @SuppressWarnings("unchecked")
    public T get(Serializable id){
		logger.debug("根据主键删除数据，主键："+id);

		return (T)super.getHibernateTemplate().get(entityClass, id);
    }
}
