package com.zfy.lifecyclex;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.zfy.lifecyclex.fragment.AppLifecycleXFragment;
import com.zfy.lifecyclex.fragment.SupportLifecycleXFragment;

import java.util.HashMap;
import java.util.Map;

import static com.zfy.lifecyclex.LifecycleX.FRAGMENT_TAG;

public class LifecycleXRetriever implements Handler.Callback {

    private static final String TAG = "LifecycleXRetriever";

    private static final LifecycleXRetriever INSTANCE = new LifecycleXRetriever();

    private static final int MSG_RM_APP_FRAGMENT     = 1;
    private static final int MSG_RM_SUPPORT_FRAGMENT = 2;

    private final Map<android.app.FragmentManager, AppLifecycleXFragment> mPendingAppFragments;
    private final Map<FragmentManager, SupportLifecycleXFragment>         mPendingSupportFragments;
    private final Handler                                                 mFragmentHandler;

    public static LifecycleXRetriever get() {
        return INSTANCE;
    }

    private LifecycleXRetriever() {
        mPendingAppFragments = new HashMap<>();
        mPendingSupportFragments = new HashMap<>();
        mFragmentHandler = new Handler(Looper.getMainLooper(), this /* Callback */);
    }

    // from FragmentActivity
    public Embed get(FragmentActivity activity) {
        assertNotDestroyed(activity);
        FragmentManager fm = activity.getSupportFragmentManager();
        return supportFragmentGet(activity, fm);
    }

    // from v4.fragment
    public Embed get(Fragment fragment) {
        if (fragment.getActivity() == null) {
            throw new IllegalArgumentException("fragment not attach");
        }
        FragmentManager fm = fragment.getChildFragmentManager();
        return supportFragmentGet(fragment.getActivity(), fm);
    }

    // from Activity
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public Embed get(Activity activity) {
        assertNotDestroyed(activity);
        android.app.FragmentManager fm = activity.getFragmentManager();
        return fragmentGet(activity, fm);
    }

    // from app.fragment
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public Embed get(android.app.Fragment fragment) {
        if (fragment.getActivity() == null) {
            throw new IllegalArgumentException("fragment not attach");
        }
        android.app.FragmentManager fm = fragment.getChildFragmentManager();
        return fragmentGet(fragment.getActivity(), fm);
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static void assertNotDestroyed(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed()) {
            throw new IllegalArgumentException("activity is destroyed");
        }
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private Embed fragmentGet(Context context, android.app.FragmentManager fm) {
        AppLifecycleXFragment current = getLifecycleXFragment(fm);
        Embed parasiteManager = current.getEmbed();
        if (parasiteManager == null) {
            parasiteManager = new Embed();
            current.setEmbed(parasiteManager);
        }
        return parasiteManager;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private AppLifecycleXFragment getLifecycleXFragment(final android.app.FragmentManager fm) {
        AppLifecycleXFragment current = (AppLifecycleXFragment) fm.findFragmentByTag(FRAGMENT_TAG);
        if (current == null) {
            current = mPendingAppFragments.get(fm);
            if (current == null) {
                current = new AppLifecycleXFragment();
                mPendingAppFragments.put(fm, current);
                fm.beginTransaction().add(current, FRAGMENT_TAG).commitAllowingStateLoss();
                mFragmentHandler.obtainMessage(MSG_RM_APP_FRAGMENT, fm).sendToTarget();
            }
        }
        return current;
    }

    private Embed supportFragmentGet(Context context, FragmentManager fm) {
        SupportLifecycleXFragment current = getSupportLifecycleXFragment(fm);
        Embed parasiteManager = current.getEmbed();
        if (parasiteManager == null) {
            parasiteManager = new Embed();
            current.setEmbed(parasiteManager);
        }
        return parasiteManager;
    }


    public SupportLifecycleXFragment getSupportLifecycleXFragment(final FragmentManager fm) {
        SupportLifecycleXFragment current = (SupportLifecycleXFragment) fm.findFragmentByTag(FRAGMENT_TAG);
        if (current == null) {
            current = mPendingSupportFragments.get(fm);
            if (current == null) {
                current = new SupportLifecycleXFragment();
                mPendingSupportFragments.put(fm, current);
                fm.beginTransaction().add(current, FRAGMENT_TAG).commitNowAllowingStateLoss();
                mFragmentHandler.obtainMessage(MSG_RM_SUPPORT_FRAGMENT, fm).sendToTarget();
            }
        }
        return current;
    }


    @Override
    public boolean handleMessage(Message message) {
        boolean handled = true;
        Object removed = null;
        Object key = null;
        switch (message.what) {
            case MSG_RM_APP_FRAGMENT:
                android.app.FragmentManager fm = (android.app.FragmentManager) message.obj;
                key = fm;
                removed = mPendingAppFragments.remove(fm);
                break;
            case MSG_RM_SUPPORT_FRAGMENT:
                FragmentManager supportFm = (FragmentManager) message.obj;
                key = supportFm;
                removed = mPendingSupportFragments.remove(supportFm);
                break;
            default:
                handled = false;
        }
        if (handled && removed == null && Log.isLoggable(TAG, Log.WARN)) {
            Log.w(TAG, "Failed to remove expected request manager fragment, manager: " + key);
        }
        return handled;
    }
}