package com.beyond.algm.algmalgorithmsboot.infra;

import com.beyond.algm.exception.AlgException;

/**
 * @Author: qihe
 * @Description:
 * @Date: create in 2017/11/30 14:27
 */
public interface PathService {

    /**
     * 获取当前用户项目为modId的服务器项目根路径
     * @param usrCode ;当
     * @param modId
     * @return
     * @throws Exception
     * @author xialf
     */
    String getModuleBasePath(String usrCode, String modId) throws AlgException ;

    /**
     *获取当前用户项目为modId的服务器项目根路径
     * @param orgCode 是组织 传入 组织code 不是组织 为null
     * @param modId 算法Id
     * @param usrCode 用户Id
     * @param isOrg 组织-1 用户-0
     * @return
     * @throws AlgException
     */
    String getModuleBasePath(String orgCode, String modId,String usrCode,String isOrg) throws AlgException ;

    /**
     * 返回项目主文件目录 例：E:\repo\erniu4\TestJavaK1\src\algmarket\TestJavaK1
     * @param basePath
     * @param modId
     * @param lanSn
     * @return
     * @throws AlgException
     * 当组织时,则usrCode-\组织编号(名称) \项目名称(编号);modId-用户编号！
     *                当用户时,则usrCode-用户编号;modId-项目名称(编号) ！
     */
    String getModuleMainFilePath(String basePath, String modId, String lanSn) throws AlgException;

    /**
     * 返回Spring Boot发布项目的用户级别目录 例：E:\alg-publish\gaohaijun
     * @param usrCode
     * @param modId
     * @return
     */
    String getPublishPath(String usrCode, String modId);

    /**
     * 获取当前用户项目为modId的服务器项目根路径
     *
     * @param orgUsrCode 组织编号
     * @param modId      项目编号
     * @return
     * @throws Exception
     * @author xialf
     * 当组织时, 则usrCode-\组织编号(名称) \项目名称(编号)
     */
    String getOrgAlgBasePath(String orgUsrCode, String modId) throws AlgException;

    /**
     * 初始化项目基础目录 例：E:\repo\erniu4\TestJavaK1
     *
     * @param stringBuilder
     * @param basePath
     * @param usrCode
     * @param modId
     * @return
     */
    StringBuilder initBaseFolder(StringBuilder stringBuilder, String basePath, String usrCode, String modId);

}
