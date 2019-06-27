package com.xinge.demo.core.shiro;

import com.xinge.demo.common.constant.StringConstant;
import com.xinge.demo.model.User;
import com.xinge.demo.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author duanxq
 * created by duanxq on 2019/3/11
 */
public class MyShiroRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        Object attribute = principals.getPrimaryPrincipal();
        if (attribute instanceof User) {
            authorizationInfo.addRole(StringConstant.LOGIN_TYP_ADMIN);
            return authorizationInfo;
        }
        attribute = SecurityUtils.getSubject().getSession().getAttribute(StringConstant.SESSION_MEMBER);
        if (attribute instanceof User) {
            authorizationInfo.addRole(StringConstant.LOGIN_TYP_WEB);
            return authorizationInfo;
        }
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
        if (!(token instanceof MyToken)) {
            throw new UnsupportedOperationException();
        }
        MyToken myToken = (MyToken) token;
        String username = myToken.getUsername();
        String loginType = myToken.getLoginType();
        if (StringConstant.LOGIN_TYP_ADMIN.equals(loginType)) {
            User user = userService.queryByName(username);
            if (user == null) {
                throw new UnknownAccountException();
            }
            return new SimpleAuthenticationInfo(user, user.getPassword(), ByteSource.Util.bytes(user.getSalt()), getName());
        }
        return null;
    }

    @Override
    protected Object getAuthenticationCacheKey(AuthenticationToken token) {
        if (!(token instanceof MyToken)) {
            throw new UnsupportedOperationException();
        }
        MyToken myToken = (MyToken) token;
        return myToken.getLoginType() + "_" + myToken.getUsername();
    }

}
