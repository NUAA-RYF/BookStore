package com.thundersoft.bookstore.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.allen.library.SuperButton;
import com.thundersoft.bookstore.R;
import com.thundersoft.bookstore.model.Message;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private List<Message> mMessages;

    public MessageAdapter(List<Message> messages) {
        mMessages = messages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = mMessages.get(position);
        holder.mTitle.setText(message.getTitle());
        holder.mContent.setText(message.getContent());

        holder.mEdit.setOnClickListener(view -> {
            //编辑
        });

        holder.mDelete.setOnClickListener(view -> {
            //删除
        });
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.message_item_title)
        TextView mTitle;
        @BindView(R.id.message_item_content)
        TextView mContent;
        @BindView(R.id.message_item_edit)
        SuperButton mEdit;
        @BindView(R.id.message_item_delete)
        SuperButton mDelete;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
