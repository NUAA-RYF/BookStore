package com.thundersoft.bookstore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.allen.library.SuperButton;
import com.thundersoft.bookstore.R;
import com.thundersoft.bookstore.model.Message;
import org.jetbrains.annotations.NotNull;
import org.litepal.crud.DataSupport;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AddMessageActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.add_message_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.add_message_title)
    EditText mTitle;
    @BindView(R.id.add_message_content)
    EditText mContent;
    @BindView(R.id.add_message_submit)
    SuperButton mSubmit;

    private static final String ADD_FAILED = "添加失败!";

    private static final String ADD_SUCCESS = "添加成功!";

    private static final String CHANGE_SUCCESS = "修改成功!";

    private static final String CONFIRM = "确认";

    private boolean addType;
    private boolean changeType;
    private int id;
    private Message mMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_message);
        ButterKnife.bind(this);
        initData();
        initControls();
        initClickListener();
    }

    private void initData(){

        Intent intent = getIntent();
        id = intent.getIntExtra("messageId",0);

        addType = false;
        changeType = false;
        if (id != 0){
            changeType = true;
        }else {
            addType = true;
        }
    }

    private void initControls() {

        //若是修改模式
        if (changeType){
            mMessage = DataSupport.find(Message.class,id);
            if (mMessage != null){
                mTitle.setText(mMessage.getTitle());
                mContent.setText(mMessage.getContent());
            }
        }

        //标题栏
        setSupportActionBar(mToolbar);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setTitle("添加消息");
        }

    }

    private void initClickListener() {
        mSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.add_message_submit) {
            String title = mTitle.getText().toString();
            String content = mContent.getText().toString();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            boolean flag = true;

            if (title.equals("")) {
                builder.setTitle(ADD_FAILED)
                        .setMessage("消息标题不能为空!")
                        .setPositiveButton(CONFIRM, (dialogInterface, i) -> dialogInterface.dismiss())
                        .show();
                flag = false;
            }

            if (content.equals("") && flag) {
                builder.setTitle(ADD_FAILED)
                        .setMessage("消息内容不能为空!")
                        .setPositiveButton(CONFIRM, (dialogInterface, i) -> dialogInterface.dismiss())
                        .show();
                flag = false;
            }

            if (flag) {
                List<Message> messageList = DataSupport.findAll(Message.class);
                if (messageList.size() > 0) {
                    for (Message message : messageList) {
                        if (message.getTitle().equals(title)) {
                            builder.setTitle(ADD_FAILED)
                                    .setMessage("消息标题已存在!")
                                    .setPositiveButton(CONFIRM, (dialogInterface, i) -> dialogInterface.dismiss())
                                    .show();
                            flag = false;
                            break;
                        }
                    }
                }
            }

            if (flag && changeType){
                mMessage.setTitle(title);
                mMessage.setContent(content);
                mMessage.update(mMessage.getId());

                builder.setTitle(CHANGE_SUCCESS)
                        .setMessage("消息标题: " + title + "\n" + "消息内容: " + content)
                        .setPositiveButton(CONFIRM, (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                            finish();
                        })
                        .show();
            }

            if (flag && addType) {
                Message message = new Message();
                message.setTitle(title);
                message.setContent(content);
                message.save();

                builder.setTitle(ADD_SUCCESS)
                        .setMessage("消息标题: " + title + "\n" + "消息内容: " + content)
                        .setPositiveButton(CONFIRM, (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                            finish();
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
