package com.zfy.lifecyclex.callback;

import android.content.Intent;

/**
 * CreateAt : 2018/7/20
 * Describe :
 *
 * @author chendong
 */
public interface StartActivityForResultCallback {
    void onActivityResult(int requestCode, int resultCode, Intent data);
}
