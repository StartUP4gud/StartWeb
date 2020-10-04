package org.am.web.dao;


import org.am.web.entity.CollectorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectorDao extends JpaRepository< CollectorEntity, String> {

	@Query("SELECT obj FROM CollectorEntity obj where id=:id")
	public CollectorEntity findByID(@Param("id")Integer id);
	
}
