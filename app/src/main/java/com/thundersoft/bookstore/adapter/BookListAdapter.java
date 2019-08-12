package com.thundersoft.bookstore.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.thundersoft.bookstore.R;
import com.thundersoft.bookstore.model.Book;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;


public class BookListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    private List<Book> mBookList;

    private Context mContext;

    public BookListAdapter(Context context, List<Book> mBookList) {
        this.mContext = context;
        this.mBookList = mBookList;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mBookList.size();
    }

    @Override
    public Object getItem(int position) {
        return mBookList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Book book = mBookList.get(position);
        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.booklist_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Glide.with(mContext).load(book.getImageurl()).into(viewHolder.bookListImage);
        viewHolder.bookListName.setText(book.getBookName());
        viewHolder.bookListAbstract.setText(book.getBookAbstract());
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.bookList_image)
        ImageView bookListImage;
        @BindView(R.id.bookList_name)
        TextView bookListName;
        @BindView(R.id.bookList_abstract)
        TextView bookListAbstract;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
