package com.thundersoft.bookstore.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import com.thundersoft.bookstore.R;
import com.thundersoft.bookstore.adapter.BannerBookAdapter;
import com.thundersoft.bookstore.adapter.BookCategoryAdapter;
import com.thundersoft.bookstore.model.Book;
import com.thundersoft.bookstore.model.BookCategory;
import org.litepal.crud.DataSupport;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class BannerManagementActivity extends AppCompatActivity {

    private static final String TAG = "BannerManagement";

    @BindView(R.id.banner_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.banner_categoryList)
    ListView mCategoryListView;
    @BindView(R.id.banner_book_recyclerView)
    RecyclerView mBookRecyclerView;

    private ActionBar mBar;

    private List<Book> mBookList;

    private List<Book> mCurrentBookList;

    private List<BookCategory> mCategoryList;

    private BannerBookAdapter mBannerBookAdapter;

    private BookCategoryAdapter mBookCategoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_management);
        ButterKnife.bind(this);
        initData();
        initControls();
        initClickLitsener();
    }


    private void initClickLitsener() {

        mCategoryListView.setOnItemClickListener((adapterView, parent, position, id) -> {
            BookCategory currentCate = mCategoryList.get(position);
            mCurrentBookList = DataSupport.where("categoryId=" + currentCate.getCategoryId()).find(Book.class);

            //若当前书籍列表不为空,则显示
            if (mCurrentBookList.size() > 0){
                mBookList.clear();
                for (int i = 0; i < mCurrentBookList.size(); i++) {
                    mBookList.add(mCurrentBookList.get(i));
                }
                mCurrentBookList.clear();
            }
            mBannerBookAdapter.notifyDataSetChanged();
        });


    }

    private void initData() {
        mCategoryList = DataSupport.findAll(BookCategory.class);
        mBookList = DataSupport.where("categoryId=242").find(Book.class);
    }


    private void initControls() {
        //书籍种类列表适配器和视图
        mBookCategoryAdapter = new BookCategoryAdapter(this, android.R.layout.simple_list_item_1, mCategoryList);
        mCategoryListView.setAdapter(mBookCategoryAdapter);

        //书籍适配器和视图
        mBannerBookAdapter = new BannerBookAdapter(mBookList);
        mBookRecyclerView.setAdapter(mBannerBookAdapter);

        //标题栏
        setSupportActionBar(mToolbar);
        mBar = getSupportActionBar();
        if (mBar != null) {
            mBar.setDisplayHomeAsUpEnabled(true);
            mBar.setTitle("书籍管理");
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
