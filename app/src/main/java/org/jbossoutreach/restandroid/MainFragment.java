package org.jbossoutreach.restandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.BindView;

public abstract class MainFragment extends Fragment {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    protected abstract void onNewIntent(Intent intent);

    protected abstract @StringRes
    int getTitle();

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        toolbar.setTitle(getTitle());
        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
        }
    }
}
