package com.zfy.lifecyclex.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.zfy.lifecyclex.LifecycleMonitor;

/**
 * CreateAt : 2018/7/20
 * Describe :
 *
 * @author chendong
 */
public interface LifecycleXHost {

    void startActivityForResult(Intent intent, int requestCode);

    LifecycleMonitor getLifecycleMonitor();

    void requestPermissions(@NonNull String[] permissions, int requestCode);

    void uninstall();
}