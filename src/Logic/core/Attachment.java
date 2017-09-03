package Logic.core;

import Logic.utils.connection;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by astures on 12.02.17.
 */
public class Attachment
{
    private static String _tableName = "core_attachment";
    private int _idMedicalRecord;
    private String _fileName;

    public static String getTableName()
    {
        return _tableName;
    }

    public int getIdPatient()
    {
        return _idMedicalRecord;
    }

    public String getFileName()
    {
        return _fileName;
    }

    public static HashMap<String, String> getColumns()
    {
        HashMap<String, String> columns = new HashMap<>();
        columns.put("id_medical_record", null);
        columns.put("file_name", null);

        return columns;
    }

    public Attachment(HashMap<String, String> values)
    {
        _idMedicalRecord = Integer.parseInt(values.get("id_medical_record"));
        _fileName = values.get("file_name");
    }

    public Attachment(int idMedicalRecord, String fileName)
    {
        _idMedicalRecord = idMedicalRecord;
        _fileName = fileName;
        Insert();
    }

    private void Insert()
    {
        HashMap values = new HashMap<String, String>();
        values.put("id_medical_record", _idMedicalRecord);
        values.put("file_name", _fileName);
        connection.Insert(_tableName, values);
        Log.log("INSERT NEW ATTACHMENT record: " + _idMedicalRecord + " file_name: " + _fileName);
    }

    public void Delete()
    {
        connection.Delete(_tableName, "WHERE id_medical_record = " + _idMedicalRecord + " AND file_name = '" + _fileName + "'");
        File file = new File(_fileName);
        file.delete();
        Log.log("DELETE  ATTACHMENT record: " + _idMedicalRecord + " file_name: " + _fileName);
    }
}
