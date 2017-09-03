package Logic.core;

import Logic.utils.connection;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by astures on 3.02.17.
 */
public class Medico extends RecursiveTreeObject<Medico>
{
    private static String _idColumn;
    private static String _tableName;
    private int _id;
    private final SimpleStringProperty _firstName;
    private final SimpleStringProperty _middleName;
    private final SimpleStringProperty _lastName;

    public int getId()
    {
        return _id;
    }

    public SimpleStringProperty getFirstName()
    {
        return _firstName;
    }

    public void setFirstName(String value)
    {
        _firstName.set(value);
    }

    public SimpleStringProperty getMiddleName()
    {
        return _middleName;
    }

    public void setMiddleName(String value)
    {
        _middleName.set(value);
    }

    public SimpleStringProperty getLastName()
    {
        return _lastName;
    }

    public void setLastName(String value)
    {
        _lastName.set(value);
    }

    public String getFio()
    {
        return _firstName.getValue() + " " + _middleName.getValue() + " " + _lastName.getValue();
    }

    public Medico(HashMap<String, String> values, String idColumn, String tableName)
    {
        _id = Integer.parseInt(values.get(idColumn));
        _firstName = new SimpleStringProperty(values.get("first_name"));
        _middleName = new SimpleStringProperty(values.get("middle_name"));
        _lastName = new SimpleStringProperty(values.get("last_name"));

        _idColumn = idColumn;
        _tableName = tableName;
    }

    //New anaesthesiologist
    public Medico(String firstName, String middleName, String lastName, String tableName) {
        _firstName = new SimpleStringProperty(firstName);
        _middleName = new SimpleStringProperty(middleName);
        _lastName = new SimpleStringProperty(lastName);
        _tableName = tableName;
        Insert();
        _id = connection.LastId(_tableName);
        Log.log("INSERT NEW MEDICO IN " + _tableName + "id = " + _id);
    }

    protected void Insert()
    {
        HashMap values = new HashMap<String, String>();
        values.put("first_name", _firstName.get());
        values.put("middle_name", _middleName.get());
        values.put("last_name", _lastName.get());
        connection.Insert(_tableName, values);
    }

    public void Update()
    {
        HashMap values = new HashMap<String, String>();
        values.put("first_name", _firstName.get());
        values.put("middle_name", _middleName.get());
        values.put("last_name", _lastName.get());
        connection.Update(_tableName, values, "WHERE " + _idColumn + " = " + _id);
        Log.log("UPDATE MEDICO IN " + _tableName + "id = " + _id);
    }

    public void Delete()
    {
        connection.Delete(_tableName, "WHERE " + _idColumn + " = " + _id);
        Log.log("DELETE MEDICO IN " + _tableName + "id = " + _id);
    }

    public boolean CanDelete()
    {
        ArrayList<HashMap<String, String>> result = connection.SelectMany(MedicalRecord.getTableName(), MedicalRecord.getColumns(), "WHERE " + _idColumn + " = " + _id);
        if(result.size() != 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public String toString()
    {
        return this.getFio();
    }
}
