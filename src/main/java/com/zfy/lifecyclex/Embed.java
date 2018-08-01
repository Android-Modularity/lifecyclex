package com.zfy.lifecyclex;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.zfy.lifecyclex.callback.LifecycleCallback;
import com.zfy.lifecyclex.callback.RequestPermissionsCallback;
import com.zfy.lifecyclex.callback.StartActivityForResultCallback;
import com.zfy.lifecyclex.fragment.LifecycleXHost;


/**
 * CreateAt : 2018/7/20
 * Describe :
 *
 * @author chendong
 */
public class Embed {

    private LifecycleXHost mLifecycleXHost;

    public void setLifecycleXHost(LifecycleXHost lifecycleXHost) {
        mLifecycleXHost = lifecycleXHost;
    }

    /**
     * 打开界面并接受数据
     *
     * @param intent      intent
     * @param requestCode code
     * @param callback    callback
     */
    public void startActivityForResult(final Intent intent, final int requestCode, final StartActivityForResultCallback callback) {
        execute(new Executable() {
            @Override
            public void execute(LifecycleXHost fragment) {
                fragment.startActivityForResult(intent, requestCode);
            }
        }, new LifecycleCallback() {
            @Override
            public void onActivityResult(int requestCodeBack, int resultCode, Intent data) {
                if (requestCodeBack == requestCode) {
                    callback.onActivityResult(requestCodeBack, resultCode, data);
                    mLifecycleXHost.getLifecycleMonitor().removeListener(this);
                }
            }
        });
    }

    /**
     * 请求权限并返回数据
     *
     * @param permissions 权限列表
     * @param requestCode code
     * @param callback    callback
     */
    public void requestPermissions(@NonNull final String[] permissions, final int requestCode, final RequestPermissionsCallback callback) {
        execute(new Executable() {
            @Override
            public void execute(LifecycleXHost fragment) {
                fragment.requestPermissions(permissions, requestCode);
            }
        }, new LifecycleCallback() {
            @Override
            public void onRequestPermissionsResult(int requestCodeBack, @NonNull String[] permissions, @NonNull int[] grantResults) {
                if (requestCodeBack == requestCode) {
                    callback.onRequestPermissionsResult(requestCodeBack, permissions, grantResults);
                    mLifecycleXHost.getLifecycleMonitor().removeListener(this);
                }
            }
        });
    }

    /**
     * 执行通用操作，并 hook 声明周期
     *
     * @param executable 可执行的操作
     * @param listener listener
     */
    public void execute(Executable executable, LifecycleCallback listener) {
        mLifecycleXHost.getLifecycleMonitor().addListener(listener);
        if (executable != null) {
            executable.execute(mLifecycleXHost);
        }
    }
}
