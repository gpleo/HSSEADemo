package com.gopersist.action;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.json.JSONResult;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.gopersist.entity.BaseEntity;
import com.gopersist.service.BaseServiceForHibernate;
import com.opensymphony.xwork2.ActionSupport;

/**
 * struts2与extjs交互抽象类
 * 完成基本增删改查分页等功能，子类继承时只需设置entity中属性的set。
 * @author Leo
 * @date 2009-12-31
 * @param <T extends BaseEntity, X extends BaseHibernateDao<T>> entity和service
 */
@Controller
@Scope("prototype")
@ParentPackage(value="json-default")
@Results({
    @Result(name="success", value="", type=JSONResult.class, params={"ignoreHierarchy", "false"})
})
public abstract class Struts2ExtjsBaseAction<T extends BaseEntity, X extends BaseServiceForHibernate<T>> extends ActionSupport{
	static Logger logger = Logger.getLogger(Struts2ExtjsBaseAction.class.getName());
	
	private static final long serialVersionUID = 2863769505963567954L;
	
	// Extjs 使用，成功与否，返回信息
	protected boolean success = false;
	protected String message;
    // Extjs 使用，分页、排序信息
    protected int start;
    protected int limit;
    protected String sort;
    protected String dir;
	
    // Service类
    protected X service;
    
    // 返回的数据列表和记录总数
    protected List<T> list;
    protected int total;
    
    // entity对象，用于提交保存，更新前取个别数据
    protected T entity;
    
    // 记录选中记录的id
    protected String ids;

	/**
     * 取列表
     */
    public String list() {
		logger.debug("取数据列表！start:"+this.start+"，limit:"+this.limit+",sort:"+this.sort+"，dir:"+this.dir);
    	
    	Set<Criterion> criterionSet = new HashSet<Criterion>();
    	Set<Order> orderSet = new HashSet<Order>();
    	if(this.dir.equals("ASC")){
    		orderSet.add(Order.asc(this.sort));
    	}else{
    		orderSet.add(Order.desc(this.sort));
    	}
    	list = service.query(criterionSet, orderSet, this.start, this.limit);
    	total = service.totalSize(criterionSet);
    	success = true;
        return SUCCESS;
    }
    /**
     * 根据ID取数据
     */
    public String get() {
    	logger.debug("根据id取数据明细！id:"+entity.getId());
    	
   		entity = service.get(entity.getId());
   		success = true;
        return SUCCESS;
    }
    /**
     * 保存
     */
    public String save() {
    	logger.debug("保存数据！entity:"+entity);

    	service.save(entity);
    	success = true;
        return SUCCESS;
    }
    /**
     * 删除
     */
    public String delete() {
    	logger.debug("根据ids删除数据！ids:"+ids);
    	
    	for(String s : ids.split(",")){
    		service.delete(Long.parseLong(s));
    	}
    	success = true;
    	return SUCCESS;
    }

	// 对象T内部信息的读取均在子类中进行
	
	// getter,setter方法
    // struts2需get方法将数据转换成json
	public List<T> getList() {
		return list;
	}
	public int getTotal() {
		return total;
	}
	public boolean isSuccess() {
		return success;
	}
	public String getMessage() {
		return message;
	}

	// 读取extjs传来的分页和排序信息	
	public void setStart(int start) {
		this.start = start;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public void setDir(String dir) {
		this.dir = dir;
	}
	// 读取extjs传来的选中记录id
	public void setIds(String ids) {
		this.ids = ids;
	}
	
	public T getEntity() {
		return entity;
	}
}
