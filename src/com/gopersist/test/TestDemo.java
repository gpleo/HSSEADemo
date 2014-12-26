package com.gopersist.test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.gopersist.entity.demo.Demo;
import com.gopersist.service.demo.DemoService;

public class TestDemo {
	private ApplicationContext context;
	private DemoService demoService;
	
	@Before
	public void setUp() throws Exception {
		String[] config={"classpath:config/applicationContext-*.xml"};
        context=new FileSystemXmlApplicationContext(config);
        demoService =(DemoService)context.getBean("demoService");
    }

	@Test
	public void testInsert(){
		for(int i=0;i<10;i++){
			Demo demo = new Demo();
			demo.setCode("code"+i);
			demo.setDescription("description"+i);
			demoService.save(demo);
		}
	}
	
	@Test
	public void testDelete(){
		Demo demo = demoService.get("code1");
		assert(demo!=null);
		demoService.delete(demo.getId());
		demo = demoService.get("code1");
		assert(demo==null);
	}
	
	@Test
	public void testUpdate(){
		Demo demo = demoService.get("code2");
		assert(demo!=null);
		demo.setDescription("description8");
		demoService.save(demo);
		demo = demoService.get("code2");
		assert(demo.getDescription().equals("update"));
	}

	@Test
	public void testQuery(){
    	Set<Criterion> set = new HashSet<Criterion>();
    	set.add(Restrictions.like("description", "%8"));
    	
    	List<Demo> list = demoService.query(set);
    	assert(list!=null&&list.size()==2);
	}

	@Test
	public void testPageQuery(){
    	Set<Criterion> criterionSet = new HashSet<Criterion>();
    	Set<Order> orderSet = new HashSet<Order>();
    	orderSet.add(Order.asc("description"));
    	int count = demoService.totalSize(criterionSet);
    	for(int i=0;i<count;i+=5){
			System.out.println("========第"+(i/5+1)+"页========");
    		List<Demo> list = demoService.query(criterionSet, orderSet, i, 5);
    		for(Demo o : list){
    			System.out.println(o.getId()+"|"+o.getCode()+"|"+o.getDescription());
    		}
    	}
	}
}
