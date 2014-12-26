package com.gopersist.action.demo;

import javax.annotation.Resource;

import org.apache.struts2.config.Namespace;

import com.gopersist.action.Struts2ExtjsBaseAction;
import com.gopersist.entity.demo.Demo;
import com.gopersist.service.demo.DemoService;

@Namespace(value="/demo")
public class DemoAction extends Struts2ExtjsBaseAction<Demo, DemoService>{

	private static final long serialVersionUID = 7796054923782630546L;

	// 注入service
	@Resource(name="demoService")
	public void setDemoService(DemoService demoService){
		super.service = demoService;
	}
	
	public DemoAction(){
		super.entity = new Demo();
	}
	
	// entity对象中的set方法
	public void setId(long id) {
		entity.setId(id);
	}
	public void setCode(String code) {
		entity.setCode(code);
	}
	public void setDescription(String description) {
		entity.setDescription(description);
	}
}
