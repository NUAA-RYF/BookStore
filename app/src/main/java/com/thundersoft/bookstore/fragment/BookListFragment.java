package com.thundersoft.bookstore.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.thundersoft.bookstore.R;
import com.thundersoft.bookstore.activity.BookActivity;
import com.thundersoft.bookstore.adapter.BookCategoryAdapter;
import com.thundersoft.bookstore.adapter.BookListAdapter;
import com.thundersoft.bookstore.model.Book;
import com.thundersoft.bookstore.model.BookCategory;
import com.thundersoft.bookstore.util.Util;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import org.jetbrains.annotations.NotNull;
import org.litepal.crud.DataSupport;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BookListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookListFragment extends Fragment {

    private static final String FRAGMENT_TITLE = "title";
    @BindView(R.id.bookCategory_list_listView)
    ListView mCategory;
    @BindView(R.id.book_list_listView)
    ListView mBook;

    //根视图
    private View rootView;

    private Unbinder mBinder;

    private boolean mIsPrepare = false;

    private boolean mIsVisible = false;

    private boolean mIsFirstLoad = true;

    private BookCategoryAdapter adapter;

    private BookListAdapter bookListAdapter;

    private List<Book> mBookLists;

    private List<BookCategory> mCategorys;


    public BookListFragment() {
        // Required empty public constructor
    }


    public static BookListFragment newInstance(String title) {
        BookListFragment fragment = new BookListFragment();
        Bundle args = new Bundle();
        args.putString(FRAGMENT_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mFragment_Title = getArguments().getString(FRAGMENT_TITLE);
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_booklist, container, false);
        }
        mBinder = ButterKnife.bind(this,rootView);
        mIsPrepare = true;
        lazyload();
        return rootView;
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


    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void lazyload() {
        if (!mIsPrepare || !mIsVisible || !mIsFirstLoad) {
            return;
        }
        loadData();
        mIsFirstLoad = false;
    }

    private void loadData() {
        //从管理员处获取可获得的列表
        mCategorys = DataSupport.findAll(BookCategory.class);
        //从管理员处获取可获得的书籍
        String categoryId = "242";
        mBookLists = DataSupport.where("categoryId=" + categoryId).find(Book.class);
        //列表种类不为零,正常显示
        adapter = new BookCategoryAdapter(getContext(), android.R.layout.simple_list_item_1, mCategorys);
        bookListAdapter = new BookListAdapter(getContext(), mBookLists);
        mBook.setAdapter(bookListAdapter);
        mCategory.setAdapter(adapter);
        mCategory.setOnItemClickListener((adapterView, parent, position, id) -> {
            BookCategory category = mCategorys.get(position);
            String currentId = String.valueOf(category.getCategoryId());
            mBookLists.clear();
            List<Book> mCurrentLists = DataSupport.where("categoryId=" + currentId).find(Book.class);
            if (mCurrentLists.size() > 0){
                //若当前列表种类中,书籍为零,则从网络获取
                mBookLists.addAll(mCurrentLists);
            }else{
                //从网络获取书籍信息
                if (Integer.parseInt(currentId) >= 242 && Integer.parseInt(currentId) <= 258){
                    Util.downloadBookFromServer(currentId);
                    mBookLists.clear();
                    mBookLists.addAll(DataSupport.findAll(Book.class));
                    bookListAdapter.notifyDataSetChanged();
                }
            }
            mCurrentLists.clear();
            bookListAdapter.notifyDataSetChanged();
        });
        mBook.setOnItemClickListener((adapterView, parent, position, id) -> {
            Intent intent = new Intent(getContext(), BookActivity.class);
            Book book = mBookLists.get(position);
            //传递book对象
            intent.putExtra("book", new Gson().toJson(book));
            Objects.requireNonNull(getContext()).startActivity(intent);
        });
        if (mCategorys.size() <= 0) {
            //列表种类为零,无法显示,从网络获取
            Util.downloadCategoryFromServer();
            mCategorys.clear();
            mCategorys.addAll(DataSupport.findAll(BookCategory.class));
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mIsFirstLoad = true;
        mIsVisible = false;
        mIsPrepare = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBinder.unbind();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    interface OnFragmentInteractionListener {
    }
}
