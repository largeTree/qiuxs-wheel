/*
 * 文件名称: BuyOrderDao.java
 * 版权信息: Copyright 2001-2017 hangzhou ecool technology Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: qiuxs
 * 修改日期: 2017-9-6
 * 修改内容: 
 */
package com.qiuxs.btctrade.order.dao;
import com.qiuxs.frm.dao.IParentDAO;
import com.qiuxs.frm.dao.MyBatisRepository;
import com.qiuxs.btctrade.order.entity.BuyOrder;

/**
 * 买单表Dao接口类
 * 
 * @author qiuxs created on 2017-9-6
 * @since 
 */
 @MyBatisRepository
public interface BuyOrderDao extends IParentDAO<Long, BuyOrder> {

}
