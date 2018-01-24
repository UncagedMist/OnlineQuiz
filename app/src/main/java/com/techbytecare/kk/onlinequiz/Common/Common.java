package com.techbytecare.kk.onlinequiz.Common;

import com.techbytecare.kk.onlinequiz.Model.Question;
import com.techbytecare.kk.onlinequiz.Model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kundan on 12/24/2017.
 */

public class Common {

    public static String categoryId, categoryName;
    public static User currentUser;

    public static final String USER_KEY = "User";
    public static final String PWD_KEY = "Password";

    public static List<Question> questionList = new ArrayList<>();

    public static final String STR_PUSH = "pushNotification";
}
