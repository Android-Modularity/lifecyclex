package com.zfy.lifecyclex.callback;

import android.content.Intent;
import android.support.annotation.NonNull;

/**
 * CreateAt : 2018/7/20
 * Describe : 生命周期
 *
 * @author chendong
 */
public abstract class LifecycleCallback {

    public void onStart() {

    }

    public void onStop() {

    }

    public void onDestroy() {

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    }

}
