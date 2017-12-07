package com.beyond.algm.algmalgorithmsboot.controller;

import com.beyond.algm.algmalgorithmsboot.base.BaseController;
import com.beyond.algm.algmalgorithmsboot.infra.CephService;
import com.beyond.algm.algmalgorithmsboot.model.ProjectConfigEntity;
import com.beyond.algm.common.Assert;
import com.beyond.algm.common.Result;
import com.beyond.algm.algmalgorithmsboot.infra.UserService;
import com.beyond.algm.exception.AlgException;
import com.beyond.algm.model.AlgUser;
import com.beyond.algm.vo.UserAccountVo;
import com.beyond.algm.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author ：zhangchuanzhi
 * @Description:实现用户登录注册功能
 * @date ：13:16 2017/9/25
 */
@Slf4j
@RestController
public class UserController  extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private ProjectConfigEntity projectConfigEntity;
    @Autowired
    private CephService cephService;
    /**
     * @author ：zhangchuanzhi
     * @Description:实现用户注册功能
     * @param：User
     * @Modify By :zhangchuanzhi
     * @date ：13:16 2017/9/25
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result register(AlgUser user) throws AlgException,Exception {
        log.info("用户编码:{},用户密码:{},用户邮箱:{}", user.getUsrCode(), user.getPasswd(), user.getEmail());
        userService.createUser(user);
        return Result.successResponse();
    }

    /**
     * @author ：zhangchuanzhi
     * @Description:用户登录处理
     * @param：User
     * @Modify By :zhangchuanzhi
     * @date ：8:49 2017/9/27
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result userLogin(AlgUser user)throws AlgException,Exception {
        log.info("用户编码:{},用户密码:{}", user.getUsrCode(), user.getPasswd());
        userService.userLogin(user);
        return  Result.successResponse();

    }

    /**
     * @author ：zhangchuanzhi
     * @Description:用户修改密码
     * @param：User
     * @Modify By :zhangchuanzhi
     * @date ：8:49 2017/9/27
     */
    @RequestMapping(value = "/changePassword", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result changePassword(UserVo userVo) throws AlgException,Exception{
        AlgUser algUser = getUserInfo();
        userVo.setUsrCode(algUser.getUsrCode());
    //    userVo.setUsrCode("zhang1");
        log.info("用户姓名:{},用户密码:{},用户确认密码:{},用户新密码:{}", userVo.getUsrCode(), userVo.getPasswd(), userVo.getConfirmPassword(), userVo.getNewPassword());
        userService.changePassword(userVo);
        return Result.successResponse();
    }

    /**
     * @author ：zhangchuanzhi
     * @Description:用户更新信息
     * @param：User
     * @Modify By :zhangchuanzhi
     * @date ：9:14 2017/10/10
     */
    @RequestMapping(value = "/updateUserInformation", method = RequestMethod.PUT,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result updateUserInformation(AlgUser user)throws AlgException {
//        AlgUser algUser = getUserInfo();
//        user.setUsrCode(algUser.getUsrCode());
        log.info("用户全名:{},用户编码:{},用户邮箱:{},用户电话:{},用户主页{},用户是短信还是邮箱发送{},用户唯一主键", user.getUsrName(), user.getUsrCode(), user.getEmail(), user.getTelephone(), user.getUsrUrl(), user.getNeedNotify(), user.getUsrSn());
        userService.updateUserInformation(user);
        return Result.successResponse();

    }

    /**
     * @author ：zhangchuanzhi
     * @Description:用户账户信息
     * @param：accSn
     * @Modify By :zhangchuanzhi
     * @date ：9:14 2017/10/10
     */
    @RequestMapping(value = "/accountInformation", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result accountInformation() throws AlgException{
        AlgUser algUser = getUserInfo();
        log.info("用户名:{},用户串号：{}", algUser.getUsrCode(),algUser.getUsrSn());
      //  AlgUser algUser =new AlgUser();
      //  algUser.setUsrSn("9c371a86c6e5439097de4b20024479f3");
        UserAccountVo algAccount = null;
        algAccount = userService.accountInformation(algUser.getUsrSn());
        return Result.ok(algAccount);
    }

    /**
     * @author ：zhangchuanzhi
     * @Description:用户个人信息查询
     * @param：accSn
     * @Modify By :zhangchuanzhi
     * @date ：9:14 2017/11/01
     */
    @RequestMapping(value = "/getUserInformation", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result getUserInformation() throws AlgException{
        AlgUser algUser = getUserInfo();
        log.info("用户名字:{}", algUser.getUsrCode());
        return Result.ok(algUser);
    }

    /**
     * @author ：zhangchuanzhi
     * @Description:用户登录信息
     * @param：accSn
     * @Modify By :zhangchuanzhi
     * @date ：9:14 2017/11/24
     */
    @RequestMapping(value = "/getUserLogInfor", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result getUserLogInfor() throws AlgException{
        AlgUser algUser = getUserInfo();
        if(Assert.isNotNULL(algUser)){
            AlgUser user = new AlgUser();
            user.setUsrCode(algUser.getUsrCode());
            user.setUsrSn(algUser.getUsrSn());
            return Result.ok(user);
        }else{
            return Result.ok(0);
        }
    }
    /**
     * @author ：zhangchuanzhi
     * @Description:个人用户图片上传
     * @param：accSn
     * @Modify By :zhangchuanzhi
     * @date ：9:14 2017/11/24
     */
    @RequestMapping(value = "/upLoadImage", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result uploadImage(MultipartFile file) throws AlgException{
        AlgUser algUser = getUserInfo();
        cephService.upload(file,algUser.getUsrCode());
        return Result.successResponse();
    }

}
