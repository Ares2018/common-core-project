package com.zjrb.core.load;


/**
 * Created by lixinke on 2017/11/8.
 */

public interface ILoad {
    void finishLoad();

    void showFailed(int errCode);
}
