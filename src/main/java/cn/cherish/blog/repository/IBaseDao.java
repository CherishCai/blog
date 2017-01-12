package cn.cherish.blog.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;

/**
 * 通用DAO接口
 */
@NoRepositoryBean
public interface IBaseDao<E,PK extends Serializable> extends PagingAndSortingRepository<E, PK>, JpaSpecificationExecutor<E>{

	
}
