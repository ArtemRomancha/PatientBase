package Logic.core;

import Logic.controllers.GeneralController;
import Logic.utils.connection;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by astures on 12.02.17.
 */

public class Log extends RecursiveTreeObject<Log>
{
    private static String _tableName = "log";
    private int _idLog;
    private int _idUser;
    private SimpleStringProperty _date;
    private SimpleStringProperty _event;
    private SimpleStringProperty _user;

    public static String getTableName() {
        return _tableName;
    }

    public int getIdLog()
    {
        return _idLog;
    }

    public SimpleStringProperty getDate()
    {
        return _date;
    }

    public SimpleStringProperty getEvent() { return _event; }

    public SimpleStringProperty getUser()
    {
        return _user;
    }

    public static HashMap<String, String> getColumns()
    {
        HashMap<String, String> columns = new HashMap<>();
        columns.put("id_log", null);
        columns.put("id_user", null);
        columns.put("date", null);
        columns.put("event", null);

        return columns;
    }

    public Log(HashMap<String, String> values)
    {
        if(values.size() != 0) {
            _idLog = Integer.parseInt(values.get("id_log"));
            _idUser = Integer.parseInt(values.get("id_user"));
            _user = new SimpleStringProperty(new User(connection.SelectOne(User.getTableName(), User.getColumns(), "WHERE id_user = " + _idUser)).getLogin().getValue());
            _date = new SimpleStringProperty(values.get("date"));
            _event = new SimpleStringProperty(values.get("event"));
        } else {
            return;
        }
    }

    public Log(String event)
    {
        if(!GeneralController.getCurrentUser().isNull()) {
            _idUser = GeneralController.getCurrentUser().getIdUser();
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        _date = new SimpleStringProperty(dateFormat.format(new Date()));
        _event = new SimpleStringProperty(event);
        Insert();
    }

    private void Insert()
    {
        HashMap values = new HashMap<String, String>();
        values.put("id_user", _idUser);
        values.put("date", _date.getValue());
        values.put("event", _event.getValue());
        connection.Insert(_tableName, values);
    }

    public static void log(String event)
    {
        new Log(event);
    }

    @Override
    public String toString() {
        return _user + " " + _date.getValue() + " " + _event.getValue();
    }
}
