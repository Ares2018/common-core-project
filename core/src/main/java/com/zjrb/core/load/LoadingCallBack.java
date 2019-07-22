package com.zjrb.core.load;

import com.core.network.callback.ApiCallback;

/**
 * 优化：重命名为 ApiCallback
 * ApiCallback （interface）
 * | - ApiProgressCallback (interface)
 * |
 * | - ApiProCallback (interface)
 * |			| - AbsProCallback (abstract)
 * |
 * | - AbsCallback (abstract)
 * <p>
 * 加载数据回调接口
 *
 * @param <T> 泛型 ：加载成功返回的数据类型
 */
public interface LoadingCallBack<T> extends ApiCallback<T> {
}
