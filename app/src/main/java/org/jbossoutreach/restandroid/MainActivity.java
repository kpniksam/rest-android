package org.jbossoutreach.restandroid;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.jbossoutreach.restandroid.history.HistoryFragment;
import org.jbossoutreach.restandroid.request.RequestFragment;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.bottomNavigationView_main)
    BottomNavigationView navigationView;

    private MainActivityViewModel mViewModel;

    @StringRes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);


        disableShiftMode(navigationView);

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



        /*

        Example usage of RestClient
        Call<Response> getRequest = new RestClient().getResponseService().getCall("the url", "Body if the request has one");
        getRequest.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {

            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {

            }
        });

        */
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

    public static void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                //noinspection RestrictedApi
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                //noinspection RestrictedApi
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Log.e("BNVHelper", "Unable to get shift mode field", e);
        } catch (IllegalAccessException e) {
            Log.e("BNVHelper", "Unable to change value of shift mode", e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.info_dialog,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle(getString(R.string.info_dialog_title))
                .setMessage(getString(R.string.info_dialog_message))
                .setPositiveButton("Ok", null);
        int id = item.getItemId();
        switch (id){
            case R.id.item_menu_info:
                builder.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}

