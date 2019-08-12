package com.thundersoft.bookstore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.allen.library.SuperButton;
import com.allen.library.SuperTextView;
import com.thundersoft.bookstore.Dao.ManagerDAO;
import com.thundersoft.bookstore.R;
import com.thundersoft.bookstore.model.Manager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PersonalActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "PersonalActivity";

    @BindView(R.id.personal_name)
    SuperTextView mName;
    @BindView(R.id.personal_email)
    SuperTextView mEmail;
    @BindView(R.id.personal_change_btn)
    SuperButton mChangeBtn;
    @BindView(R.id.personal_info)
    SuperTextView mInfo;
    @BindView(R.id.personal_toolbar)
    Toolbar mToolbar;

    private Unbinder mBinder;
    private Intent mIntent;
    private ActionBar mBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        mBinder = ButterKnife.bind(this);
        initClickListener();
    }

    private void initClickListener() {
        mChangeBtn.setOnClickListener(this);
    }

    private void initData() {
        mIntent = getIntent();
        mIntent.setClass(this, ChangeInfoActivity.class);

        //从数据库获得管理员信息
        int managerId = mIntent.getIntExtra("managerId", -1);
        Manager manager = ManagerDAO.getManagerById(managerId);
        String managerAccount = manager.getManagerAccount();
        String managerName = manager.getManagerName();
        String managerEmail = manager.getManagerEmail();
        String managerInfo = manager.getManagerIntroduce();


        //账户信息栏
        mName.setRightString("账户 :" + managerAccount)
                .setLeftString("昵称 :" + managerName);

        //电子邮箱栏
        mEmail.setLeftString("电子邮箱")
                .setCenterString(managerEmail);

        //信息介绍
        mInfo.setLeftString("个人简介: ")
                .setCenterString(managerInfo);

        //标题栏
        setSupportActionBar(mToolbar);
        mBar = getSupportActionBar();
        if (mBar != null) {
            mBar.setDisplayHomeAsUpEnabled(true);
            mBar.setTitle("个人中心");
        }
    }

    @Override
    protected void onStart() {
        initData();
        super.onStart();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.personal_change_btn:
                PersonalActivity.this.startActivity(mIntent);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinder.unbind();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
