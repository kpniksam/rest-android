package org.jbossoutreach.restandroid;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import org.jbossoutreach.restandroid.history.HistoryFragment;
import org.jbossoutreach.restandroid.request.RequestFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.bottomNavigationView_main)
    BottomNavigationView navigationView;

    private MainActivityViewModel mViewModel;

    @StringRes
    private int mTitleRes = R.string.app_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        navigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_request:
                    mViewModel.onNavigationItemClicked(
                            MainActivityViewModel.NavigationItem.REQUEST);
                    break;
                case R.id.navigation_history:
                    mViewModel.onNavigationItemClicked(
                            MainActivityViewModel.NavigationItem.HISTORY);
                    break;
            }
            return true;
        });

        mViewModel.navigationLiveData.observe(this, this::showNavigationItem);

//        Example usage of RestClient
//        Call<Response> getRequest = new RestClient().getResponseService().getCall("the url", "Body if the request has one");
//        getRequest.enqueue(new Callback<Response>() {
//            @Override
//            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
//
//            }
//
//            @Override
//            public void onFailure(Call<Response> call, Throwable t) {
//
//            }
//        });
    }

    private void showNavigationItem(MainActivityViewModel.NavigationItem item) {
        if (item == null) {
            return;
        }

        Fragment newFragment = null;
        switch (item) {
            case REQUEST:
                newFragment = new RequestFragment();
                break;
            case HISTORY:
                newFragment = new HistoryFragment();
                break;
        }

        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.constraintLayout_main, newFragment)
                .commit();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        final Fragment fragment = getSupportFragmentManager()
                .findFragmentById(R.id.constraintLayout_main);
        if (fragment instanceof MainFragment) {
            ((MainFragment) fragment).onNewIntent(intent);
        }
    }
}
