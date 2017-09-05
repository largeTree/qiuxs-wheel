/*
 * 文件名称: BtcMarketAction.java
 * 版权信息: Copyright 2001-2017 hangzhou ecool technology Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: qiuxs
 * 修改日期: 2017-9-5
 * 修改内容: 
 */
package com.qiuxs.btctrade.market.action;

import com.qiuxs.btctrade.market.entity.BtcMarket;
import com.qiuxs.btctrade.market.service.BtcMarketService;
import com.qiuxs.btctrade.market.dao.BtcMarketDao;
import com.qiuxs.bizfdn.frm.action.EcAction;
import javax.annotation.Resource;
import com.alibaba.dubbo.config.annotation.Service;

/**
 * 虚拟币行情表入口
 * @author qiuxs created on 2017-9-5
 * @since 
 */
@Service
public class BtcMarketAction extends EcAction<Long, BtcMarket, BtcMarketDao, BtcMarketService> implements IBtcMarketAction{
    
	@Resource
    private BtcMarketService btcMarketService;
    
    @Override
    protected BtcMarketService getService() {
        return btcMarketService;
    }    

    @Override
    protected Class<BtcMarket> getEntityClass() {
        return BtcMarket.class;
    }

}
