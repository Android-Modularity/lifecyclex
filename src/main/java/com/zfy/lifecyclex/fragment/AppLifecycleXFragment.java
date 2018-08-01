package com.zfy.lifecyclex.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.zfy.lifecyclex.Embed;
import com.zfy.lifecyclex.LifecycleMonitor;


/**
 * CreateAt : 2018/7/20
 * Describe : app.fragment hook
 *
 * @author chendong
 */
public class AppLifecycleXFragment extends Fragment implements LifecycleXHost {

    private LifecycleMonitor mLifecycleMonitor;
    private Embed            mEmbed;

    public AppLifecycleXFragment() {
        this.mLifecycleMonitor = new LifecycleMonitor();
    }

    public Embed getEmbed() {
        return mEmbed;
    }

    public void setEmbed(Embed embed) {
        mEmbed = embed;
        mEmbed.setLifecycleXHost(this);
    }

    public LifecycleMonitor getLifecycleMonitor() {
        return mLifecycleMonitor;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onStart() {
        super.onStart();
        mLifecycleMonitor.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mLifecycleMonitor.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLifecycleMonitor.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mLifecycleMonitor.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mLifecycleMonitor.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void uninstall() {
        getActivity().getFragmentManager()
                .beginTransaction()
                .remove(this)
                .commitAllowingStateLoss();
    }
}
