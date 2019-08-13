package com.thundersoft.bookstore.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;

import com.allen.library.SuperButton;
import com.thundersoft.bookstore.R;
import com.thundersoft.bookstore.model.Book;
import com.thundersoft.bookstore.model.BookCategory;
import com.thundersoft.bookstore.model.Management;

import org.jetbrains.annotations.NotNull;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddBookActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "AddBookActivity";

    @BindView(R.id.add_book_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.add_book_name)
    EditText mName;
    @BindView(R.id.add_book_abstract)
    EditText mAbstract;
    @BindView(R.id.add_book_imgUrl)
    EditText mImgUrl;
    @BindView(R.id.add_book_category)
    Spinner mCategory;
    @BindView(R.id.add_book_content)
    EditText mContent;
    @BindView(R.id.add_book_addBtn)
    SuperButton mAddBtn;

    private String categoryName;
    private ArrayAdapter mAdapter;
    private List<BookCategory> mCategoryList;
    private int categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        ButterKnife.bind(this);
        initData();
        initControls();
        initClickListener();
    }


    private void initControls(){
        //标题栏
        setSupportActionBar(mToolbar);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setTitle("添加书籍");
        }
    }

    private void initData(){
        mCategoryList = DataSupport.findAll(BookCategory.class);
        List<String> categoryNames = new ArrayList<>();
        for (BookCategory category : mCategoryList) {
            categoryNames.add(category.getBookCategoryName());
        }
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryNames);
        //设置下拉样式
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCategory.setAdapter(mAdapter);
    }

    private void initClickListener() {
        mAddBtn.setOnClickListener(this);
        mCategory.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                categoryName = String.valueOf(mAdapter.getItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                categoryName = "中国文学";
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.add_book_addBtn) {
            String name = String.valueOf(mName.getText());
            String aBstract = String.valueOf(mAbstract.getText());
            String url = String.valueOf(mImgUrl.getText());
            String content = String.valueOf(mContent.getText());
            Log.i(TAG, "onClick: category name :" + categoryName);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            boolean flag = true;
            if (name.equals("")) {
                builder.setTitle("添加失败")
                        .setMessage("书籍名称不得为空!")
                        .setPositiveButton("确认", (dialogInterface, i) -> dialogInterface.dismiss())
                        .show();
                flag = false;
            }
            if (aBstract.equals("") && flag) {
                builder.setTitle("添加失败")
                        .setMessage("书籍摘要不得为空!")
                        .setPositiveButton("确认", (dialogInterface, i) -> dialogInterface.dismiss())
                        .show();
                flag = false;
            }
            if (url.equals("") && flag) {
                builder.setTitle("添加失败")
                        .setMessage("书籍图片地址不得为空!")
                        .setPositiveButton("确认", (dialogInterface, i) -> dialogInterface.dismiss())
                        .show();
                flag = false;
            }
            if (content.equals("") && flag) {
                builder.setTitle("添加失败")
                        .setMessage("书籍内容不得为空!")
                        .setPositiveButton("确认", (dialogInterface, i) -> dialogInterface.dismiss())
                        .show();
                flag = false;
            }

            //取出书籍种类ID
            if (flag){
                for (BookCategory category : mCategoryList) {
                    if (category.getBookCategoryName().equals(categoryName)){
                        categoryId = category.getCategoryId();
                        break;
                    }
                }
            }

            if (flag) {
                Book book = new Book();
                book.setBookName(name);
                book.setBookAbstract(aBstract);
                book.setBookContent(content);
                book.setImageurl(url);
                book.setCategoryId(String.valueOf(categoryId));
                Log.i(TAG, "onClick: categoryId "+categoryId);
                book.save();

                builder.setTitle("添加成功")
                        .setMessage("书籍名称: " + name)
                        .setPositiveButton("确认", (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                            this.finish();
                        })
                        .show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
