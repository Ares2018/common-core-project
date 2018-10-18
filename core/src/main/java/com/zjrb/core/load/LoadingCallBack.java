package com.zjrb.core.load;

/**
 * 优化：重命名为 ApiCallback
 * ApiCallback （interface）
 * 		| - ApiProgressCallback (interface)
 *		|
 * 		| - ApiProCallback (interface)
 * 		|			| - AbsProCallback (abstract)
 *		|
 * 		| - AbsCallback (abstract)
 *
 * 加载数据回调接口
 *
 * @param <T> 泛型 ：加载成功返回的数据类型
 */
public interface LoadingCallBack<T> {
	
	/**
	 * 取消
	 */
	void onCancel();
	
	/**
	 * 加载失败
	 */
	void onError(String errMsg, int errCode);
	
	/**
	 * 加载成功
	 * @param data 从网络获取的数据
	 */
	void onSuccess(T data);

}
