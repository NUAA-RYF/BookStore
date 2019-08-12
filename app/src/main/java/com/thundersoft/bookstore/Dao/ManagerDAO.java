package com.thundersoft.bookstore.Dao;

import android.util.Log;

import com.thundersoft.bookstore.model.Manager;
import org.litepal.crud.DataSupport;
import java.util.List;

public class ManagerDAO {

    private static final String TAG = "LoginDao";

    public static Manager login(Manager manager){
        String managerName = manager.getManagerName();
        String managerPassword = manager.getManagerPassword();
        List<Manager> mManagers = DataSupport.where("manageraccount=?",managerName).find(Manager.class);
        if (mManagers.size() > 0){
            for (int i = 0; i < mManagers.size(); i++) {
                Manager currentManager = mManagers.get(i);
                String password = currentManager.getManagerPassword();
                if (managerPassword.equals(password)){
                    return currentManager;
                }
            }
            //账号密码错误
            return null;
        }else {
            //此用户不存在
            return null;
        }
    }

    //通过ID查询信息
    public static Manager getManagerById(int id){
        List<Manager> mManagers = DataSupport.findAll(Manager.class);
        Log.i(TAG, "getManagerById: List Size is " + mManagers.size());
        if (mManagers.size() > 0){
            for (int i = 0; i < mManagers.size(); i++) {
                Manager manager = mManagers.get(i);
                if (id == manager.getId()){
                    Log.i(TAG, "getManagerById: 查询Manager成功!");
                    return manager;
                }
            }
        }
        return null;
    }

    public static void updataManagerById(Manager manager){
        int id = manager.getId();

        Log.i(TAG, "updataManagerById: id = " + id);
        Log.i(TAG, "updataManagerById: name = " + manager.getManagerName());
        Log.i(TAG, "updataManagerById: info = " + manager.getManagerIntroduce());
        manager.update(id);
        Log.i(TAG, "updataManagerById: 更新Manager成功!");
    }

    //注册用户,对数据库进行增加
    public static void register(Manager manager){
        manager.save();
    }

    //判断用户是否存在
    public static boolean accountIsExisted(String account){
        List<Manager> mManagers = DataSupport.where("managername=?",account).find(Manager.class);
        return mManagers.size() <= 0;
    }

}
