package com.spring.crud.rest.api.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "EMPLOYEE_IMAGE_TBL")
@SequenceGenerator(name="EMPLOYEE_IMAGE_TBL_SEQ", initialValue=1, allocationSize=1)
public class ImageModel 
{
	public ImageModel() 
	{
		super();
	}
	
	
	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="EMPLOYEE_IMAGE_TBL_SEQ")
	@Column(name = "id")
	private long id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "type")
	private String type;
	
	@Column(name = "picByte")
	private byte[] picByte;

	@Column(name = "crt_date")
	private Date crt_date;
	

	

	

	public Date getCrt_date() {
		return crt_date;
	}

	public void setCrt_date(Date crt_date) {
		this.crt_date = crt_date;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public byte[] getPicByte() {
		return picByte;
	}

	public void setPicByte(byte[] picByte) {
		this.picByte = picByte;
	}

	public ImageModel(String name, String type, byte[] picByte) {
		
		this.name = name;
		this.type = type;
		this.picByte = picByte;
	}

	

	
	
	
	
	
}
