package com.silica.info.model;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Project {
	public String projectName;
	public String author;
	public Date createdDate;
}
