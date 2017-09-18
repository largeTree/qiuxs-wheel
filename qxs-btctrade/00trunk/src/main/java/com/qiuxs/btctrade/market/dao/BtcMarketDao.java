/*
 * 文件名称: BtcMarketDao.java
 * 版权信息: Copyright 2001-2017 hangzhou ecool technology Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: qiuxs
 * 修改日期: 2017-9-5
 * 修改内容: 
 */
package com.qiuxs.btctrade.market.dao;

import java.math.BigDecimal;

import com.qiuxs.frm.dao.IParentDAO;
import com.qiuxs.frm.dao.MyBatisRepository;
import com.qiuxs.btctrade.market.entity.BtcMarket;

/**
 * 虚拟币行情表Dao接口类
 * 
 * @author qiuxs created on 2017-9-5
 * @since 
 */
@MyBatisRepository
public interface BtcMarketDao extends IParentDAO<Long, BtcMarket> {

	/**
	 * 获取平均卖价
	 * @return
	 */
	public BigDecimal calSellAvg();

}
