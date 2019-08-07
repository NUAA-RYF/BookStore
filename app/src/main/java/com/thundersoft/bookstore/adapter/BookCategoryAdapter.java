package com.thundersoft.bookstore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.thundersoft.bookstore.R;
import com.thundersoft.bookstore.model.BookCategory;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookCategoryAdapter extends ArrayAdapter<BookCategory> {

    private static final String TAG = "BookCategoryAdapter";

    public BookCategoryAdapter(Context context, int resource, List<BookCategory> objects) {
        super(context, resource, objects);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BookCategory bookCategory = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.bookcategory_item, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.bookCatagoryName.setText(bookCategory.getBookCategoryName());
        return view;
    }


    static class ViewHolder {
        @BindView(R.id.bookCatagory_name)
        TextView bookCatagoryName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
