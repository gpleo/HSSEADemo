package com.gopersist.service.demo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.gopersist.entity.demo.Demo;
import com.gopersist.service.BaseServiceForHibernate;

@Service("demoService")
public class DemoService extends BaseServiceForHibernate<Demo> {
	/**
	 * 根据code取对象
	 * @param code
	 * @return
	 */
	public Demo get(String code){
		Set<Criterion> set = new HashSet<Criterion>();
		set.add(Restrictions.eq("code", code));
		List<Demo> list = super.query(set);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
}
