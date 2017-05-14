package cn.cherish.blog.dal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * 通用DAO接口
 */
@NoRepositoryBean
public interface IBaseDAO<E,PK extends Serializable> extends JpaRepository<E, PK>, JpaSpecificationExecutor<E>{

	
}
