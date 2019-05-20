package com.xinge.demo.controller;

import com.xinge.demo.common.util.DozerUtil;
import com.xinge.demo.core.entity.BatchResultDTO;
import com.xinge.demo.core.entity.PageDO;
import com.xinge.demo.core.entity.SuccessResult;
import com.xinge.demo.model.User;
import com.xinge.demo.service.UserService;
import com.xinge.demo.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * @author duanxq
 * Created by duanxq on 2019-05-03.
 */
@Api(tags = "User")
@RestController
@RequestMapping("admin/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private DozerUtil dozerUtil;

    @ApiOperation("添加")
    @PostMapping("add")
    public SuccessResult add(@RequestBody User user) {
        userService.add(user);
        return new SuccessResult();
    }

    @ApiOperation("删除")
    @PostMapping("delete/{id}")
    public SuccessResult delete(@ApiIgnore @PathVariable Integer id) {
        userService.del(id);
        return new SuccessResult();
    }

    @ApiOperation("更新")
    @PostMapping("update")
    public SuccessResult update(@RequestBody User user) {
        userService.editByPK(user);
        return new SuccessResult();
    }

    @ApiOperation("详情")
    @GetMapping("detail/{id}")
    public UserVO detail(@ApiIgnore @PathVariable Integer id) {
        User user = userService.queryByPK(id);
        return dozerUtil.convert(user, UserVO.class);
    }

    @ApiOperation("列表")
    @GetMapping("list")
    public BatchResultDTO<UserVO> list(PageDO pageDO) {
        List<User> users = userService.queryForPageList(new User(), pageDO);
        List<UserVO> userVOList = dozerUtil.convert(users, UserVO.class);
        return BatchResultDTO.of(userVOList);
    }
}
