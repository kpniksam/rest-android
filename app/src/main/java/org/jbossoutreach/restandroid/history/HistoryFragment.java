package org.jbossoutreach.restandroid.history;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jbossoutreach.restandroid.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HistoryFragment extends Fragment {
    @BindView(R.id.recyclerView_history)
    RecyclerView recyclerView;
    private Unbinder mUnbinder;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_history, container, false);

        mUnbinder = ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final List<Request> mockList = new ArrayList<>();
        mockList.add(new Request(Request.Method.GET, "https://www.google.com"));
        mockList.add(new Request(Request.Method.POST, "http://www.example.com"));
        mockList.add(new Request(Request.Method.PUT, "http://www.example.com"));
        mockList.add(new Request(Request.Method.DELETE, "https://www.google.com"));
        recyclerView.setAdapter(new Adapter(mockList));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mUnbinder.unbind();
    }

    private final class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

        private List<Request> requests;

        Adapter(List<Request> requests) {
            super();
            this.requests = requests;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_history_request, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final Request request = requests.get(position);
            holder.bind(request);
        }

        @Override
        public int getItemCount() {
            return requests.size();
        }

        final class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private TextView method;
            private TextView url;

            ViewHolder(View itemView) {
                super(itemView);

                method = itemView.findViewById(R.id.textView_history_method);
                url = itemView.findViewById(R.id.textView_history_url);

                itemView.setOnClickListener(this);
            }

            void bind(Request request) {
                method.setText(request.method.toString());
                final Resources res = getContext().getResources();
                switch (request.method) {
                    case GET:
                        method.setTextColor(res.getColor(R.color.color_request_method_get));
                        break;
                    case POST:
                        method.setTextColor(res.getColor(R.color.color_request_method_post));
                        break;
                    case PUT:
                        method.setTextColor(res.getColor(R.color.color_request_method_put));
                        break;
                    case DELETE:
                        method.setTextColor(res.getColor(R.color.color_request_method_delete));
                        break;
                }
                url.setText(request.url);
            }

            @Override
            public void onClick(View view) {
                final Resources res = getContext().getResources();
                Snackbar.make(view, res.getString(R.string.text_item_clicked_prompt), Snackbar.LENGTH_SHORT).show();
            }
        }
    }
}
