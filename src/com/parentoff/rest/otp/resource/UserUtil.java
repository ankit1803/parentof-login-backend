package com.parentoff.rest.otp.resource;

import com.google.gson.Gson;
import com.parentoff.rest.db.ParentDAO;
import com.parentoff.rest.db.dao.UserDao;
import com.parentoff.rest.otp.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by pooja on 28/12/16.
 */
public class UserUtil {
    private static ParentDAO<User, String> userDao = new UserDao(User.class);
    public static Logger LOGGER = LoggerFactory
            .getLogger(UserUtil.class);
    private Gson gson = new Gson();


    public User getUser(String userId){
        User dbUser = null;
        try {
            dbUser = userDao.get(userId);
            LOGGER.info("Exiting getData.." + dbUser);
        } catch (Exception e) {
            LOGGER.error("Exception occured in getData ", e);
        }

        return dbUser;
    }

    public User getUserByEmailAppId(String emailId, String appId){
        User dbUser = null;
        try {
            Map<String, String> context = new HashMap<>();
            context.put("email", emailId);
            context.put("appId", appId);

            dbUser = userDao.getOneByQueryId("getUserByMailApp", context);
            LOGGER.info("Exiting getData.." + dbUser);
        } catch (Exception e) {
            LOGGER.error("Exception occured in getData ", e);
        }

        return dbUser;
    }

    public User createUser(String userId){
        User newUser = new User();
        newUser.setUsername(userId);
        try{
            int status = userDao.insert(newUser);
            LOGGER.info("Exiting createUser.." + status);
        }catch (Exception e){
            newUser = null;
            LOGGER.error("Error createUser.." + e.getMessage());
        }

        return newUser;
    }

    public String getJson(User user){
        String jsonString = gson.toJson(user);
        return jsonString;
    }
}
