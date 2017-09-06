package com.qiuxs.btctrade.context.lifecycle;

import com.qiuxs.btctrade.context.BtcContextManager;
import com.qiuxs.frm.lc.IEcWebLifecycle;

/**
 * 启动关闭生命周期
 * @author qiuxs
 *
 */
public class BtcInitLifecycle implements IEcWebLifecycle {

	@Override
	public void firstInit() throws Exception {
	}

	@Override
	public void middleInit() throws Exception {
	}

	@Override
	public void lastInit() throws Exception {
		BtcContextManager.init();
	}

	@Override
	public void firstDestroy() throws Exception {
	}

	@Override
	public void middleDestroy() throws Exception {
	}

	@Override
	public void lastDestroy() throws Exception {
	}

}
