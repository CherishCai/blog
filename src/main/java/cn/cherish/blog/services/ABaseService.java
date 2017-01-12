package cn.cherish.blog.services;

import cn.cherish.blog.repository.IBaseDao;
import cn.cherish.blog.utils.DynamicSpecifications;
import cn.cherish.blog.utils.ObjectConvertUtil;
import cn.cherish.blog.utils.Reflections;
import cn.cherish.blog.utils.SearchFilter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * ABaseService
 * 基础Service 封装常用 CURD 及分页 查询
 * 子类继承后可以省略代码
 */
@Component
public abstract class ABaseService<E, PK extends Serializable> {

    protected Class<E> entityClass;

    /**
     * 用于Service层子类使用的构造函数.
     * 通过子类的泛型定义取得对象类型Class.
     * eg. public class UserService extends ABaseService<User, Long>
     */
    public ABaseService() {
        this.entityClass = Reflections.getClassGenricType(getClass());
    }

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected abstract IBaseDao<E, PK> getEntityDAO();

    public E findById(PK id) {
        return getEntityDAO().findOne(id);
    }

    public List<E> findAll() {
        return (List<E>) getEntityDAO().findAll();
    }

    public List<E> findByEntity(E entity) {
        return getEntityDAO().findAll(buildSpecification(entity));
    }

    public E findByEntityUnique(E entity) {
        return getEntityDAO().findOne(buildSpecification(entity));

    }

    public List<E> findByEntityOrder(E entity, String orderField, boolean isAsc) {
        Sort sort = new Sort(Direction.ASC, orderField.trim());
        return getEntityDAO().findAll(buildSpecification(entity), sort);
    }

    public List<E> findByProperty(String propertyName, Object propertyValue) {
        return getEntityDAO().findAll(buildSpecification(propertyName, propertyValue));
    }

    private Specification<E> buildSpecification(String propertyName, Object propertyValue) {
        Specification<E> spec = DynamicSpecifications.bySearchFilter(SearchFilter.parse(buildSearchParams(propertyName, propertyValue)).values(), this.entityClass);
        return spec;
    }

    @Transactional(readOnly = false)
    public E save(E entity) {
        return getEntityDAO().save(entity);
    }


    public Long getCount() {
        return getEntityDAO().count();
    }

    public Long getCount(E entity) {
        return getEntityDAO().count(buildSpecification(entity));
    }

    @Transactional(readOnly = false)
    public void removeById(PK id) {
        getEntityDAO().delete(id);
    }

    @Transactional(readOnly = false)
    public void update(E entity) {
        getEntityDAO().save(entity);
    }

    public boolean isUnique(E entity) {
        return getEntityDAO().count(buildSpecification(entity)) == 0;
    }

    /**
     * 带参数的分页查询
     * 1，提取分页参数，组装到PageRequest对象中。
     * 2，提取查询过滤参数，组装到Specification<T>对象中。
     * 3，执行查询（参数包装对象，分页对象）
     */
    public Page<E> searchAllByPageSort(Map<String, Object> searchParams, int pageNumber, int pageSize, String sortType) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
        Specification<E> spec = buildSpecification(searchParams);
        return this.getEntityDAO().findAll(spec, pageRequest);
    }

    private Specification<E> buildSpecification(E entity) {
        //Class<E> clazz = (Class<E>) entity.getClass();
        return DynamicSpecifications.bySearchFilter(SearchFilter.parse(buildSearchParams(entity)).values(), this.entityClass);
    }

    private Specification<E> buildSpecification(Map<String, Object> searchParams) {
        return DynamicSpecifications.bySearchFilter(SearchFilter.parse(searchParams).values(), this.entityClass);
    }

    /**
     * 把实体对象封装成Map<String,Object>
     * 如：object对象 ==>Map｛EQ_id:1;EQ_name:admin}
     */
    private Map<String, Object> buildSearchParams(E entity) {
        Map<String, Object> searchParams = new HashMap<>();
        Map<String, Object> map = ObjectConvertUtil.objectToMap(entity);
        if (map != null && !map.isEmpty()) {
            for (Entry<String, Object> entry : map.entrySet()) {
                // 过滤掉空值
                String key = entry.getKey();
                Object value = entry.getValue();
                if (null != value) {
                    if (value instanceof String) {
                        if (StringUtils.isBlank((String) value)) {
                            continue;
                        }
                    }
                    String searchParam = "EQ_" + key;
                    searchParams.put(searchParam, value);
                }
            }
        }
        return searchParams;
    }

    /**
     * 把实体对象封装成Map<String,Object>
     * 如：object对象 ==>Map｛EQ_id:1;EQ_name:admin}
     */
    private Map<String, Object> buildSearchParams(String propertyName, Object propertyValue) {
        Map<String, Object> searchParams = new HashMap<>();
        String searchParam = "EQ_" + propertyName;
        searchParams.put(searchParam, propertyValue);
        return searchParams;
    }

    /**
     * 创建分页请求.
     */
    private PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType) {
        Sort sort = new Sort(Direction.DESC, "id");
        ;
        if (!StringUtils.isEmpty(sortType) && !"auto".equals(sortType)) {
            sort = new Sort(Direction.ASC, sortType.trim());
        }
        return new PageRequest(pageNumber - 1, pagzSize, sort);
    }
}
