package org.jbossoutreach.restandroid.request.edit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;

import org.jbossoutreach.restandroid.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditRequestActivity extends AppCompatActivity {

    public static final String REQUEST_URL = "REQUEST_URL";
    public static final String REQUEST_HEADER_KEY = "REQUEST_HEADER_KEY";
    public static final String REQUEST_BODY = "REQUEST_BODY";
    public static final String REQUEST_METHOD = "REQUEST_METHOD";

    @BindView(R.id.textInputLayout_edit_url)
    TextInputLayout urlLayout;
    @BindView(R.id.textInputLayout_header_key)
    TextInputLayout headerKeyLayout;
    @BindView(R.id.textInputLayout_edit_body)
    TextInputLayout bodyLayout;
    @BindView(R.id.spinner_edit)
    Spinner spinner;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_request_edit);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
            actionBar.setTitle(R.string.title_edit_request);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_request_done:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Intent getParentActivityIntent() {
        final Intent intent = super.getParentActivityIntent();
        if (intent != null) {
            final EditText urlText = urlLayout.getEditText();
            if (urlText != null) {
                intent.putExtra(REQUEST_URL, urlText.getText().toString().trim());
            }

            final EditText bodyText = bodyLayout.getEditText();
            if (bodyText != null) {
                intent.putExtra(REQUEST_BODY, bodyText.getText().toString().trim());
            }

            final EditText headerKeyText = headerKeyLayout.getEditText();
            if (headerKeyText != null) {
                intent.putExtra(REQUEST_HEADER_KEY, headerKeyText.getText().toString().trim());
            }

            intent.putExtra(REQUEST_METHOD, spinner.getSelectedItem().toString());
        }
        return intent;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_edit_request, menu);
        return true;
    }
}
