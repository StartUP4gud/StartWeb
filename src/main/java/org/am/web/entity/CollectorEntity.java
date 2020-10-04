package org.am.web.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "collectors")
public class CollectorEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int id;
	
	public String hostname;
	public String ip;
	public String protocol;
	public Integer port;
	public Integer sampling_rate;
	public Integer template_id;	
	public String version;	
	public Integer export_timeout;
	public Integer enabled;
}
