package com.xinge.demo.service;

import com.xinge.demo.model.User;


/**
 * @author duanxq
 * Created by duanxq on 2019-05-03.
 */
public interface UserService extends IService<User> {

    /**
     * 根据用户名查询用户
     * @param userName
     * @return
     */
    User queryByName(String userName);

}
