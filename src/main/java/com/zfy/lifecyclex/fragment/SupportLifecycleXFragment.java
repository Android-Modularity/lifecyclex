package com.zfy.lifecyclex.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.zfy.lifecyclex.Embed;
import com.zfy.lifecyclex.LifecycleMonitor;
import com.zfy.lifecyclex.LifecycleXRetriever;

import java.util.HashSet;

/**
 * CreateAt : 2018/7/20
 * Describe :
 *
 * @author chendong
 */
public class SupportLifecycleXFragment extends Fragment implements LifecycleXHost {

    private       Embed            mEmbed;
    private final LifecycleMonitor mLifecycleMonitor;
    private final HashSet<SupportLifecycleXFragment> childRequestManagerFragments =
            new HashSet<>();
    private SupportLifecycleXFragment rootRequestManagerFragment;

    public SupportLifecycleXFragment() {
        this.mLifecycleMonitor = new LifecycleMonitor();
    }

    public Embed getEmbed() {
        return mEmbed;
    }

    public void setEmbed(Embed embed) {
        mEmbed = embed;
        mEmbed.setLifecycleXHost(this);
    }

    @Override
    public LifecycleMonitor getLifecycleMonitor() {
        return mLifecycleMonitor;
    }


    private void addChildRequestManagerFragment(SupportLifecycleXFragment child) {
        childRequestManagerFragments.add(child);
    }

    private void removeChildRequestManagerFragment(SupportLifecycleXFragment child) {
        childRequestManagerFragments.remove(child);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        getLifecycleMonitor().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getLifecycleMonitor().onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        rootRequestManagerFragment = LifecycleXRetriever.get()
                .getSupportLifecycleXFragment(getActivity().getSupportFragmentManager());
        if (rootRequestManagerFragment != this) {
            rootRequestManagerFragment.addChildRequestManagerFragment(this);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (rootRequestManagerFragment != null) {
            rootRequestManagerFragment.removeChildRequestManagerFragment(this);
            rootRequestManagerFragment = null;
        }
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
    public void uninstall() {
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }
}
