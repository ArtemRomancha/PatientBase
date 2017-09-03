package Logic.core;

import Logic.utils.connection;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by astures on 11.02.17.
 */
public class Surgeon extends Medico
{
    private static String _tableName = "core_surgeon";

    public static String getTableName()
    {
        return _tableName;
    }

    public static HashMap<String, String> getColumns()
    {
        HashMap<String, String> columns = new HashMap<>();
        columns.put("id_surgeon", null);
        columns.put("first_name", null);
        columns.put("middle_name", null);
        columns.put("last_name", null);

        return columns;
    }

    public Surgeon(HashMap<String, String> values)
    {
        super(values, "id_surgeon", _tableName);
    }

    //New anaesthesiologist
    public Surgeon(String firstName, String middleName, String lastName)
    {
        super(firstName, middleName, lastName, _tableName);
    }

    public static Surgeon FindSurgeon(int id)
    {
        return new Surgeon(connection.SelectOne(_tableName, getColumns(), "WHERE id_doctor = " + id));
    }
}
