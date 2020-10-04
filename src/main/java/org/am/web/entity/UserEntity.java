package org.am.web.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class UserEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int id;
	public String username;
	public String name;
	public String password;
	public int enabled;	
	public String role;
	
	@Column(name="failure_attempt_count",columnDefinition = "int(11) default '0'")
	public Integer failure_attempt_count;
	
	@Column(name="failure_timestamp",columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	public Timestamp failure_timestamp;
}
