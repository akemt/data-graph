package com.beyond.algo.algoalgorithmsboot.infra;

import com.beyond.algo.exception.AlgException;
import com.beyond.algo.model.*;
import com.beyond.algo.vo.PayRecordVo;
import com.beyond.algo.vo.PayVo;

import java.util.List;


/**
 * @author ：zhangchuanzhi
 * @Description:接口定义
 * @date ：13:32 2017/9/25
 */
public interface PayCashService {
      List<AlgCashTrans> payRecord(PayRecordVo payRecordVo)throws AlgException;
      void buyIntegral(PayVo payVo)throws AlgException;
}