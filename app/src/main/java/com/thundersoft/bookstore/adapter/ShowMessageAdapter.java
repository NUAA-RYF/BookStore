package com.thundersoft.bookstore.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.allen.library.SuperTextView;
import com.thundersoft.bookstore.R;
import com.thundersoft.bookstore.activity.ShowMessageActivity;
import com.thundersoft.bookstore.model.Message;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowMessageAdapter extends RecyclerView.Adapter<ShowMessageAdapter.ViewHolder> {

    private static final String TAG = "ShowMessageAdapter";

    private List<Message> mMessageList;

    private Context mContext;

    public ShowMessageAdapter(List<Message> messageList) {
        this.mMessageList = messageList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_show_item, parent, false);
        mContext = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mMessageList.size()>0){
            Message message = mMessageList.get(position);
            holder.mTitle.setLeftString(message.getTitle());
            holder.mContent.setText(message.getContent());
            holder.mTitle.setOnClickListener(view -> {
                Log.i(TAG, "queryMessage: 点击事件触发");
                Intent intent = new Intent(mContext, ShowMessageActivity.class);
                if (mContext != null) {
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    static
    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.message_title)
        SuperTextView mTitle;
        @BindView(R.id.message_content)
        TextView mContent;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
