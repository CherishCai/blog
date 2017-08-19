package cn.cherish.blog.service;

import cn.cherish.blog.common.enums.ActiveEnum;
import cn.cherish.blog.dal.entity.User;
import cn.cherish.blog.dal.repository.IBaseDAO;
import cn.cherish.blog.dal.repository.UserDAO;
import cn.cherish.blog.util.ObjectConvertUtil;
import cn.cherish.blog.web.req.BasicSearchReq;
import cn.cherish.blog.web.req.user.UserSaveReq;
import cn.cherish.blog.web.req.user.UserSearchReq;
import cn.cherish.blog.web.req.user.UserUpdateReq;
import cn.cherish.blog.web.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UserService extends ABaseService<User, Long> {

    private final UserDAO userDAO;

    @Autowired
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    protected IBaseDAO<User, Long> getEntityDAO() {
        return userDAO;
    }

    public User findByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    public boolean exist(String username) {
        return userDAO.findByUsername(username) != null;
    }

    public Long getCount() {
        return userDAO.count();
    }

    @Transactional
    public void delete(Long id) {
        // 并不是真正的删除，只是冻结账户
        User user = findById(id);
        if (user == null) return;

        user.setActive(0);
        this.update(user);
    }

    @Transactional
    public void update(UserUpdateReq userUpdateReq) {
        User user = findById(userUpdateReq.getId());
        if (user == null) return;

        ObjectConvertUtil.objectCopy(user, userUpdateReq);
        user.setModifiedTime(new Date());
        this.update(user);
    }

    @Transactional
    public User save(UserSaveReq userSaveReq) {
        if (exist(userSaveReq.getUsername())) {
            return null;
        }
        User user = new User();
        ObjectConvertUtil.objectCopy(user, userSaveReq);
        user.setCreatedTime(new Date());
        user.setModifiedTime(new Date());
        return this.save(user);
    }

    public Page<UserDTO> findAll(UserSearchReq userSearchReq, BasicSearchReq basicSearchReq) {

        int pageNumber = basicSearchReq.getStartIndex() / basicSearchReq.getPageSize() + 1;
        PageRequest pageRequest = super.buildPageRequest(pageNumber, basicSearchReq.getPageSize());

        //除了分页条件没有特定搜索条件的，为了缓存count
        if (ObjectConvertUtil.objectFieldIsBlank(userSearchReq)){
            List<User> userList = userDAO.listAllPaged(pageRequest);
            List<UserDTO> userDTOList = userList.stream().map(source -> {
                UserDTO userDTO = new UserDTO();
                ObjectConvertUtil.objectCopy(userDTO, source);
                userDTO.setActiveStr(ActiveEnum.getDesc(source.getActive()));
                return userDTO;
            }).collect(Collectors.toList());

            //为了计算总数使用缓存，加快搜索速度
            Long count = getCount();
            return new PageImpl<>(userDTOList, pageRequest, count);
        }

        //有了其它搜索条件
        Page<User> userPage = super.findAllBySearchParams(
                buildSearchParams(userSearchReq), pageNumber, basicSearchReq.getPageSize());

        return userPage.map(source -> {
            UserDTO userDTO = new UserDTO();
            ObjectConvertUtil.objectCopy(userDTO, source);
            userDTO.setActiveStr(ActiveEnum.getDesc(source.getActive()));
            return userDTO;
        });

    }

}
