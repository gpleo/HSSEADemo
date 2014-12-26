package com.gopersist.entity.demo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.gopersist.entity.BaseEntity;

@Entity
@Table(name="HSSEA_DEMO")
public class Demo implements BaseEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="hssea_demo")
	@TableGenerator(name = "hssea_demo",
					table="OBJECT_ID",
					pkColumnName="NAME",
					valueColumnName="VALUE",
					pkColumnValue="HSSEA_DEMO_PK",
					initialValue=1,
					allocationSize=1
	)
    @SequenceGenerator(name="hssea_demo_seq", sequenceName="seq_hssea_demo", allocationSize=1)
	private long id;
	@Column(name="CODE")
	private String code;
	@Column(name="DESCRIPTION")
	private String description;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
