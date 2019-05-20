package com.xinge.demo.service.impl;

import com.xinge.demo.model.User;
import com.xinge.demo.service.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author duanxq
 * Created by duanxq on 2019-05-03.
 */
@Service
public class UserServiceImpl extends BaseService<User> implements UserService {


    @Override
    public User queryByName(String userName) {
        if (StringUtils.isEmpty(userName)) {
            return null;
        }
        User user = new User();
        user.setName(userName);
        List<User> users = queryForList(user);
        if (CollectionUtils.isNotEmpty(users)) {
            return users.get(0);
        }
        return null;
    }
}
