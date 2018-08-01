package com.zfy.lifecyclex.callback;

import android.support.annotation.NonNull;

/**
 * CreateAt : 2018/7/20
 * Describe : 权限请求返回
 *
 * @author chendong
 */
public interface RequestPermissionsCallback {
    void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);

}
