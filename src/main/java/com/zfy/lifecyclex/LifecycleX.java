package com.zfy.lifecyclex;

import android.app.Activity;
import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

/**
 * CreateAt : 2018/7/20
 * Describe :
 *
 * @author chendong
 */
public class LifecycleX {

    public static final String FRAGMENT_TAG = "LifecycleX";

    public static Embed with(Activity activity) {
        Embed embed = LifecycleXRetriever.get().get(activity);
        Log.e("chendong", "find embed " + embed.toString());
        return embed;
    }

    public static Embed with(FragmentActivity activity) {
        Embed embed = LifecycleXRetriever.get().get(activity);
        Log.e("chendong", "find embed " + embed.toString());
        return embed;
    }


    public static Embed with(Fragment fragment) {
        return LifecycleXRetriever.get().get(fragment);
    }

    public static Embed with(android.support.v4.app.Fragment fragment) {
        return LifecycleXRetriever.get().get(fragment);
    }

}
