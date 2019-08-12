package com.thundersoft.bookstore.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.allen.library.SuperButton;
import com.thundersoft.bookstore.Dao.ManagerDAO;
import com.thundersoft.bookstore.R;
import com.thundersoft.bookstore.model.Manager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    /*
    * 记住密码
    * 账户
    * 密码
    * */
    private static final String REMEMBER_PASSWORD = "rememberPassword";
    private static final String ACCOUNT = "account";
    private static final String PASSWORD = "password";

    //登录信息
    private static final String ERROR_LOGIN = "账号密码错误!";
    private static final String ERROR_NOT_NULL = "账号密码不能为空!";
    private static final String LOGIN_SUCCESS = "登录成功!";

    @BindView(R.id.login_account)
    EditText mAccount;
    @BindView(R.id.login_password)
    EditText mPassword;
    @BindView(R.id.login_signIn)
    SuperButton mSignIn;
    @BindView(R.id.login_signUp)
    SuperButton mSignUp;
    @BindView(R.id.login_forget)
    TextView mForget;
    @BindView(R.id.login_remember)
    CheckBox mRemember;

    private Intent mIntent;

    private Unbinder mBinder;

    private SharedPreferences pref;

    private Manager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mBinder = ButterKnife.bind(this);
        initClickListener();
        initData();
    }

    private void initData(){
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRemember = pref.getBoolean(REMEMBER_PASSWORD, false);

        if (isRemember){
            //将账号密码设置于文本框中
            String account = pref.getString(ACCOUNT,"");
            String password = pref.getString(PASSWORD,"");
            mAccount.setText(account);
            mPassword.setText(password);
            mRemember.setChecked(true);
        }

    }

    private void initClickListener() {
        mSignIn.setOnClickListener(this);
        mSignUp.setOnClickListener(this);
        mForget.setOnClickListener(this);
        mRemember.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.login_signIn:
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(LoginActivity.this);
                String account = String.valueOf(mAccount.getText());
                String password = String.valueOf(mPassword.getText());
                String result;

                //此flag为登录信息是否正确的标志,不仅仅是账户密码为空的标志,true为错误信息,false为正确信息
                boolean flag = checkIsNull(account, password);
                if (flag) {
                    result = ERROR_NOT_NULL;
                } else {
                    manager = new Manager();
                    manager.setManagerName(account);
                    manager.setManagerPassword(password);
                    //传递登录用户信息
                    manager = ManagerDAO.login(manager);
                    if (manager != null) {
                        flag = false;
                        int managerId = manager.getId();
                        mIntent = new Intent(this, MainActivity.class);
                        mIntent.putExtra("managerId", managerId);
                        result = LOGIN_SUCCESS;

                        //记住账号密码功能
                        SharedPreferences.Editor editor = pref.edit();
                        if(mRemember.isChecked()){
                            editor.putBoolean(REMEMBER_PASSWORD,true);
                            editor.putString(ACCOUNT,account);
                            editor.putString(PASSWORD,password);
                        }else {
                            editor.clear();
                        }
                        editor.apply();
                    } else {
                        flag = true;
                        result = ERROR_LOGIN;
                    }
                }
                MessageDialog(mBuilder, flag, result);
                break;
            case R.id.login_signUp:
                Intent intent = new Intent(this, RegisterActivity.class);
                this.startActivity(intent);
                break;
            case R.id.login_forget:
                break;
            case R.id.login_remember:

                break;
        }
    }

    private boolean checkIsNull(String account, String password) {
        return TextUtils.isEmpty(account) | TextUtils.isEmpty(password);
    }

    private void MessageDialog(AlertDialog.Builder builder, boolean flag, String message) {
        if (flag) {
            builder.setTitle("错误:");
            builder.setMessage(message);
            builder.setCancelable(true);
            builder.setNegativeButton("确认", (dialogInterface, i) -> dialogInterface.dismiss());
            builder.show();
        } else {
            this.startActivity(mIntent);
            Toast.makeText(this, manager.getManagerName()+",欢迎登录!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinder.unbind();
    }
}
