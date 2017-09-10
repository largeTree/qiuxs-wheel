/*
 * 文件名称: BuyOrderService.java
 * 版权信息: Copyright 2001-2017 hangzhou ecool technology Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: qiuxs
 * 修改日期: 2017-9-6
 * 修改内容: 
 */
package com.qiuxs.btctrade.order.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.qiuxs.bizfdn.frm.bean.BaseField;
import com.qiuxs.bizfdn.frm.bean.ViewIndex;
import com.qiuxs.bizfdn.frm.bean.ViewProperty;
import com.qiuxs.bizfdn.frm.service.AbstractEcService;
import com.qiuxs.btctrade.market.entity.BtcMarket;
import com.qiuxs.btctrade.order.dao.BuyOrderDao;
import com.qiuxs.btctrade.order.entity.BuyOrder;
import com.qiuxs.frm.service.filter.IServiceFilter;
import com.qiuxs.frm.service.impl.IdServiceFilter;

/**
 * 买单表服务实现类
 * @author qiuxs created on 2017-9-6
 * @since 
 */
@Service
public class BuyOrderService extends AbstractEcService<Long, BuyOrder, BuyOrderDao>
{

	@Resource
	private BuyOrderDao buyOrderDao;

	private final static String TABLE_NAME = "btc_buy_order";
	public final static String VIEW_BUYORDER = "buyOrder";

	/**
	 * 构造函数
	 */
	public BuyOrderService() {
		super();
		this.pojoClass = BuyOrder.class;
		this.tableName = TABLE_NAME;
		this.description = "买单表";
		ViewIndex.putService(VIEW_BUYORDER, this);//, ViewIndex.TOPIC_BASE
	}

	/**
	 * 根据当前行情 获取可以出手的买单
	 * @param curMarket
	 * 	当前行情对象
	 * @return
	 */
	public List<BuyOrder> findCanSaleOrders(BtcMarket market) {
		return this.getDao().findCanSaleOrders(market);
	}

	@Override
	public BuyOrderDao getDao() {
		return buyOrderDao;
	}

	@Override
	protected void initCreate(BuyOrder buyOrder) {
		super.initCreate(buyOrder);
	}

	@Override
	protected void initServiceFilters(List<IServiceFilter> filters) {
		filters.add(new IdServiceFilter<Long, BuyOrder>(tableName));//为了主键生成
	}

	// -------------------------------- 以下为增删改查表单元数据配置 -------------------------------- //
	@Override
	protected String[] collectQueryProps() {
		return new String[] { "id" };
	}

	@Override
	protected String[] collectInputProps() {
		return new String[] { "id", "type", "btcOrderId", "price", "num", "money", "btcFee", "salePrice", "finishDate", "cancelDate" };
	}

	@Override
	protected void initQueryProps(Map<String, ViewProperty<?>> queryPropMap, String viewId) {
		super.initQueryProps(queryPropMap, viewId);
	}

	@Override
	protected void initInputProps(Map<String, ViewProperty<?>> inputPropMap, String viewId) {
		super.initInputProps(inputPropMap, viewId);
	}

	@Override
	protected void initPropMapInner(List<ViewProperty<?>> props) {
		ViewProperty<?> prop = null;

		prop = new ViewProperty<Object>(new BaseField("id", "编号", "Long"), null);
		props.add(prop);

		prop = new ViewProperty<Object>(new BaseField("type", "type", "Integer"), null);
		props.add(prop);

		prop = new ViewProperty<Object>(new BaseField("btcOrderId", "btcOrderId", "String"), null);
		props.add(prop);

		prop = new ViewProperty<Object>(new BaseField("price", "price", "java.math.BigDecimal"), null);
		props.add(prop);

		prop = new ViewProperty<Object>(new BaseField("num", "num", "java.math.BigDecimal"), null);
		props.add(prop);

		prop = new ViewProperty<Object>(new BaseField("money", "money", "java.math.BigDecimal"), null);
		props.add(prop);

		prop = new ViewProperty<Object>(new BaseField("btcFee", "btcFee", "java.math.BigDecimal"), null);
		props.add(prop);

		prop = new ViewProperty<Object>(new BaseField("salePrice", "salePrice", "java.math.BigDecimal"), null);
		props.add(prop);

		prop = new ViewProperty<Object>(new BaseField("flag", "flag", "Integer"), null);
		props.add(prop);

		prop = new ViewProperty<Object>(new BaseField("createdDate", "createdDate", "Date"), null);
		props.add(prop);

		prop = new ViewProperty<Object>(new BaseField("finishDate", "finishDate", "Date"), null);
		props.add(prop);

		prop = new ViewProperty<Object>(new BaseField("cancelDate", "cancelDate", "Date"), null);
		props.add(prop);
	}
}
