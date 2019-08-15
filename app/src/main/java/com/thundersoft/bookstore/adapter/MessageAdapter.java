package com.thundersoft.bookstore.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.thundersoft.bookstore.R;
import com.thundersoft.bookstore.activity.AddMessageActivity;
import com.thundersoft.bookstore.model.Message;

import org.litepal.crud.DataSupport;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private static final String TAG = "MessageAdapter";

    private List<Message> mMessages;

    private Context mContext;

    public MessageAdapter(List<Message> messages) {
        this.mMessages = messages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
        mContext = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = mMessages.get(position);
        holder.mTitle.setText(message.getTitle());
        holder.mContent.setText(message.getContent());

        holder.mEdit.setOnClickListener(mView -> {
            //编辑
            Intent intent = new Intent(mContext, AddMessageActivity.class);
            intent.putExtra("messageId",message.getId());
            mContext.startActivity(intent);
        });

        holder.mDelete.setOnClickListener(mView -> {
            //删除
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage("是否删除"+message.getTitle()+"?")
                    .setPositiveButton("删除",(dialogInterface, i) -> {
                        message.delete();
                        mMessages.clear();
                        mMessages.addAll(DataSupport.findAll(Message.class));
                        notifyDataSetChanged();
                    }).show();

        });
    }


    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View messageView;
        @BindView(R.id.message_item_title)
        TextView mTitle;
        @BindView(R.id.message_item_content)
        TextView mContent;
        @BindView(R.id.message_item_edit)
        Button mEdit;
        @BindView(R.id.message_item_delete)
        Button mDelete;


        public ViewHolder(View view) {
            super(view);
            messageView = view;
            ButterKnife.bind(this, view);
        }
    }
}
