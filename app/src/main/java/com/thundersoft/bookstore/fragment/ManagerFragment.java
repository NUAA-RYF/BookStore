package com.thundersoft.bookstore.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.allen.library.SuperTextView;
import com.thundersoft.bookstore.R;
import com.thundersoft.bookstore.activity.BannerManagementActivity;
import com.thundersoft.bookstore.activity.PersonalActivity;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import jp.wasabeef.blurry.Blurry;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ManagerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManagerFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "ManagerFragment";
    private static final String MANAGER_TITLE = "title";

    @BindView(R.id.manager_Image)
    ImageView mImage;
    @BindView(R.id.manager_item_personal)
    SuperTextView mPersonal;
    @BindView(R.id.manager_item_bookApply)
    SuperTextView mBookApply;
    @BindView(R.id.manager_item_addBookCategory)
    SuperTextView mAddCategory;
    @BindView(R.id.manager_item_addBook)
    SuperTextView mAddBook;
    @BindView(R.id.manager_item_banner)
    SuperTextView mBanner;


    private String title;
    private View convertView;
    private Context mContext;
    private Unbinder mBinder;
    private Intent mIntent;

    @SuppressLint("SdCardPath")
    private static String path = "/sdcard/myHead";

    private Bitmap head;
    public ManagerFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ManagerFragment newInstance(String title) {
        ManagerFragment fragment = new ManagerFragment();
        Bundle args = new Bundle();
        args.putString(MANAGER_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(MANAGER_TITLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fragment_manager, container, false);
        }
        mContext = getContext();
        mBinder = ButterKnife.bind(this,convertView);
        initData();
        initClickListener();

        return convertView;
    }

    private void initClickListener(){
        mImage.setOnClickListener(this);
        mBanner.setOnClickListener(this);
        mPersonal.setOnClickListener(this);
    }

    private void initData() {
        mContext = getContext();
        Bitmap bmp = ((BitmapDrawable) getResources().getDrawable(R.mipmap.icon_head)).getBitmap();
        Blurry.with(getContext())
                .radius(25)
                .sampling(2)
                .async()
                .from(bmp)
                .into(mImage);
        //高斯模糊头像并设置为背景
        //Bitmap bitmap = BitmapFactory.decodeFile(path);

        /*if (bitmap != null){
            //头像不为空,设置高斯模糊
            Blurry.with(getContext())
                    .radius(25)
                    .sampling(2)
                    .async()
                    .from(bitmap)
                    .into(mImage);
        }else {
            //头像为空,设置为默认头像并设置高斯模糊
            Bitmap bmp = ((BitmapDrawable) getResources().getDrawable(R.mipmap.icon_head)).getBitmap();
            Blurry.with(getContext())
                    .radius(25)
                    .sampling(2)
                    .async()
                    .from(bmp)
                    .into(mImage);
        }*/
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.manager_Image:
                Intent intent = new Intent(Intent.ACTION_PICK,null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                startActivityForResult(intent,1);
                break;
            case R.id.manager_item_banner:
                Intent manager_banner = new Intent(mContext, BannerManagementActivity.class);
                startActivity(manager_banner);
                break;
            case R.id.manager_item_personal:
                mIntent = getActivity().getIntent();
                mIntent.setClass(mContext, PersonalActivity.class);
                startActivity(mIntent);
                break;
                /*int managerId = mIntent.getIntExtra("managerId",-1);
                Manager manager = ManagerDAO.getManagerById(managerId);*/
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case 1:
                if (resultCode == RESULT_OK){
                    //剪裁图片
                    cropPhoto(data.getData());
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    File temp = new File(Environment.getExternalStorageDirectory()
                            + "/head.jpg");
                    cropPhoto(Uri.fromFile(temp));// 裁剪图片
                }
                break;
            case 3:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    head = extras.getParcelable("data");
                    if (head != null) {
                        //此处可以上传服务器或者将路径保存至用户数据库
                        setPicToView(head);// 保存在SD卡中

                        if (head != null && head.isRecycled()) {
                            head.recycle();
                        }
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //将图片
    private void setPicToView(Bitmap mBitmap) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return;
        }
        FileOutputStream b = null;
        File file = new File(path);
        file.mkdirs();// 创建文件夹
        String fileName = path + "head.jpg";// 图片名字
        try {
            b = new FileOutputStream(fileName);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭流
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    //利用系统裁剪图片
    private void cropPhoto(Uri uri){
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    //通过路径打开图片
    private Bitmap getBitmap(String pathString) {
        Bitmap bitmap = null;
        try {
            File file = new File(pathString);
            if (file.exists()) {
                bitmap = BitmapFactory.decodeFile(pathString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinder.unbind();
    }
}
