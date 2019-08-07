package com.thundersoft.bookstore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.allen.library.SuperButton;
import com.google.gson.Gson;
import com.thundersoft.bookstore.Dao.ManagerDAO;
import com.thundersoft.bookstore.R;
import com.thundersoft.bookstore.model.Manager;

import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ChangeInfoActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "ChangeInfoActivity";

    @BindView(R.id.changInfo_name)
    EditText mName;
    @BindView(R.id.changInfo_email)
    EditText mEmail;
    @BindView(R.id.changInfo_info)
    EditText mInfo;
    @BindView(R.id.changInfo_submit)
    SuperButton mSubmit;

    private Unbinder mBinder;

    private Intent mIntent;

    private Manager manager;

    private String name;

    private String email;

    private String info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info);
        mBinder = ButterKnife.bind(this);
        initData();
        initClickListener();
    }

    private void initClickListener(){
        mSubmit.setOnClickListener(this);
    }
    private void initData(){
        mIntent = getIntent();
        mIntent.setClass(this,PersonalActivity.class);
        //从数据库获取管理员数据
        int managerId = mIntent.getIntExtra("managerId",-1);
        manager = ManagerDAO.getManagerById(managerId);
        //
        name = manager.getManagerName();
        email = manager.getManagerEmail();
        info = manager.getManagerIntroduce();
        mName.setText(name);
        mEmail.setText(email);
        mInfo.setText(info);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.changInfo_submit:
                name = String.valueOf(mName.getText());
                email = String.valueOf(mEmail.getText());
                info = String.valueOf(mInfo.getText());
                boolean result = isAvailable(name,email,info);
                if (result){
                    manager.setManagerName(name);
                    manager.setManagerEmail(email);
                    manager.setManagerIntroduce(info);
                    ManagerDAO.updataManagerById(manager);
                    //跳转至个人中心
                    this.startActivity(mIntent);
                    //销毁修改活动,释放资源
                    this.finish();
                }
                break;
            default:
                break;
        }
    }


    private boolean isAvailable(String name,String email,String info){
        if (TextUtils.isEmpty(name)|TextUtils.isEmpty(email)|TextUtils.isEmpty(info)){
            Toast.makeText(this, "信息不能为空!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinder.unbind();
    }
}
