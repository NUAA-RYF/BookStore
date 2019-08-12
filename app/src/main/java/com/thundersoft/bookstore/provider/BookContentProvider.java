package com.thundersoft.bookstore.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.thundersoft.bookstore.dbHelper.BookDataBaseHelper;

import org.jetbrains.annotations.NotNull;

public class BookContentProvider extends ContentProvider {

    private static final int BOOK_DIR = 0;

    private static final int BOOK_ITEM = 1;

    private static final int CATEGORY_DIR = 2;

    private static final int CATEGORY_ITEM = 3;

    private static final String AUTHORITY = "com.thundersoft.BookStore.provider";

    private static UriMatcher uriMatcher;

    private BookDataBaseHelper dbHelper;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY,"Book",BOOK_DIR);
        uriMatcher.addURI(AUTHORITY,"Book/#",BOOK_ITEM);
    }
    public BookContentProvider() {
    }

    @Override
    public int delete(@NotNull Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(@NotNull Uri uri) {
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                return "vnd.android.cursor.dir/vnd.com.thundersoft.bookstore.privider.book";
            case BOOK_ITEM:
                return "vnd.android.cursor.dir/vnd.com.thundersoft.bookstore.privider.book";
            case CATEGORY_DIR:
                return "vnd.android.cursor.dir/vnd.com.thundersoft.bookstore.privider.bookcategory";
            case CATEGORY_ITEM:
                return "vnd.android.cursor.dir/vnd.com.thundersoft.bookstore.privider.bookcategory";
        }
        return null;
    }

    @Override
    public Uri insert(@NotNull Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onCreate() {
        dbHelper = new BookDataBaseHelper(getContext(),"BookStore.db",null,2);
        return true;
    }

    @Override
    public Cursor query(@NotNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                cursor = db.query("book",projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);//0为路径，1为id
                cursor = db.query("book",projection,"id=?",new String[]{bookId},null,null,sortOrder);
                break;
            case CATEGORY_DIR:
                cursor = db.query("bookcategory",projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);//0为路径，1为id
                cursor = db.query("bookcategory",projection,"id=?",new String[]{categoryId},null,null,sortOrder);
                break;
            default:
                break;
        }
        return cursor;
    }

    @Override
    public int update(@NotNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
