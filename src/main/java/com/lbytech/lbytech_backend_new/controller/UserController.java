package com.lbytech.lbytech_backend_new.controller;

import cn.hutool.core.util.StrUtil;
import com.lbytech.lbytech_backend_new.exception.BusinessException;
import com.lbytech.lbytech_backend_new.pojo.Enum.StatusCodeEnum;
import com.lbytech.lbytech_backend_new.pojo.dto.ChangePasswordFrom;
import com.lbytech.lbytech_backend_new.pojo.dto.SendVerifyCodeForm;
import com.lbytech.lbytech_backend_new.pojo.dto.UserLoginFrom;
import com.lbytech.lbytech_backend_new.pojo.dto.UserRegisterFrom;
import com.lbytech.lbytech_backend_new.pojo.vo.BaseResponse;
import com.lbytech.lbytech_backend_new.pojo.vo.UserVO;
import com.lbytech.lbytech_backend_new.service.IUserService;
import com.lbytech.lbytech_backend_new.util.ResultUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Tag(name = "用户模块", description = "用户相关API")    // springdoc描述类
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
    @Operation(summary = "发送验证码", description = "发送验证码到用户邮箱")   // springdoc描述方法
    public BaseResponse<String> sendVerifyCode(
            @Parameter(description = "发送验证码表单")
            @RequestBody SendVerifyCodeForm sendVerifyCodeForm) {
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
            throw new BusinessException(StatusCodeEnum.FAIL, "参数不能为空");
        }
        userService.userRegister(email, password, verifyCode);
        return ResultUtil.success("注册成功");
    }

    /**
     * 用验证码登录
     *
     * @param userLoginFrom
     * @return
     */
    @PostMapping("/loginByVerifyCode")
    public BaseResponse<UserVO> loginByVerifyCode(@RequestBody UserLoginFrom userLoginFrom) {
        String email = userLoginFrom.getEmail();
        String password = userLoginFrom.getVerifyCode();
        UserVO userVO = userService.loginByVerifyCode(email, password);
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
        return ResultUtil.success(userVO);
    }

    /**
     * 更改密码
     *
     * @param changePasswordFrom
     * @return
     */
    @PostMapping("/changePassword")
    public BaseResponse<String> changePassword(@RequestBody ChangePasswordFrom changePasswordFrom) {
        String email = changePasswordFrom.getEmail();
        String verifyCode = changePasswordFrom.getVerifyCode();
        String newPassword = changePasswordFrom.getNewPassword();
        if (StrUtil.hasBlank(email, verifyCode, newPassword)) {
            throw new BusinessException(StatusCodeEnum.FAIL, "参数不能为空");
        }
        userService.changePassword(email, verifyCode, newPassword);
        return ResultUtil.success("密码更改成功");
    }
}
