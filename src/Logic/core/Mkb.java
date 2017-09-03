package Logic.core;

import Logic.utils.connection;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by astures on 12.02.17.
 */
public class Mkb extends RecursiveTreeObject<Mkb>
{
    private static String _tableName = "core_mkb_10";
    private int _idDiagnosis;
    private final SimpleStringProperty _mkbCode;
    private final SimpleStringProperty _description;

    public static String getTableName()
    {
        return _tableName;
    }

    public int getId()
    {
        return _idDiagnosis;
    }

    public SimpleStringProperty getMkbCode()
    {
        return _mkbCode;
    }

    public void setMkbCode(String value)
    {
        _mkbCode.setValue(value);
    }

    public SimpleStringProperty getDescription()
    {
        return _description;
    }

    public void setDescription(String value)
    {
        _description.setValue(value);
    }

    public static HashMap<String, String> getColumns()
    {
        HashMap<String, String> columns = new HashMap<>();
        columns.put("id_diagnosis", null);
        columns.put("mkb_code", null);
        columns.put("description", null);

        return columns;
    }

    public Mkb(HashMap<String, String> values)
    {
        _idDiagnosis = Integer.parseInt(values.get("id_diagnosis"));
        _mkbCode = new SimpleStringProperty(values.get("mkb_code"));
        _description = new SimpleStringProperty(values.get("description"));
    }

    //New anaesthesiologist
    public Mkb(String mkbCode, String description)
    {
        _mkbCode = new SimpleStringProperty(mkbCode);
        _description = new SimpleStringProperty(description);
        Insert();
        _idDiagnosis = connection.LastId(_tableName);
        Log.log("INSERT NEW DIAGNOSIS " + toString());
    }

    protected void Insert()
    {
        HashMap values = new HashMap<String, String>();
        values.put("mkb_code", _mkbCode.getValue());
        values.put("description", _description.getValue());
        connection.Insert(_tableName, values);
    }

    public void Update()
    {
        HashMap values = new HashMap<String, String>();
        values.put("mkb_code", _mkbCode.getValue());
        values.put("description", _description.getValue());
        connection.Update(_tableName, values, "WHERE id_diagnosis = " + _idDiagnosis);
        Log.log("UPDATE DIAGNOSIS " + toString());
    }

    public void Delete()
    {
        connection.Delete(_tableName, "WHERE id_diagnosis = " + _idDiagnosis);
        Log.log("DELETE DIAGNOSIS " + toString());
    }

    @Override
    public String toString()
    {
        return _mkbCode.getValue() + " " + _description.getValue();
    }
}
