package com.thundersoft.bookstore.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.thundersoft.bookstore.R;
import com.thundersoft.bookstore.model.Book;
import com.thundersoft.bookstore.model.Management;

import org.litepal.crud.DataSupport;

import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;


public class BannerBookAdapter extends RecyclerView.Adapter<BannerBookAdapter.ViewHolder> {

    private static final String TAG = "BannerBookAdapter";

    private List<Book> mBookList;

    private Context mContext;

    private Management mManagement;

    public BannerBookAdapter(List<Book> bookList) {
        this.mBookList = bookList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_booklist,parent,false);
        final ViewHolder holder = new ViewHolder(view);

        //是否轮播,点击事件逻辑处理
        holder.mBookIsSelected.setOnClickListener(mView -> {

            //获取书籍和管理信息
            Book book = mBookList.get(holder.getAdapterPosition());
            mManagement = DataSupport.find(Management.class, 1);

            if (mManagement == null){
                String temp = "0 0 0 0 0";
                Management management = new Management();
                management.setBannerManagement(temp);
                management.save();
                mManagement = DataSupport.find(Management.class,1);
            }
            if (mManagement.getBannerManagement() == null){
                String temp = "0 0 0 0 0";
                mManagement.setBannerManagement(temp);
                mManagement.update(mManagement.getId());
                mManagement = DataSupport.find(Management.class,1);
            }

            String[] bookId = mManagement.getBannerManagement().split(" ");

            //是否为选中状态
            if (book.getIsBanner() == 1){
                //书籍原本在轮播图中,现在要移除,书籍选中设置为否,管理列表移除书籍
                book.setToDefault("isBanner");
                book.update(book.getId());
                holder.mBookIsSelected.setChecked(false);

                //移除管理列表中的id
                StringBuffer newId = new StringBuffer();
                for (int i = 0; i < bookId.length; i++) {
                    String tempId = String.valueOf(book.getId());
                    if (bookId[i].equals(tempId)) {
                        bookId[i] = "0";
                    }
                    newId.append(bookId[i] + " ");
                }
                mManagement.setBannerManagement(newId.toString());
            }else {

                //添加到轮播图中,判断是否为空并且最大数为5
                if (bookIsAvailable(bookId,5)) {

                    //书籍原本不在轮播图中,现在要加入
                    book.setIsBanner(1);
                    book.update(book.getId());
                    holder.mBookIsSelected.setChecked(true);
                    StringBuffer newId = new StringBuffer();

                    //若长度小于5,则追加,反之将为0的改为书籍id
                    if (bookId.length < 5) {
                        for (int i = 0; i < bookId.length; i++) {
                            newId.append(bookId[i] + " ");
                        }
                        newId.append(book.getId() + "");
                    } else {

                        //将书籍的id改为0
                        boolean flag = true;
                        for (int i = 0; i < bookId.length; i++) {
                            if (bookId[i].equals("0") && flag) {
                                bookId[i] = String.valueOf(book.getId());
                                flag = false;
                            }
                            newId.append(bookId[i] + " ");
                        }
                    }
                    mManagement.setBannerManagement(newId.toString());
                } else {
                    //设置UI控件和数据为false
                    holder.mBookIsSelected.setChecked(false);
                    Toast.makeText(mContext, "轮播图已部署5张.", Toast.LENGTH_SHORT).show();
                    book.setToDefault("isBanner");
                    book.update(book.getId());
                }
            }

            mManagement.update(mManagement.getId());
        });

        holder.mBookIsRecommend.setOnClickListener(mView ->{
            Book book = mBookList.get(holder.getAdapterPosition());
            Management mManagement = DataSupport.find(Management.class,1);
            if (mManagement == null){
                String temp = "0 0 0 0 0 0 0 0 0 0";
                Management management = new Management();
                management.setBookRecommendId(temp);
                management.save();
                mManagement = DataSupport.find(Management.class,1);
            }
            if (mManagement.getBookRecommendId() == null){
                String temp = "0 0 0 0 0 0 0 0 0 0";
                mManagement.setBookRecommendId(temp);
                mManagement.update(mManagement.getId());
                mManagement = DataSupport.find(Management.class,1);
            }

            //推荐书籍逻辑
            String[] bookId = mManagement.getBookRecommendId().split(" ");
            if (book.getIsRecommend() == 1){
                //原本状态为选中,现要进行取消操作,分别进行书籍取消和管理取消
                book.setIsRecommend(-1);
                holder.mBookIsRecommend.setChecked(false);
                StringBuffer newId = new StringBuffer();
                for (int i = 0; i < bookId.length; i++) {
                    if (bookId[i].equals(String.valueOf(book.getId()))){
                        bookId[i] = "0";
                    }
                    newId.append(bookId[i] + " ");
                }
                mManagement.setBookRecommendId(newId.toString());
            }else {
                //原本为未选中状态,现在进行添加操作,先判断管理是否已满
                if (bookIsAvailable(bookId,10)){
                    book.setIsRecommend(1);
                    holder.mBookIsRecommend.setChecked(true);
                    StringBuffer newId = new StringBuffer();
                    boolean flag = true;
                    for (int i = 0; i < bookId.length; i++) {
                        if (bookId[i].equals("0") && flag){
                            bookId[i] = String.valueOf(book.getId());
                            flag = false;
                        }
                        newId.append(bookId[i] + " ");
                    }
                    mManagement.setBookRecommendId(newId.toString());
                }else {
                    Toast.makeText(mContext, "推荐书籍已满10本!", Toast.LENGTH_SHORT).show();
                    book.setIsRecommend(-1);
                    holder.mBookIsRecommend.setChecked(false);
                }
            }
            Log.i(TAG, "onCreateViewHolder: bookId " + mManagement.getBookRecommendId());
            book.update(book.getId());
            mManagement.update(mManagement.getId());
        });

        return holder;
    }

    private boolean bookIsAvailable(String[] bookId,int num) {
        int length = bookId.length;
        if (length > 0 && length < num){
            return true;
        }else {
            for (int i = 0; i < length; i++) {
                if (bookId[i].equals("0")){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = mBookList.get(position);
        Glide.with(mContext).load(book.getImageurl()).into(holder.mBookImage);
        holder.mBookName.setText(book.getBookName());
        holder.mBookIsSelected.setChecked(book.getIsBanner() == 1);
        holder.mBookIsRecommend.setChecked(book.getIsRecommend() == 1);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View bannerBookView;
        @BindView(R.id.banner_book_image)
        ImageView mBookImage;
        @BindView(R.id.banner_book_name)
        TextView mBookName;
        @BindView(R.id.banner_book_isSelected)
        AppCompatCheckBox mBookIsSelected;
        @BindView(R.id.banner_book_isRecommend)
        AppCompatCheckBox mBookIsRecommend;

        public ViewHolder(View view) {
            super(view);
            bannerBookView = view;
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public int getItemCount() {
        return mBookList.size();
    }
}
