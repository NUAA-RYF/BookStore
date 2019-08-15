package com.thundersoft.bookstore.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.allen.library.SuperTextView;
import com.thundersoft.bookstore.R;
import com.thundersoft.bookstore.model.Message;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageActivityAdapter extends RecyclerView.Adapter<MessageActivityAdapter.ViewHolder> {

    private List<Message> mMessageList;

    public MessageActivityAdapter(List<Message> messageList) {
        mMessageList = messageList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_activity_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = mMessageList.get(position);
        holder.mTitle.setLeftString(message.getTitle());
        holder.mContent.setText(message.getContent());
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    static
    class ViewHolder extends RecyclerView.ViewHolder{
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
