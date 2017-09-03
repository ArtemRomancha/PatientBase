package Logic.core;

import Logic.utils.connection;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by astures on 11.02.17.
 */
public class Anaesthesiologist extends Medico
{
    private static String _tableName = "core_anaesthesiologist";

    public static String getTableName()
    {
        return _tableName;
    }

    public static HashMap<String, String> getColumns()
    {
        HashMap<String, String> columns = new HashMap<>();
        columns.put("id_anaesthesiologist", null);
        columns.put("first_name", null);
        columns.put("middle_name", null);
        columns.put("last_name", null);

        return columns;
    }

    public Anaesthesiologist(HashMap<String, String> values)
    {
        super(values, "id_anaesthesiologist", _tableName);
    }

    //New anaesthesiologist
    public Anaesthesiologist(String firstName, String middleName, String lastName)
    {
        super(firstName, middleName, lastName, _tableName);
    }

    public static Anaesthesiologist FindAnaesthesiologist(int id)
    {
        return new Anaesthesiologist(connection.SelectOne(_tableName, getColumns(), "WHERE id_anaesthesiologist = " + id));
    }
}
