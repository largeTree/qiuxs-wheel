/*
 * 文件名称: BuyOrderAction.java
 * 版权信息: Copyright 2001-2017 hangzhou ecool technology Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: qiuxs
 * 修改日期: 2017-9-6
 * 修改内容: 
 */
package com.qiuxs.btctrade.order.action;

import com.qiuxs.btctrade.order.entity.BuyOrder;
import com.qiuxs.btctrade.order.service.BuyOrderService;
import com.qiuxs.btctrade.order.dao.BuyOrderDao;
import com.qiuxs.bizfdn.frm.action.EcAction;
import javax.annotation.Resource;
import com.alibaba.dubbo.config.annotation.Service;

/**
 * 买单表入口
 * @author qiuxs created on 2017-9-6
 * @since 
 */
@Service
public class BuyOrderAction extends EcAction<Long, BuyOrder, BuyOrderDao, BuyOrderService> implements IBuyOrderAction{
    
	@Resource
    private BuyOrderService buyOrderService;
    
    @Override
    protected BuyOrderService getService() {
        return buyOrderService;
    }    

    @Override
    protected Class<BuyOrder> getEntityClass() {
        return BuyOrder.class;
    }

}
