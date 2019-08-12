package com.thundersoft.bookstore.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.thundersoft.bookstore.Dao.ManagerDAO;
import com.thundersoft.bookstore.R;
import com.thundersoft.bookstore.model.Manager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.register_account)
    EditText mAccount;
    @BindView(R.id.register_password)
    EditText mPassword;
    @BindView(R.id.register_password_repeat)
    EditText mPasswordRepeat;
    @BindView(R.id.register_submit)
    Button mSubmit;
    @BindView(R.id.register_email)
    EditText mEmail;
    @BindView(R.id.register_back)
    Button mBack;

    private static final String TAG = "RegisterActivity";

    private static final String PASSWORD_LENGTH = "密码长度应为6-18字符!";

    private static final String PASSWORD_REPEAT_ERROR = "两次密码不相同!";

    private static final String ACCOUNT_EXISTED = "账户已经存在,请重新输入!";

    private static final String ACCOUNT_NOT_NULL = "账户不能为空!";

    private static final String EMAIL_NOT_NULL = "电子邮箱不能为空!";

    private static final String REGISTER_SUCCESS = "注册成功,返回登录!";

    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mUnbinder = ButterKnife.bind(this);

        initClickListener();
    }

    private void initClickListener() {
        mSubmit.setOnClickListener(this);
        mBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.register_submit:

                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);

                //从UI控件获取字符串
                String account = String.valueOf(mAccount.getText());
                String password = String.valueOf(mPassword.getText());
                String password_repeat = String.valueOf(mPasswordRepeat.getText());
                String email = String.valueOf(mEmail.getText());

                //判断密码是否符合规范
                boolean result = passwordIsCorrect(password, password_repeat, builder);

                if (result) {
                    //判断账户是否存在,若存在则对话框报错提示,反之注册
                    accountIsExisted(account, builder, password,email);
                }else {
                    break;
                }
                break;
            case R.id.register_back:
                //返回登录界面,并结束此活动
                Intent intent = new Intent(this,LoginActivity.class);
                this.startActivity(intent);
                this.finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    //账户是否存在,若存在则提示,反之注册
    private boolean accountIsExisted(String account, AlertDialog.Builder builder, String password,String email) {
        //账户不能为空
        if (account.equals("")) {
            builder.setMessage(ACCOUNT_NOT_NULL)
                    .setTitle("错误:")
                    .setCancelable(true)
                    .setPositiveButton("确认", (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    })
                    .show();
            return false;
        }
        //电子邮箱不能为空
        if (email.equals("")) {
            builder.setMessage(EMAIL_NOT_NULL)
                    .setTitle("错误:")
                    .setCancelable(true)
                    .setPositiveButton("确认", (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    })
                    .show();
            return false;
        }
        //判断用户是否存在
        boolean result = ManagerDAO.accountIsExisted(account);
        if (result) {
            //注册成功
            Manager manager = new Manager(account, password, email);
            ManagerDAO.register(manager);
            builder.setMessage(REGISTER_SUCCESS)
                    .setTitle("注册成功:")
                    .setCancelable(true)
                    .setPositiveButton("确认", (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    })
                    .show();
            return true;
        } else {
            //用户已经存在,无法注册
            builder.setMessage(ACCOUNT_EXISTED)
                    .setTitle("错误:")
                    .setCancelable(true)
                    .setPositiveButton("确认", (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    })
                    .show();
            return false;
        }
    }

    //判断密码长度是否6-18,两次密码是否相同
    private boolean passwordIsCorrect(String password, String passwordRepeat, AlertDialog.Builder builder) {
        int length = password.length();
        if (length < 6) {
            //密码长度不足
            builder.setMessage(PASSWORD_LENGTH)
                    .setTitle("错误:")
                    .setCancelable(true)
                    .setPositiveButton("确认", (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    })
                    .show();
            return false;
        } else if (password.equals(passwordRepeat)) {
            //密码相同,正确
            return true;
        } else {
            Log.i(TAG, "passwordIsCorrect: " + password);
            Log.i(TAG, "passwordIsCorrect: " + passwordRepeat);
            builder.setMessage(PASSWORD_REPEAT_ERROR)
                    .setTitle("错误:")
                    .setCancelable(true)
                    .setPositiveButton("确认", (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    })
                    .show();
            return false;
        }
    }
}
