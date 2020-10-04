package org.am.web.dao;

import java.util.List;

import org.am.web.entity.TemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface TemplateDao extends JpaRepository< TemplateEntity, String> {

	@Query("SELECT obj FROM TemplateEntity obj where id=:id")
	public TemplateEntity findByID(@Param("id")Integer id);
	
	@Query("SELECT obj FROM TemplateEntity obj where version=:version")
	public List<TemplateEntity> findByVersion(@Param("version")String version);
	
	
}