package com.zjrb.core.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

/**
 * DialogFragment 的实现类
 *
 * @author a_liYa
 * @date 2017/9/14 17:39.
 */
public class DialogFragmentImpl extends DialogFragment {

    private int id;
    private boolean recoverable = false; // 默认不可恢复

    private static final String KEY_ID = "key_id";
    private static final String KEY_RECOVERABLE = "key_recoverable";

    public DialogFragmentImpl() {
    }

    /**
     * 当需要创建多个Dialog时 {@link CreateDialog#onCreateDialog(Bundle, int)} 通过id来区分
     *
     * @param id 标志id
     * @return this
     */
    public DialogFragmentImpl setId(int id) {
        this.id = id;
        return this;
    }

    /**
     * 设置被系统销毁时是否支持恢复
     *
     * @param recoverable true:可恢复
     * @return this
     */
    public DialogFragmentImpl setRecoverable(boolean recoverable) {
        this.recoverable = recoverable;
        return this;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) { // 此时为恢复创建
            recoverable = savedInstanceState.getBoolean(KEY_RECOVERABLE, recoverable);
            if (!recoverable) { // 不支持恢复，remove
                dismissAllowingStateLoss();
            }
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            id = savedInstanceState.getInt(KEY_ID, id);
        }

        if (getParentFragment() instanceof CreateDialog) {
            return ((CreateDialog) getParentFragment()).onCreateDialog(savedInstanceState, id);
        }
        if (getContext() instanceof CreateDialog) {
            return ((CreateDialog) getContext()).onCreateDialog(savedInstanceState, id);
        }

        if (getParentFragment() == null) {
            throw new IllegalStateException(getContext().getClass().getSimpleName()
                    + " must be implements " + CreateDialog.class.getName());
        } else {
            throw new IllegalStateException(getContext().getClass().getSimpleName()
                    + " or " + getParentFragment().getClass().getSimpleName()
                    + " must be implements " + CreateDialog.class.getName());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (recoverable) {
            super.onSaveInstanceState(outState);
            outState.putInt(KEY_ID, id);
        }
        outState.putBoolean(KEY_RECOVERABLE, recoverable);
    }

    /**
     * 接口 - 留给DialogFragment的宿主实现
     *
     * @author a_liYa
     * @date 2017/9/14 下午8:19.
     */
    public interface CreateDialog {

        @NonNull
        Dialog onCreateDialog(Bundle savedInstanceState, int id);

    }

}
