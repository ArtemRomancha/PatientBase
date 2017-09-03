package Logic.controllers;

import Logic.core.User;

/**
 * Created by astures on 21.02.17.
 */
public class GeneralController
{
    private static User _currentUser;
    private static String defaultPassword = "123456789";
    private static String dbName = "Patient.db";

    public static User getCurrentUser()
    {
        return _currentUser;
    }

    public static void setCurrenUser(User value)
    {
        _currentUser = value;
    }

    public static String getDefaultPassword()
    {
        return defaultPassword;
    }

    public static String getDbName()
    {
        return dbName;
    }
}
