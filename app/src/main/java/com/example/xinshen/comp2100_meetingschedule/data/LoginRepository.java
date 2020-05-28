package com.example.xinshen.comp2100_meetingschedule.data;

import com.example.xinshen.comp2100_meetingschedule.data.model.UserInfo;
import com.example.xinshen.comp2100_meetingschedule.main.UserInfoCallback;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 *
 * @author Xin Shen, Shaocong Lang
 */
public class LoginRepository {

    private static volatile LoginRepository instance;

    private LoginDataSource dataSource;

    // private constructor : singleton access
    private LoginRepository(LoginDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static LoginRepository getInstance(LoginDataSource dataSource) {
        if (instance == null) {
            instance = new LoginRepository(dataSource);
        }
        return instance;
    }

    public void login(String username, UserInfoCallback callback) {
        // handle login
        dataSource.login(username, callback);
    }

    public void register(UserInfo info, UserInfoCallback callback) {
        dataSource.register(info, callback);
    }

    public void query(String name, UserInfoCallback callback) {
        dataSource.query(name, callback);
    }

    public boolean update(UserInfo info) {
        return dataSource.update(info);
    }
}
