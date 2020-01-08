package bat.ke.qq.com.search.service;


import bat.ke.qq.com.manager.dto.EsInfo;

/**
 * @author 源码学院
 */
public interface SearchItemService {

	/**
	 * 同步索引
	 * @return
	 */
	int importAllItems();

	/**
	 * 获取ES基本信息
	 * @return
	 */
	EsInfo getEsInfo();
}
