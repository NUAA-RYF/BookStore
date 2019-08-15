package com.thundersoft.bookstore.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thundersoft.bookstore.R;
import com.thundersoft.bookstore.adapter.MessageActivityAdapter;
import com.thundersoft.bookstore.model.Message;

import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowMessageActivity extends AppCompatActivity {

    @BindView(R.id.show_message_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.show_message_recycleView)
    RecyclerView mRecycleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_message);
        ButterKnife.bind(this);
        initControls();
    }

    private void initControls(){
        //消息适配器
        List<Message> messageList = DataSupport.findAll(Message.class);
        MessageActivityAdapter mAdapter = new MessageActivityAdapter(messageList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(layoutManager);
        mRecycleView.setAdapter(mAdapter);

        //标题栏
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("消息通知");
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return true;
    }
}
