package com.thundersoft.bookstore.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.thundersoft.bookstore.R;
import com.thundersoft.bookstore.activity.BookActivity;
import com.thundersoft.bookstore.model.Book;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    private static final String TAG = "BookAdapter";

    private Context mContext;

    private List<Book> mBookList;

    static class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        @BindView(R.id.book_item_image)
        ImageView bookImage;
        @BindView(R.id.book_item_name)
        TextView bookName;
        @BindView(R.id.book_item_readerCount)
        TextView bookReaderCount;
        @BindView(R.id.book_item_abstract)
        TextView bookAbstract;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            ButterKnife.bind(this,view);
        }
    }

    public BookAdapter(List<Book> bookList) {
        mBookList = bookList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.book_item, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.cardView.setOnClickListener(view1 -> {
            int position = viewHolder.getAdapterPosition();
            Book book = mBookList.get(position);
            Intent intent = new Intent(mContext, BookActivity.class);

            //使用Json格式传输对象
            intent.putExtra("book", new Gson().toJson(book));
            mContext.startActivity(intent);
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = mBookList.get(position);
        holder.bookName.setText(book.getBookName());
        holder.bookReaderCount.setText(book.getReaderCount());
        holder.bookAbstract.setText(book.getBookAbstract());
        Glide.with(mContext).load(book.getImageurl()).into(holder.bookImage);
    }

    @Override
    public int getItemCount() {
        return mBookList.size();
    }
}
