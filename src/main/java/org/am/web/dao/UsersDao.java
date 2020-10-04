package org.am.web.dao;

import java.sql.Timestamp;
import javax.transaction.Transactional;

import org.am.web.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersDao extends JpaRepository<UserEntity, String> {

	@Query("SELECT obj FROM UserEntity obj where username=:username")
	public UserEntity findByUsername(@Param("username")String username);

	@Query("SELECT obj FROM UserEntity obj where id=:id")
	public UserEntity findByID(@Param("id")Integer id);
	

	@Modifying
	@Transactional
	@Query(value = "UPDATE users set failure_attempt_count = ((SELECT failure_attempt_count FROM (SELECT failure_attempt_count "
			+ "FROM users WHERE username = :username) AS failure_attempt_count) + 1) WHERE username = :username", nativeQuery = true)
	public void updateFailureAttemptCount(@Param("username") String username);

	@Modifying
	@Transactional
	@Query("update UserEntity obj set obj.failure_timestamp = :failure_timestamp where obj.username = :username")
	public void updateFailureTimestamp(@Param("failure_timestamp") Timestamp failure_timestamp, @Param("username") String username);
	
	@Modifying
	@Transactional
	@Query("update UserEntity obj set obj.enabled = 0 where obj.username = :username")
	public void blockUserAfterMaxAttempts(@Param("username") String username);
	
	@Modifying
	@Transactional
	@Query("update UserEntity obj set obj.failure_attempt_count = :failure_attempt_count where obj.username = :username")
	public void resetLoginAttemptCount(@Param("failure_attempt_count") Integer failure_attempt_count, @Param("username") String username);
	
	@Query("select obj from UserEntity obj where obj.username = :username")
	public UserEntity getUserDetailsByUserName (@Param("username") String username);
	
	@Modifying
	@Transactional
	@Query("UPDATE UserEntity obj SET enabled = 1 WHERE enabled = 0 AND TIMESTAMPDIFF(MINUTE,obj.failure_timestamp,CURRENT_TIMESTAMP()) >= 20")
	public void enableUserStatus();
	
}
