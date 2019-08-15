package com.thundersoft.bookstore.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.thundersoft.bookstore.R;
import com.thundersoft.bookstore.activity.ShowMessageActivity;
import com.thundersoft.bookstore.adapter.BookAdapter;
import com.thundersoft.bookstore.adapter.ShowMessageAdapter;
import com.thundersoft.bookstore.model.Book;
import com.thundersoft.bookstore.model.BookCategory;
import com.thundersoft.bookstore.model.Management;
import com.thundersoft.bookstore.util.Util;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import org.jetbrains.annotations.NotNull;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BookRecommendFragment extends Fragment {

    private static final String TAG = "BookRecommendFragment";

    private static final String FRAGMENT_TITLE = "title";
    private static final int UPDATE_BOOK = 2;
    private static final int UPDATE_CATEGORY = 1;

    @BindView(R.id.book_recommend_banner)
    Banner mBanner;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.book_recommend_message)
    RecyclerView mMessageView;

    //根视图
    private View rootView;
    //是否准备
    private boolean mIsPrepare = false;
    //是否可见
    private boolean mIsVisible = false;
    //是否第一次加载
    private boolean mIsFirstLoad = true;

    private List<Book> mBookList;

    private BookAdapter bookAdapter;

    private List<String> urls;

    private Unbinder mBinder;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_CATEGORY:
                    Util.downloadCategoryFromServer();
                    queryBookFromDatabase();
                    break;
                case UPDATE_BOOK:
                    Util.downloadBookFromServer(String.valueOf(msg.arg1));
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
                    mBanner.setImages(urls)
                            .setBannerStyle(BannerConfig.NUM_INDICATOR)
                            .setDelayTime(1500)
                            .setImageLoader(new GlideImageLoader())
                            .isAutoPlay(true)
                            .setIndicatorGravity(BannerConfig.RIGHT)
                            .setBannerAnimation(Transformer.Default)
                            .start();
                    break;
                default:
                    break;
            }
        }
    };

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
        mIsPrepare = true;
        //绑定控件,返回Unbinder实例
        mBinder = ButterKnife.bind(this, rootView);
        lazyload();
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mFragmentTitle = getArguments().getString(FRAGMENT_TITLE);
        }
    }


    //从数据库中查询
    private void queryBookFromDatabase() {
        Management mManagement = DataSupport.find(Management.class, 1);

        if (mManagement == null) {
            Management management = new Management();
            management.setBannerManagement("0 0 0 0 0");
            management.setBookRecommendId("0 0 0 0 0 0 0 0 0 0");
            management.save();
            mManagement = DataSupport.find(Management.class, 1);
        }
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

        //mBookList应为管理员设置推荐书籍
        mBookList = new ArrayList<>();
        String[] recommend_bookId = mManagement.getBookRecommendId().split(" ");
        if (recommendIsAvailable(recommend_bookId)) {
            for (String s : recommend_bookId) {
                if (!s.equals("0")) {
                    Book book = DataSupport.find(Book.class, Integer.parseInt(s));
                    mBookList.add(book);
                }
            }
        } else {
            mBookList = DataSupport.limit(10).find(Book.class);
        }

        //urls轮播图应为管理员设置推荐书籍,应为url地址或者图片id
        urls = new ArrayList<>();
        String[] banner_bookId = mManagement.getBannerManagement().split(" ");
        if (bannerIsAvailable(banner_bookId)) {
            for (String s : banner_bookId) {
                if (!s.equals("0")) {
                    int tempId = Integer.parseInt(s);
                    Book book = DataSupport.find(Book.class, tempId);
                    urls.add(book.getImageurl());
                }
            }
        } else {
            for (int i = 0; i < mBookList.size(); i++) {
                urls.add(mBookList.get(i).getImageurl());
            }
        }

        //轮播图属性设置
        mBanner.setImages(urls)
                .setBannerStyle(BannerConfig.NUM_INDICATOR)
                .setDelayTime(1500)
                .setImageLoader(new GlideImageLoader())
                .isAutoPlay(true)
                .setIndicatorGravity(BannerConfig.RIGHT)
                .setBannerAnimation(Transformer.Default)
                .start();

        if (mBookList.size() > 0) {
            //显示视图
            for (int i = 0; i < mBookList.size(); i++) {
                urls.add(mBookList.get(i).getImageurl());
            }


            GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
            bookAdapter = new BookAdapter(mBookList);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setAdapter(bookAdapter);
        } else {
            //从网络获取获取数据,先判断列表是否为空,而后获得书籍
            List<BookCategory> mCategorys = DataSupport.findAll(BookCategory.class);
            if (mCategorys.size() > 0) {
                for (int i = 0; i < mCategorys.size(); i++) {
                    BookCategory mCategory = mCategorys.get(i);
                    String id = String.valueOf(mCategory.getCategoryId());
                    //更新书籍并且更新UI
                    new Thread(() -> {
                        Message message = new Message();
                        message.what = UPDATE_BOOK;
                        message.arg1 = Integer.parseInt(id);
                        mHandler.sendMessage(message);
                    }).start();
                }
            } else {
                //更新书籍种类并且更新UI
                new Thread(() -> {
                    Message message = new Message();
                    message.what = UPDATE_CATEGORY;
                    mHandler.sendMessage(message);
                }).start();
            }
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

    private void loadData() {
        //加载系统消息通知
        queryMessage();
        //加载数据
        queryBookFromDatabase();
    }

    private void queryMessage(){
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
