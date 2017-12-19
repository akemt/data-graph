package com.beyond.algm.algmdataboot.infra.impl;


import com.beyond.algm.algmdataboot.infra.ModelSetService;
import com.beyond.algm.common.Assert;
import com.beyond.algm.common.Result;
import com.beyond.algm.common.UUIDUtil;
import com.beyond.algm.exception.AlgException;
import com.beyond.algm.mapper.AlgModelMapper;
import com.beyond.algm.mapper.AlgModelSetMapper;
import com.beyond.algm.mapper.AlgUserMapper;
import com.beyond.algm.model.AlgModel;
import com.beyond.algm.model.AlgModelSet;
import com.beyond.algm.model.AlgUser;
import com.beyond.algm.vo.AlgModelSetVo;
import com.beyond.algm.vo.ModelDataVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author huangjinqing
 * @version Created in：20:22 2017/10/17
 * @Description:数据管理模型模块service接口实现
 */

@Service
@Slf4j
public class ModelSetServiceImpl implements ModelSetService {
    @Autowired
    private AlgModelSetMapper algModelSetMapper;

    @Autowired
    private AlgModelMapper algModelMapper;

    @Autowired
    private AlgUserMapper algUserMapper;

    @Override
    public void addModelSet(AlgModelSet modelSet) throws AlgException {
            //生成模型集随机串
            if (modelSet.getModelSetName().isEmpty()) {
                throw new AlgException("BEYOND.ALG.MODEL.COMMON.VALID.0000001");
            }
            int count= algModelSetMapper.checkData(modelSet);
            if(count>0){
                String[] checkMessage = {"模型集",""};
                throw new AlgException("BEYOND.ALG.MODEL.COMMON.VALID.0000002",checkMessage);
            }
            String maxOrderby =algModelSetMapper.checkMaxOrderby(modelSet.getUsrSn());
            if(Assert.isNotEmpty(maxOrderby)){
                maxOrderby=String.valueOf(Integer.valueOf(maxOrderby)+1);
            }else{
                maxOrderby="1";
            }
            modelSet.setModelOrderby(maxOrderby);
            modelSet.setModelSetSn(UUIDUtil.createUUID());
            algModelSetMapper.insert(modelSet);
    }

    @Override
    public void deleteModelSet(AlgModel algModel) throws AlgException {
            int count= algModelMapper.checkData(algModel);
            if(count==0) {
                algModelSetMapper.deleteByPrimaryKey(algModel);
            }else{
                String[] checkMessage = {"模型集下存在数据，不允许删除",""};
                throw new AlgException("BEYOND.ALG.MODEL.STATUS.0000015",checkMessage);
            }

    }

    @Override
    public Result addAlgModel(AlgModel algModel,String usrCode) throws AlgException {
        try {
            if (Assert.isEmpty(algModel.getModelName())) {
              throw new Exception("BEYOND.ALG.DATA.PAY.STATUS.0000011");
            }
            if (Assert.isNotEmpty(algModel.getModelEnName()) && algModelMapper.checkModelEnName(algModel.getUsrSn(),algModel.getModelEnName()) != 0 ){
                throw new Exception("BEYOND.ALG.DATA.PAY.STATUS.0000012");
            }
            // new Date()为获取当前系统时间
            algModel.setCreateTime(new Date());
            //数据地址
  /*          String modelPath = null;
            if(Assert.isEmpty(algModel.getModelEnName())){
                modelPath = "model://" + usrCode + "/" + algModel.getModelSn();
            }else {
                modelPath = "model://" + usrCode + "/" + algModel.getModelEnName();
            }
            algModel.setModelAddress(modelPath);*/
            algModelMapper.updateMode(algModel);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure("添加模型失败");
        }

        return Result.successResponse();
    }

    @Override
    public int deleteModel( AlgModel algModel ) throws AlgException {
           int count= algModelMapper.deleteModel(algModel);
           return count;
    }

    @Override
    public Result deleteModelByModelSetSn(String modelSetSn) throws Exception {
        try {
            if (modelSetSn.isEmpty()) {
                return Result.failure("模型串号为空");
            }
            algModelMapper.deleteByModelSetSn(modelSetSn);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failureResponse();
        }
        return Result.successResponse();
    }

    @Override
    public Result modifyAlgModel(AlgModel algModel) throws Exception {
        try {
            if (algModel.getModelSn().isEmpty()) {
                return Result.failure("模型串号为空！");
            }
            algModelMapper.updateByPrimaryKey(algModel);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure("修改模型失败");
        }
        return Result.successResponse();
    }

    @Override
    public  PageInfo<AlgModelSetVo> queryAlgModelSet(String usrSn, PageInfo pageInfo) throws AlgException {
        PageHelper.startPage(pageInfo.getPageNum(), pageInfo.getPageSize());
        Page<AlgModelSetVo> algModelSetVoList = algModelSetMapper.queryModelSet(usrSn);
        if(Assert.isNotEmpty(algModelSetVoList)){
            String modelSetSn= algModelSetVoList.get(0).getModelSetSn();
            AlgModel algModel=new AlgModel();
            algModel.setUsrSn(usrSn);
            algModel.setModelSetSn(modelSetSn);
            List<ModelDataVo> algModelList=algModelMapper.queryModel(algModel);
            algModelSetVoList.get(0).setAlgModelVolist(algModelList);
            return new PageInfo<>(algModelSetVoList);
        }
        return null;
    }

    @Override
    public PageInfo<ModelDataVo> queryAlgModel( AlgModel algModel, PageInfo pageInfo) throws AlgException {
        PageHelper.startPage(pageInfo.getPageNum(), pageInfo.getPageSize());
        Page<ModelDataVo> allAlgModel = algModelMapper.queryModelPage(algModel);
        return new PageInfo<>(allAlgModel);
    }
    @Override
    public AlgUser findByUsrCode(String usrCode){

        return algUserMapper.selectUsrCode(usrCode);
    }
    @Override
    public PageInfo<ModelDataVo> queryModelDataSet(ModelDataVo modelDataVo,PageInfo pageInfo)throws AlgException{
        //分页处理
        PageHelper.startPage(pageInfo.getPageNum(), pageInfo.getPageSize());
        Page<ModelDataVo> modelDataVoList = algModelMapper.queryModelDataSet(modelDataVo);
        return new PageInfo<>(modelDataVoList);
    }
}
