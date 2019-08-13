package com.thundersoft.bookstore.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.allen.library.SuperButton;
import com.thundersoft.bookstore.R;
import com.thundersoft.bookstore.model.BookCategory;

import org.jetbrains.annotations.NotNull;
import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddBookListActivity extends AppCompatActivity implements View.OnClickListener {


    @BindView(R.id.add_bookList_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.add_bookList_nameImg)
    ImageView mNameImg;
    @BindView(R.id.add_bookList_name)
    EditText mName;
    @BindView(R.id.add_bookList_idImg)
    ImageView mIdImg;
    @BindView(R.id.add_bookList_id)
    EditText mId;
    @BindView(R.id.add_bookList_addBtn)
    SuperButton mAddBtn;

    private List<BookCategory> mCategoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_booklist);
        ButterKnife.bind(this);
        initData();
        initControls();
        initClickListener();
    }


    private void initData() {
        mCategoryList = DataSupport.findAll(BookCategory.class);
    }

    private void initControls() {
        //标题栏
        setSupportActionBar(mToolbar);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setTitle("添加书籍种类");
        }
    }

    private void initClickListener() {
        mAddBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.add_bookList_addBtn) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            String name = String.valueOf(mName.getText());
            String stringId = String.valueOf(mId.getText());
            boolean flag = true;

            //判断名称和ID不能为空
            if (name.equals("") || stringId.equals("")) {
                builder.setTitle("添加失败")
                        .setMessage("书籍名称与ID不得为空!")
                        .setPositiveButton("确认", (dialogInterface, i) -> dialogInterface.dismiss())
                        .show();
                flag = false;
            }
            int id = Integer.parseInt(stringId);

            //判断ID范围应为1~999
            if (id < 1 || id >= 1000) {
                builder.setTitle("添加失败")
                        .setMessage("ID范围应为1~999!")
                        .setPositiveButton("确认", (dialogInterface, i) -> dialogInterface.dismiss())
                        .show();
                flag = false;
            }

            //判断名称和ID是否已经存在
            if (flag) {
                for (BookCategory category : mCategoryList) {
                    if (category.getCategoryId() == id && flag) {
                        builder.setTitle("添加失败")
                                .setMessage("ID已存在!")
                                .setPositiveButton("确认", (dialogInterface, i) -> dialogInterface.dismiss())
                                .show();
                        flag = false;
                    }
                    if (category.getBookCategoryName().equals(name) && flag) {
                        builder.setTitle("添加失败")
                                .setMessage("书籍名称已存在!")
                                .setPositiveButton("确认", (dialogInterface, i) -> dialogInterface.dismiss())
                                .show();
                        flag = false;
                    }
                }
            }

            if (flag) {
                //判断完毕,可以存储
                BookCategory category = new BookCategory();
                category.setSelected(true);
                category.setBookCategoryName(name);
                category.setCategoryId(id);
                category.save();
                builder.setTitle("成功")
                        .setMessage("书籍种类名称:" + name + "\n" + "ID : " + id)
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
