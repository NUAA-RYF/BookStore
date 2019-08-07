package com.thundersoft.bookstore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.gson.Gson;
import com.thundersoft.bookstore.R;
import com.thundersoft.bookstore.model.Book;
import butterknife.BindView;
import butterknife.ButterKnife;

public class BookActivity extends AppCompatActivity {

    private static final String TAG = "BookActivity";

    private static final String BOOK_OBJECT = "book";

    @BindView(R.id.book_activity_bookImage)
    ImageView mBookImage;

    @BindView(R.id.book_activity_toolBar)
    Toolbar mToolbar;

    @BindView(R.id.book_activity_collToolBar)
    CollapsingToolbarLayout mCollapsingToolbar;

    @BindView(R.id.book_activity_content)
    TextView mContent;

    ActionBar mActionBar;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        ButterKnife.bind(this);

        initControls();
        initData();
    }

    private void initControls() {
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    private void initData() {
        Log.i(TAG, "initData: ");
        intent = getIntent();
        String JsonData = intent.getStringExtra(BOOK_OBJECT);
        Book book = new Gson().fromJson(JsonData, Book.class);
        mCollapsingToolbar.setTitle(book.getBookName());
        Glide.with(this).load(book.getImageurl()).into(mBookImage);
        mContent.setText(book.getBookContent());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
