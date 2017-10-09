package com.beyond.algo.infra.impl;

/**
 * @author ：zhangchuanzhi
 * @Description:用户登录注册server层
 * @date ：13:26 2017/9/25
 */

import com.beyond.algo.common.*;
import com.beyond.algo.mapper.AlgUserMapper;
import com.beyond.algo.model.AlgUser;

import com.beyond.algo.infra.UserServer;

import com.beyond.algo.model.ProjectConfigEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


import java.util.Date;

@Service
public class UserServerImpl implements UserServer {
    private final static Logger logger = LoggerFactory.getLogger(UserServerImpl.class);
    @Autowired
    private AlgUserMapper algUserMapper;

    @Autowired
    private ProjectConfigEntity projectConfigEntity;
/**
 * @author ：zhangchuanzhi
 * @Description:实现用户注册功能
 * @param：User
 * @Modify By :zhangchuanzhi
 * @date ：13:16 2017/9/25
 */
    @Override
    public Result createUser(AlgUser user){
        String uuid= UUIDUtil.creatUUID();
        user.setUsrSn(uuid);
        user.setUpdateDate(new Date());
        user.setCreateDate(new Date());
        logger.info("密码："+projectConfigEntity.getKeyAES());
        String passWord= AESUtil.encrypt(user.getPasswd(),projectConfigEntity.getKeyAES());
        user.setPasswd(passWord);
        algUserMapper.insert(user);
        return Result.successResponse();
    }
    /**
     * @author ：zhangchuanzhi
     * @Description:实现用户登录
     * @param：User
     * @Modify By :zhangchuanzhi
     * @date ：15:26 2017/9/28
     */
    @Override
    public Result userLogin(AlgUser user) throws Exception{
        String password=user.getPasswd();
        user = algUserMapper.selectUsrname(user.getUsrName());
      // 如果没有这个用户
        if(Assert.isNULL(user)){
              return Result.failureResponse();
        }
      // 对比密码
        String passwordEncryp = AESUtil.decrypt(user.getPasswd(),projectConfigEntity.getKeyAES());
        if(!passwordEncryp.equals(password)){
            return  Result.failureResponse();
        }
     return  Result.successResponse();
    }

}

