package com.thundersoft.bookstore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.thundersoft.bookstore.R;
import com.thundersoft.bookstore.adapter.MessageAdapter;
import com.thundersoft.bookstore.model.Message;
import org.jetbrains.annotations.NotNull;
import org.litepal.crud.DataSupport;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageActivity extends AppCompatActivity {

    private static final String TAG = "MessageActivity";

    @BindView(R.id.message_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.message_recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.message_swipe)
    SwipeRefreshLayout mSwipe;

    private List<Message> messageList;

    private MessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
        initControls();
    }

    private void initData() {
        messageList = DataSupport.findAll(Message.class);

        //消息适配器
        adapter = new MessageAdapter(messageList);
        mRecyclerView.setAdapter(adapter);
    }

    private void initControls() {

        //标题栏
        setSupportActionBar(mToolbar);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setTitle("消息管理");
        }

        //下拉刷新
        mSwipe.setOnRefreshListener(this::refreshData);
    }

    private void refreshData(){
        new Thread(() -> runOnUiThread(() -> {
            if (messageList != null){
                messageList.clear();
                messageList.addAll(DataSupport.findAll(Message.class));
            }else {
                messageList = DataSupport.findAll(Message.class);
            }
            if (adapter == null){
                adapter = new MessageAdapter(messageList);
                mRecyclerView.setAdapter(adapter);
            }else {
                adapter.notifyDataSetChanged();
            }
            mSwipe.setRefreshing(false);
        })).start();
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.add_message:
                Intent intent = new Intent(this, AddMessageActivity.class);
                startActivity(intent);
                return true;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_message, menu);
        return true;
    }
}
