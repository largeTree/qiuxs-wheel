/*
 * 文件名称: BtcMarketService.java
 * 版权信息: Copyright 2001-2017 hangzhou ecool technology Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: qiuxs
 * 修改日期: 2017-9-5
 * 修改内容: 
 */
package com.qiuxs.btctrade.market.service;

import com.qiuxs.btctrade.market.dao.BtcMarketDao;
import com.qiuxs.btctrade.market.entity.BtcMarket;
import com.qiuxs.btctrade.market.service.BtcMarketService;
import com.qiuxs.bizfdn.frm.bean.ViewProperty;
import com.qiuxs.bizfdn.frm.bean.BaseField;
import com.qiuxs.bizfdn.frm.bean.ViewIndex;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import com.qiuxs.frm.service.filter.IServiceFilter;
import com.qiuxs.frm.service.impl.IdServiceFilter;
import org.springframework.stereotype.Service;
import com.qiuxs.bizfdn.frm.service.AbstractEcService;

/**
 * 虚拟币行情表服务实现类
 * @author qiuxs created on 2017-9-5
 * @since 
 */
@Service
public class BtcMarketService extends AbstractEcService<Long, BtcMarket, BtcMarketDao>
	 {
	
	@Resource
	private BtcMarketDao btcMarketDao;
	
	private final static String TABLE_NAME = "btc_market";
    public final static String VIEW_BTCMARKET = "btcMarket";
	
	/**
     * 构造函数
     */
	public BtcMarketService() {
        super();
        this.pojoClass = BtcMarket.class;
        this.tableName = TABLE_NAME;
        this.description = "虚拟币行情表";
        ViewIndex.putService(VIEW_BTCMARKET, this);//, ViewIndex.TOPIC_BASE
    }  
    
    @Override
    public BtcMarketDao getDao() {
        return btcMarketDao;
    }
    
    @Override
    protected void initCreate(BtcMarket btcMarket) {
    	super.initCreate(btcMarket);
    }

	
    @Override
    protected void initServiceFilters(List<IServiceFilter> filters) {    
        filters.add(new IdServiceFilter<Long, BtcMarket>(tableName));//为了主键生成
    }

	// -------------------------------- 以下为增删改查表单元数据配置 -------------------------------- //
    @Override
    protected String[] collectQueryProps() {
        return new String[] {"id"};
    }
    
    @Override
    protected String[] collectInputProps() {
    	return new String[] {"id", "type", "high", "low", "sell", "buy", "last", "vol", "time"};
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
        
		prop = new ViewProperty<Object>(new BaseField("high", "high", "java.math.BigDecimal"), null);
    	props.add(prop);
        
		prop = new ViewProperty<Object>(new BaseField("low", "low", "java.math.BigDecimal"), null);
    	props.add(prop);
        
		prop = new ViewProperty<Object>(new BaseField("sell", "sell", "java.math.BigDecimal"), null);
    	props.add(prop);
        
		prop = new ViewProperty<Object>(new BaseField("buy", "buy", "java.math.BigDecimal"), null);
    	props.add(prop);
        
		prop = new ViewProperty<Object>(new BaseField("last", "last", "java.math.BigDecimal"), null);
    	props.add(prop);
        
		prop = new ViewProperty<Object>(new BaseField("vol", "vol", "java.math.BigDecimal"), null);
    	props.add(prop);
        
		prop = new ViewProperty<Object>(new BaseField("time", "time", "Long"), null);
    	props.add(prop);
        
		prop = new ViewProperty<Object>(new BaseField("createdDate", "createdDate", "Date"), null);
    	props.add(prop);
    }
}
