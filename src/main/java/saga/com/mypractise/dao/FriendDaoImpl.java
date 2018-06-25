package saga.com.mypractise.dao;

import java.util.List;

import saga.com.mypractise.MyApplication;
import saga.com.mypractise.model.Friend;

/**
 * Created by lp on 2018/6/25.
 * 好友SQLite数据库操作
 */

public class FriendDaoImpl {

    /**
     * 添加数据，若重复则覆盖
     * @param friend
     */
    public static  void  insertFriend(Friend friend){
        MyApplication.getDaoInstance().getFriendDao().insertOrReplace(friend);
    }

    /**
     * 删除数据
     * @param id
     */
    public static void deleteFriend(long id) {
        MyApplication.getDaoInstance().getFriendDao().deleteByKey(id);
    }

    /**
     * 更新数据
     * @param friend
     */
    public static void updateFriend(Friend friend) {
        MyApplication.getDaoInstance().getFriendDao().update(friend);
    }


    /**
     * 查询全部数据
     *  (条件查询 queryBuilder().where(...).list())
     */
    public static List<Friend> queryAll() {
        return MyApplication.getDaoInstance().getFriendDao().loadAll();
    }

}
