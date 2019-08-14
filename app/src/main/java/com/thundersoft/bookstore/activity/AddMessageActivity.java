package com.thundersoft.bookstore.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.allen.library.SuperButton;
import com.thundersoft.bookstore.R;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddMessageActivity extends AppCompatActivity {

    @BindView(R.id.add_message_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.add_message_title)
    EditText mTitle;
    @BindView(R.id.add_message_content)
    EditText mContent;
    @BindView(R.id.add_message_submit)
    SuperButton mSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_message);
        ButterKnife.bind(this);
        initControls();
    }

    private void initControls() {
        //标题栏
        setSupportActionBar(mToolbar);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setTitle("添加消息");
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item){
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
