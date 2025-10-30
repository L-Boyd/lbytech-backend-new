package com.lbytech.lbytech_backend_new.controller;

import cn.hutool.core.util.StrUtil;
import com.lbytech.lbytech_backend_new.exception.BusinessException;
import com.lbytech.lbytech_backend_new.pojo.Enum.StatusCodeEnum;
import com.lbytech.lbytech_backend_new.pojo.dto.SendVerifyCodeForm;
import com.lbytech.lbytech_backend_new.pojo.dto.UserLoginFrom;
import com.lbytech.lbytech_backend_new.pojo.dto.UserRegisterFrom;
import com.lbytech.lbytech_backend_new.pojo.vo.BaseResponse;
import com.lbytech.lbytech_backend_new.pojo.vo.UserVO;
import com.lbytech.lbytech_backend_new.service.IUserService;
import com.lbytech.lbytech_backend_new.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    /**
     * 发送验证码
     *
     * @param sendVerifyCodeForm
     * @return
     */
    @PostMapping("/sendVerifyCode")
    public BaseResponse<String> sendVerifyCode(@RequestBody SendVerifyCodeForm sendVerifyCodeForm) {
        String email = sendVerifyCodeForm.getEmail();
        if (StrUtil.isBlank(email)) {
            throw new BusinessException(StatusCodeEnum.FAIL, "邮箱不能为空");
        }
        userService.sendVerifyCode(email);
        return ResultUtil.success("验证码发送成功");
    }

    /**
     * 用户注册
     *
     * @param userRegisterFrom
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<String> userRegister(@RequestBody UserRegisterFrom userRegisterFrom) {
        String email = userRegisterFrom.getEmail();
        String password = userRegisterFrom.getPassword();
        String verifyCode = userRegisterFrom.getVerifyCode();
        if (StrUtil.hasBlank(email, password, verifyCode)) {
            return ResultUtil.fail("参数不能为空");
        }
        boolean result = userService.userRegister(email, password, verifyCode);
        if (!result) {
            return ResultUtil.fail("注册失败");
        }
        return ResultUtil.success("注册成功");
    }

    /**
     * 用验证码登录
     *
     * @param userLoginFrom
     * @return
     */
    @PostMapping("/loginByVerifyCode")
    public BaseResponse<UserVO> login(@RequestBody UserLoginFrom userLoginFrom) {
        String email = userLoginFrom.getEmail();
        String password = userLoginFrom.getVerifyCode();
        UserVO userVO = userService.loginByVerifyCode(email, password);
        if (userVO == null) {
            return ResultUtil.fail(null);
        }
        return ResultUtil.success(userVO);
    }

    /**
     * 用密码登录
     *
     * @param userLoginFrom
     * @return
     */
    @PostMapping("/loginByPassword")
    public BaseResponse<UserVO> loginByPassword(@RequestBody UserLoginFrom userLoginFrom) {
        String email = userLoginFrom.getEmail();
        String password = userLoginFrom.getPassword();
        UserVO userVO = userService.loginByPassword(email, password);
        if (userVO == null) {
            return ResultUtil.fail(null);
        }
        return ResultUtil.success(userVO);

    }
}
