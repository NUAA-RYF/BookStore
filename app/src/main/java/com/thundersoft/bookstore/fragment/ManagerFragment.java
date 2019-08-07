package com.thundersoft.bookstore.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;
import com.allen.library.SuperTextView;
import com.google.gson.Gson;
import com.thundersoft.bookstore.Dao.ManagerDAO;
import com.thundersoft.bookstore.R;
import com.thundersoft.bookstore.activity.PersonalActivity;
import com.thundersoft.bookstore.model.Book;
import com.thundersoft.bookstore.model.Manager;
import com.thundersoft.bookstore.util.HttpUtil;
import com.thundersoft.bookstore.util.Util;
import org.jetbrains.annotations.NotNull;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import jp.wasabeef.blurry.Blurry;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ManagerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManagerFragment extends Fragment {

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
    @BindView(R.id.manager_item_bookRecommend)
    SuperTextView mRecommend;
    @BindView(R.id.manager_item_book)
    SuperTextView mBook;
    @BindView(R.id.manager_item_banner)
    SuperTextView mBanner;
    @BindView(R.id.manager_item_bookList)
    SuperTextView mBookList;

    private String title;
    private View convertView;
    private Context mContext;
    private Unbinder mBinder;
    private Intent mIntent;
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


        return convertView;
    }

    private void initData() {

        mContext = getContext();

        //高斯模糊头像并设置为背景
        Bitmap bmp = ((BitmapDrawable) getResources().getDrawable(R.mipmap.icon_head)).getBitmap();
        Blurry.with(getContext())
                .radius(25)
                .sampling(2)
                .async()
                .from(bmp)
                .into(mImage);

        //携带管理员信息参数
        mIntent = getActivity().getIntent();
        int managerId = mIntent.getIntExtra("managerId",-1);

        Manager manager = ManagerDAO.getManagerById(managerId);


        mIntent.setClass(mContext, PersonalActivity.class);
        mPersonal.setOnSuperTextViewClickListener(superTextView -> {
            mContext.startActivity(mIntent);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinder.unbind();
    }
}
