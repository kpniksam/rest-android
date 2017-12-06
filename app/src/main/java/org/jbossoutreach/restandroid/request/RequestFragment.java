package org.jbossoutreach.restandroid.request;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.jbossoutreach.restandroid.MainFragment;
import org.jbossoutreach.restandroid.R;
import org.jbossoutreach.restandroid.request.details.ResponseDetailsActivity;
import org.jbossoutreach.restandroid.request.edit.EditRequestActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class RequestFragment extends MainFragment {

    private final Handler mHandler = new Handler();
    @BindView(R.id.cardView_response)
    CardView responseCard;
    @BindView(R.id.progressBar_request)
    ProgressBar progressBar;
    @BindView(R.id.textView_request_body_description)
    TextView bodyText;
    @BindView(R.id.textView_request_headers_description)
    TextView headersText;
    @BindView(R.id.textView_request_url_description)
    TextView urlText;
    @BindView(R.id.textView_request_method)
    TextView methodText;
    private Unbinder mUnbinder;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_request, container, false);

        mUnbinder = ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mUnbinder.unbind();
        mHandler.removeCallbacksAndMessages(null);
    }

    @OnClick(R.id.button_request_edit)
    public void editRequest() {
        startActivity(new Intent(getContext(), EditRequestActivity.class));
    }

    @OnClick(R.id.button_response_details)
    public void viewResponseDetails() {
        startActivity(new Intent(getContext(), ResponseDetailsActivity.class));
    }

    @OnClick(R.id.floatingActionButton_request)
    public void loadResponse() {
        responseCard.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        mHandler.postDelayed(() -> {
            progressBar.setVisibility(View.GONE);
            responseCard.setVisibility(View.VISIBLE);
        }, 2000);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        final Bundle extras = intent.getExtras();
        if (extras != null) {
            final String url = extras.getString(EditRequestActivity.REQUEST_URL);
            urlText.setText(TextUtils.isEmpty(url)
                    ? getString(R.string.text_request_url_description_none) : url);

            final String body = extras.getString(EditRequestActivity.REQUEST_BODY);
            if (body != null) {
                bodyText.setText(getString(R.string.text_request_body_description, body.length()));
            }

            final String headerKey = extras.getString(EditRequestActivity.REQUEST_HEADER_KEY);
            headersText.setText(getString(R.string.text_request_headers_description,
                    TextUtils.isEmpty(headerKey) ? 0 : 1));

            final String method = extras.getString(EditRequestActivity.REQUEST_METHOD);
            if (method != null) {
                methodText.setText(method);
                switch (method) {
                    case "GET":
                        methodText.setTextColor(getResources()
                                .getColor(R.color.color_request_method_get));
                        break;
                    case "POST":
                        methodText.setTextColor(getResources()
                                .getColor(R.color.color_request_method_post));
                        break;
                    case "PUT":
                        methodText.setTextColor(getResources()
                                .getColor(R.color.color_request_method_put));
                        break;
                    case "DELETE":
                        methodText.setTextColor(getResources()
                                .getColor(R.color.color_request_method_delete));
                        break;
                }
            }
        }
    }

    @Override
    protected int getTitle() {
        return R.string.title_request_fragment;
    }
}
