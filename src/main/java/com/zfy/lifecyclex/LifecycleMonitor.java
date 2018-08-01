package com.zfy.lifecyclex;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.zfy.lifecyclex.callback.LifecycleCallback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * CreateAt : 2018/7/20
 * Describe : 持有生命周期监控者
 *
 * @author chendong
 */
public class LifecycleMonitor extends LifecycleCallback {

    private final Set<LifecycleCallback> mListeners =
            Collections.newSetFromMap(new WeakHashMap<LifecycleCallback, Boolean>());

    private boolean isStarted;
    private boolean isDestroyed;

    public void addListener(LifecycleCallback listener) {
        mListeners.add(listener);
        if (isDestroyed) {
            listener.onDestroy();
        } else if (isStarted) {
            listener.onStart();
        } else {
            listener.onStop();
        }
    }

    public void removeListener(LifecycleCallback listener) {
        mListeners.remove(listener);
    }

    @Override
    public void onStart() {
        isStarted = true;
        List<LifecycleCallback> callbacks = getSnapshot(mListeners);
        for (LifecycleCallback lifecycleListener : callbacks) {
            lifecycleListener.onStart();
        }
    }

    @Override
    public void onStop() {
        isStarted = false;
        List<LifecycleCallback> callbacks = getSnapshot(mListeners);
        for (LifecycleCallback lifecycleListener : callbacks) {
            lifecycleListener.onStop();
        }
    }

    @Override
    public void onDestroy() {
        isDestroyed = true;
        List<LifecycleCallback> callbacks = getSnapshot(mListeners);
        for (LifecycleCallback lifecycleListener : callbacks) {
            lifecycleListener.onDestroy();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        List<LifecycleCallback> callbacks = getSnapshot(mListeners);
        for (LifecycleCallback lifecycleListener : callbacks) {
            lifecycleListener.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        List<LifecycleCallback> callbacks = getSnapshot(mListeners);
        for (LifecycleCallback callback : callbacks) {
            callback.onRequestPermissionsResult(requestCode,permissions,grantResults);
        }
    }

    private <T> List<T> getSnapshot(Collection<T> other) {
        List<T> result = new ArrayList<>(other.size());
        result.addAll(other);
        return result;
    }

}
