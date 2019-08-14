package com.thundersoft.bookstore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import com.thundersoft.bookstore.R;
import com.thundersoft.bookstore.adapter.BannerBookAdapter;
import com.thundersoft.bookstore.adapter.BookCategoryAdapter;
import com.thundersoft.bookstore.model.Book;
import com.thundersoft.bookstore.model.BookCategory;

import org.jetbrains.annotations.NotNull;
import org.litepal.crud.DataSupport;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class BookManagementActivity extends AppCompatActivity {

    @BindView(R.id.banner_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.banner_categoryList)
    ListView mCategoryListView;
    @BindView(R.id.banner_book_recyclerView)
    RecyclerView mBookRecyclerView;

    private List<Book> mBookList;

    private List<Book> mCurrentBookList;

    private List<BookCategory> mCategoryList;

    private BannerBookAdapter mBannerBookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_management);
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
                mBookList.addAll(mCurrentBookList);
                mCurrentBookList.clear();
            }
            mBannerBookAdapter.notifyDataSetChanged();
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_book,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item){
        switch (item.getItemId()){
            case R.id.add_book:
                Intent intent = new Intent(this,AddBookActivity.class);
                this.startActivity(intent);
                break;
            case R.id.add_bookList:
                intent = new Intent(this,AddBookListActivity.class);
                this.startActivity(intent);
                break;
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    private void initData() {
        mCategoryList = DataSupport.findAll(BookCategory.class);
        mBookList = DataSupport.where("categoryId=242").find(Book.class);
    }


    private void initControls() {
        //书籍种类列表适配器和视图
        BookCategoryAdapter bookCategoryAdapter = new BookCategoryAdapter(this, android.R.layout.simple_list_item_1, mCategoryList);
        mCategoryListView.setAdapter(bookCategoryAdapter);

        //书籍适配器和视图
        mBannerBookAdapter = new BannerBookAdapter(mBookList);
        mBookRecyclerView.setAdapter(mBannerBookAdapter);

        //标题栏
        setSupportActionBar(mToolbar);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setTitle("书籍管理");
        }
    }
}
