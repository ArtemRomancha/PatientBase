package Logic.core;

import Logic.utils.connection;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by astures on 11.02.17.
 */
public class Doctor extends Medico
{
    private static String _tableName = "core_doctor";

    public static String getTableName()
    {
        return _tableName;
    }

    public static HashMap<String, String> getColumns()
    {
        HashMap<String, String> columns = new HashMap<>();
        columns.put("id_doctor", null);
        columns.put("first_name", null);
        columns.put("middle_name", null);
        columns.put("last_name", null);

        return columns;
    }

    public Doctor(HashMap<String, String> values)
    {
        super(values, "id_doctor", _tableName);
    }

    //New anaesthesiologist
    public Doctor(String firstName, String middleName, String lastName)
    {
        super(firstName, middleName, lastName, _tableName);
    }

    public static Doctor FindDoctor(int id)
    {
        return new Doctor(connection.SelectOne(_tableName, getColumns(), "WHERE id_doctor = " + id));
    }
}
