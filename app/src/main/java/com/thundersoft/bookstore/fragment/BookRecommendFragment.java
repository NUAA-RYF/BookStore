package com.thundersoft.bookstore.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.thundersoft.bookstore.R;
import com.thundersoft.bookstore.adapter.BookAdapter;
import com.thundersoft.bookstore.adapter.ShowMessageAdapter;
import com.thundersoft.bookstore.model.Book;
import com.thundersoft.bookstore.model.BookCategory;
import com.thundersoft.bookstore.model.Management;
import com.thundersoft.bookstore.util.HttpUtil;
import com.thundersoft.bookstore.util.Util;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import org.jetbrains.annotations.NotNull;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class BookRecommendFragment extends Fragment {

    private static final String KEY = "006e8b17f75158a969f56e37f4979d41";

    private static final String PN = "0";//起始位置

    private static final String RN = "20";//数据最大返回条数

    private static final String FRAGMENT_TITLE = "title";

    @BindView(R.id.book_recommend_banner)
    Banner mBanner;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.book_recommend_message)
    RecyclerView mMessageView;

    private ProgressDialog mDialog;

    private AlertDialog.Builder builder;

    private Context context;

    private List<Book> mBookList;

    private List<BookCategory> mCategorys;

    private Management mManagement;
    //根视图
    private View rootView;
    //是否准备
    private boolean mIsPrepare = false;
    //是否可见
    private boolean mIsVisible = false;
    //是否第一次加载
    private boolean mIsFirstLoad = true;

    private List<String> urls;

    private BookAdapter bookAdapter;

    private Unbinder mBinder;

    public BookRecommendFragment() {
        // Required empty public constructor
    }

    public static BookRecommendFragment newInstance(String title) {
        BookRecommendFragment fragment = new BookRecommendFragment();
        Bundle args = new Bundle();
        args.putString(FRAGMENT_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_bookrecommend, container, false);
        }
        context = getContext();
        builder = new AlertDialog.Builder(context);
        mIsPrepare = true;
        //绑定控件,返回Unbinder实例
        mBinder = ButterKnife.bind(this, rootView);
        lazyload();
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private void loadData() {
        //加载系统消息通知
        queryMessage();
        //初始化管理
        initManagement();
        //加载数据
        queryBookFromDatabase();
    }

    private void initManagement(){
        //初始化管理
        if (DataSupport.findAll(Management.class).size() <= 0) {
            Management management = new Management();
            management.setBannerManagement("0 0 0 0 0");
            management.setBookRecommendId("0 0 0 0 0 0 0 0 0 0");
            management.save();
        }

        mManagement = DataSupport.findFirst(Management.class);

        if (mManagement.getBannerManagement() == null) {
            mManagement.setBannerManagement("0 0 0 0 0");
            mManagement.update(mManagement.getId());
            mManagement = DataSupport.find(Management.class, 1);
        }

        if (mManagement.getBookRecommendId() == null) {
            mManagement.setBookRecommendId("0 0 0 0 0 0 0 0 0 0");
            mManagement.update(mManagement.getId());
            mManagement = DataSupport.find(Management.class, 1);
        }
    }
    //从数据库中查询
    private void queryBookFromDatabase() {

        urls = new ArrayList<>();
        mBookList = new ArrayList<>();
        mCategorys = new ArrayList<>();

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        bookAdapter = new BookAdapter(mBookList);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(bookAdapter);

        queryCategory();

        //mBookList应为管理员设置推荐书籍
        String[] recommend_bookId = mManagement.getBookRecommendId().split(" ");
        if (recommendIsAvailable(recommend_bookId)) {
            mBookList.clear();
            for (String s : recommend_bookId) {
                if (!s.equals("0")) {
                    Book book = DataSupport.find(Book.class, Integer.parseInt(s));
                    mBookList.add(book);
                }
            }
        }

        bannerSet();
    }

    private void bannerSet(){
        //urls轮播图应为管理员设置推荐书籍,应为url地址或者图片id
        boolean flag = false;
        String[] banner_bookId = mManagement.getBannerManagement().split(" ");
        if (bannerIsAvailable(banner_bookId)) {
            urls.clear();
            for (String s : banner_bookId) {
                if (!s.equals("0")) {
                    int tempId = Integer.parseInt(s);
                    Book book = DataSupport.find(Book.class, tempId);
                    urls.add(book.getImageurl());
                    flag = true;
                }
            }
        } else {
            urls.clear();
            for (int i = 0; i < mBookList.size(); i++) {
                urls.add(mBookList.get(i).getImageurl());
                flag = true;
            }
        }

        //轮播图属性设置
        if (flag) {
            mBanner.setImages(urls)
                    .setBannerStyle(BannerConfig.NUM_INDICATOR)
                    .setDelayTime(1500)
                    .setImageLoader(new GlideImageLoader())
                    .isAutoPlay(true)
                    .setIndicatorGravity(BannerConfig.RIGHT)
                    .setBannerAnimation(Transformer.Default)
                    .start();
        }
    }

    private void queryBook(){
        mBookList.clear();
        mBookList.addAll(DataSupport.limit(10).find(Book.class));
        if (mBookList.size() > 0){
            //显示视图
            urls.clear();
            for (int i = 0; i < mBookList.size(); i++) {
                urls.add(mBookList.get(i).getImageurl());
            }
            bookAdapter.notifyDataSetChanged();
        }else {
            //从网络获取获取数据,先判断列表是否为空,而后获得书籍
            boolean flag = true;
            if (mCategorys.size() > 0) {
                for (int i = 0; i < mCategorys.size() && flag; i++) {
                    BookCategory mCategory = mCategorys.get(i);
                    String id = String.valueOf(mCategory.getCategoryId());
                    //更新书籍并且更新UI
                    if (Util.isNetWorkAvailable(Objects.requireNonNull(context))) {
                        queryBookFromServer(id);
                    } else {
                        flag = false;
                        builder.setMessage("当前无网络,请打开网络")
                                .setPositiveButton("确认", (dialogInterface, i1) -> dialogInterface.dismiss())
                                .show();
                    }
                }
                mBookList.clear();
                mBookList.addAll(DataSupport.findAll(Book.class));
                bookAdapter.notifyDataSetChanged();

                //轮播图属性设置
                urls.clear();
                for (Book book : mBookList) {
                    if (urls.size() < 5 && book != null) {
                        urls.add(book.getImageurl());
                    } else {
                        break;
                    }
                }
            }else {
                queryCategory();
            }
        }
        mBanner.setImages(urls)
                .setBannerStyle(BannerConfig.NUM_INDICATOR)
                .setDelayTime(1500)
                .setImageLoader(new GlideImageLoader())
                .isAutoPlay(true)
                .setIndicatorGravity(BannerConfig.RIGHT)
                .setBannerAnimation(Transformer.Default)
                .start();
    }

    private void queryCategory(){
        mCategorys = DataSupport.findAll(BookCategory.class);
        if (mCategorys.size() > 0){
            queryBook();
        }else {
            //更新书籍种类并且更新UI
            if (Util.isNetWorkAvailable(context)) {
                queryCategoryFromServer();
            } else {
                builder.setMessage("当前无网络可用,请打开网络!")
                        .setPositiveButton("确认", (dialogInterface, i) -> dialogInterface.dismiss())
                        .show();
            }
        }
    }

    private void queryBookFromServer(String id){
        showProgressDialog();
        String address = "http://apis.juhe.cn/goodbook/query?key=" + KEY +
                "&catalog_id=" + id +
                "&pn=" + PN +
                "&rn=" + RN;
        HttpUtil.sendOkhttpRequest(address, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                closeProgressDialog();
                Toast.makeText(context, "加载书籍失败!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseText = Objects.requireNonNull(response.body()).string();
                boolean result;
                result = Util.handleBookResponse(responseText, id);
                if (result){
                    Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                        closeProgressDialog();
                        queryBook();
                    });
                }
            }
        });
    }
    private void queryCategoryFromServer(){
        showProgressDialog();
        String address = "http://apis.juhe.cn/goodbook/catalog?key=" + KEY +
                "&dtype=json";
        HttpUtil.sendOkhttpRequest(address, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                closeProgressDialog();
                Toast.makeText(context, "加载失败!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseText = Objects.requireNonNull(response.body()).string();
                boolean result;
                result = Util.handleCategoryResponse(responseText);
                if (result){
                    Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                        closeProgressDialog();
                        queryBook();
                    });
                }
            }
        });
    }

    private void showProgressDialog(){
        if (mDialog == null){
            mDialog = new ProgressDialog(getContext());
            mDialog.setMessage("正在加载...");
            mDialog.setCanceledOnTouchOutside(false);
        }
        mDialog.show();
    }

    private void closeProgressDialog(){
        if (mDialog != null){
            mDialog.dismiss();
        }
    }
    //
    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load(path).into(imageView);
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            mIsVisible = true;
            lazyload();
        } else {
            mIsVisible = false;
        }
    }

    private void lazyload() {
        //如果都不是,就不加载
        if (!mIsPrepare || !mIsVisible || !mIsFirstLoad) {
            return;
        }
        //从网络加载数据
        loadData();
        //数据加载完毕,防止重复加载
        mIsFirstLoad = false;

    }

    private void queryMessage() {
        //消息适配器初始化
        List<com.thundersoft.bookstore.model.Message> messageList = DataSupport.findAll(com.thundersoft.bookstore.model.Message.class);
        ShowMessageAdapter showMessageAdapter = new ShowMessageAdapter(messageList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        //设置水平滚动
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mMessageView.setLayoutManager(layoutManager);
        mMessageView.setAdapter(showMessageAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mIsVisible = false;
        mIsPrepare = false;
        mIsFirstLoad = true;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //解除绑定
        mBinder.unbind();
    }

    private boolean bannerIsAvailable(String[] id) {
        for (String s : id) {
            if (!s.equals("0")) {
                return true;
            }
        }
        return false;
    }

    private boolean recommendIsAvailable(String[] id) {
        for (String s : id) {
            if (!s.equals("0")) {
                return true;
            }
        }
        return false;
    }
}
