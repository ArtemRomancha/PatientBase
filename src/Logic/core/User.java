package Logic.core;

import Logic.controllers.GeneralController;
import Logic.utils.MD5Util;
import Logic.utils.connection;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import java.util.HashMap;

/**
 * Created by astures on 03.02.17.
 */
public class User extends RecursiveTreeObject<User>
{
    private static String _tableName = "user";
    private int _idUser;
    private SimpleStringProperty _login;
    private SimpleStringProperty _fio;
    private SimpleStringProperty _password;
    private SimpleBooleanProperty _adminAccess;
    private boolean _superAdminAccess;

    public static String getTableName() {
        return _tableName;
    }

    public int getIdUser()
    {
        return _idUser;
    }

    public SimpleStringProperty getLogin()
    {
        return _login;
    }

    public SimpleStringProperty getFio() { return _fio; }

    public void setFio(String value)
    {
        _fio.setValue(value);
    }

    public SimpleBooleanProperty getAdminAccess()
    {
        return _adminAccess;
    }

    public void setAdminAccess(boolean value)
    {
        _adminAccess.setValue(value);
    }

    public boolean getSuperAdminAccess()
    {
        return _superAdminAccess;
    }

    public void setSuperAdminAccess(boolean value)
    {
        _superAdminAccess = value;
    }

    public boolean isNull()
    {
        return _login == null ? true : false;
    }

    public static HashMap<String, String> getColumns()
    {
        HashMap<String, String> columns = new HashMap<>();
        columns.put("id_user", null);
        columns.put("login", null);
        columns.put("fio", null);
        columns.put("password_hash", null);
        columns.put("admin_access", null);
        columns.put("super_admin_access", null);

        return columns;
    }

    //Read from DB
    public User(HashMap<String, String> values)
    {
        if(values.size() != 0) {
            _idUser = Integer.parseInt(values.get("id_user"));
            _login = new SimpleStringProperty(values.get("login"));
            _fio = new SimpleStringProperty(values.get("fio"));
            _password = new SimpleStringProperty(values.get("password_hash"));
            _adminAccess = new SimpleBooleanProperty((Integer.parseInt(values.get("admin_access")) == 0) ? false : true);
            _superAdminAccess = (Integer.parseInt(values.get("super_admin_access")) == 0) ? false : true;
        } else {
            return;
        }
    }

    //New user
    public User(String login, String fio, String password, boolean adminAccess, boolean superAdminAccess)
    {
        _login = new SimpleStringProperty(login);
        _fio = new SimpleStringProperty(fio);
        _password = new SimpleStringProperty(MD5Util.md5Custom(password));
        _adminAccess = new SimpleBooleanProperty(adminAccess);
        _superAdminAccess = superAdminAccess;
        Insert();
    }

    public static User FindUser(String login)
    {
        return new User(connection.SelectOne(_tableName, getColumns(), "WHERE login = '" + login + "'"));
    }

    private void Insert()
    {
        HashMap values = new HashMap<String, String>();
        values.put("login", _login.getValue());
        values.put("fio", _fio.getValue());
        values.put("password_hash", _password.getValue());
        values.put("admin_access", _adminAccess.getValue() ? 1 : 0);
        values.put("super_admin_access", _superAdminAccess ? 1 : 0);
        connection.Insert(_tableName, values);
        Log.log("INSERT NEW USER " + _login.getValue());
    }

    public void Update()
    {
        HashMap values = new HashMap<String, String>();
        values.put("login", _login.getValue());
        values.put("fio", _fio.getValue());
        values.put("password_hash", _password.getValue());
        values.put("admin_access", _adminAccess.getValue() ? 1 : 0);
        values.put("super_admin_access", _superAdminAccess ? 1 : 0);
        connection.Update(_tableName, values, "WHERE id_user = " + _idUser);
        Log.log("UPDATE USER " + _login.getValue());
    }

    public void Delete()
    {
        connection.Delete(_tableName, "WHERE id_user = " + _idUser);
        Log.log("DELETE USER " + _login.getValue());
    }

    public static boolean CheckLogin(String login)
    {
        HashMap<String, String> result = connection.SelectOne(_tableName, getColumns(), "WHERE login = '" + login + "'");
        return result.size() == 0 ? true : false;
    }

    public boolean ValidatePassword(String password)
    {
        return _password.getValue().equals(MD5Util.md5Custom(password));
    }

    public void ChangePassword(String newPassword)
    {
        _password.setValue(MD5Util.md5Custom(newPassword));
        Update();
    }

    public void ResetPassword()
    {
        _password.setValue(MD5Util.md5Custom(GeneralController.getDefaultPassword()));
        Update();
    }

    @Override
    public String toString() {
        return _login + " " + _fio.getValue();
    }
}
