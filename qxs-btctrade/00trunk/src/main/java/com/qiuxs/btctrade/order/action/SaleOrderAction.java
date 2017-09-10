/*
 * 文件名称: SaleOrderAction.java
 * 版权信息: Copyright 2001-2017 hangzhou ecool technology Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: qiuxs
 * 修改日期: 2017-9-9
 * 修改内容: 
 */
package com.qiuxs.btctrade.order.action;

import com.qiuxs.btctrade.order.entity.SaleOrder;
import com.qiuxs.btctrade.order.service.SaleOrderService;
import com.qiuxs.btctrade.order.dao.SaleOrderDao;
import com.qiuxs.bizfdn.frm.action.EcAction;
import javax.annotation.Resource;
import com.alibaba.dubbo.config.annotation.Service;

/**
 * 卖单表入口
 * @author qiuxs created on 2017-9-9
 * @since 
 */
@Service
public class SaleOrderAction extends EcAction<Long, SaleOrder, SaleOrderDao, SaleOrderService> implements ISaleOrderAction{
    
	@Resource
    private SaleOrderService saleOrderService;
    
    @Override
    protected SaleOrderService getService() {
        return saleOrderService;
    }    

    @Override
    protected Class<SaleOrder> getEntityClass() {
        return SaleOrder.class;
    }

}
