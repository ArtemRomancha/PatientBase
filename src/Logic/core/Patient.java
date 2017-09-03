package Logic.core;

import Logic.controllers.GeneralController;
import Logic.utils.MD5Util;
import Logic.utils.connection;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by astures on 12.02.17.
 */
public class Patient extends RecursiveTreeObject<Patient>
{
    private static String _tableName = "core_patient";
    private int _idPatient;
    private SimpleStringProperty _medicalRecordNo;
    private SimpleStringProperty _firstName;
    private SimpleStringProperty _middleName;
    private SimpleStringProperty _lastName;
    private SimpleStringProperty _sex;
    private SimpleStringProperty _dateOfBirth;
    private Adres _adress;
    private SimpleStringProperty _phoneNumber;
    private String _notice;
    private ArrayList<MedicalRecord> _medicalRecords;

    public static String getTableName() {
        return _tableName;
    }

    public int getId()
    {
        return _idPatient;
    }

    public SimpleStringProperty getMedicalRecordNo()
    {
        return _medicalRecordNo;
    }

    public void setMedicalRecordNo(String value)
    {
        _medicalRecordNo.setValue(value);
    }

    public SimpleStringProperty getFirstName()
    {
        return _firstName;
    }

    public void setFirstName(String value)
    {
        _firstName.setValue(value);
    }

    public SimpleStringProperty getMiddleName()
    {
        return _middleName;
    }

    public void setMiddleName(String value)
    {
        _middleName.setValue(value);
    }

    public SimpleStringProperty getLastName()
    {
        return _lastName;
    }

    public void setLastName(String value)
    {
        _lastName.setValue(value);
    }

    public SimpleStringProperty getSex()
    {
        return _sex;
    }

    public void setSex(String value)
    {
        _sex.setValue(value);
    }

    public SimpleStringProperty getDateOfBirth() { return _dateOfBirth; }

    public void setdateOfBirth(String value) { _dateOfBirth.setValue(value); }

    public Adres getAdress()
    {
        return _adress;
    }

    public void setAdress(Adres value)
    {
        _adress = value;
    }

    public SimpleStringProperty getPhoneNumber()
    {
        return _phoneNumber;
    }

    public void setPhoneNumber(String value)
    {
        _phoneNumber.setValue(value);
    }

    public String getNotice()
    {
        return _notice;
    }

    public void setNotice(String value)
    {
        _notice = value;
    }

    public SimpleStringProperty getAdressProperty()
    {
        return new SimpleStringProperty(_adress.getAdress());
    }

    public ArrayList<MedicalRecord> getMedicalRecords()
    {
        return _medicalRecords;
    }

    public static HashMap<String, String> getColumns()
    {
        HashMap<String, String> columns = new HashMap<>();
        columns.put("id_patient", null);
        columns.put("medical_record_no", null);
        columns.put("first_name", null);
        columns.put("middle_name", null);
        columns.put("last_name", null);
        columns.put("sex", null);
        columns.put("date_of_birth", null);
        columns.put("id_adress", null);
        columns.put("phone", null);
        columns.put("notice", null);

        return columns;
    }

    //Read from DB
    public Patient(HashMap<String, String> values)
    {
        if(values.size() != 0) {
            _idPatient = Integer.parseInt(values.get("id_patient"));
            _medicalRecordNo = new SimpleStringProperty(values.get("medical_record_no"));
            _firstName = new SimpleStringProperty(values.get("first_name"));
            _middleName = new SimpleStringProperty(values.get("middle_name"));
            _lastName = new SimpleStringProperty(values.get("last_name"));
            _sex = new SimpleStringProperty((Integer.parseInt(values.get("sex")) == 0) ? "женщина" : "мужчина");
            _dateOfBirth = new SimpleStringProperty(values.get("date_of_birth"));
            _adress = Adres.FindAdres(Integer.parseInt(values.get("id_adress")));
            _phoneNumber = new SimpleStringProperty(values.get("phone"));
            _notice = values.get("notice");
        } else {
            return;
        }
    }

    //New user
    public Patient(String medicalRecordNo, String firstName, String middleName, String lastName, String sex, String dateOfBirth, Adres adress, String phone, String notice)
    {
        _medicalRecordNo = new SimpleStringProperty(medicalRecordNo);
        _firstName = new SimpleStringProperty(firstName);
        _middleName = new SimpleStringProperty(middleName);
        _lastName = new SimpleStringProperty(lastName);
        _sex = new SimpleStringProperty(sex);
        _dateOfBirth = new SimpleStringProperty(dateOfBirth);
        _adress = adress;
        _phoneNumber = new SimpleStringProperty(phone);
        _notice = notice;
        Insert();
        _idPatient = connection.LastId(_tableName);
        Log.log("INSERT NEW PATIENT " + toString());
    }

    public static Patient FindPatient(int id)
    {
        return new Patient(connection.SelectOne(_tableName, getColumns(), "WHERE id_patient = " + id));
    }

    public void FillMedicalRecords()
    {
        _medicalRecords = new ArrayList<>();
        ArrayList<HashMap<String, String>> temp = connection.SelectMany(MedicalRecord.getTableName(), MedicalRecord.getColumns(), "WHERE id_patient = " + _idPatient);
        for (HashMap<String, String> row : temp) {
            _medicalRecords.add(new MedicalRecord(row));
            _medicalRecords.get(_medicalRecords.size() - 1).setPatient(this);
        }
    }

    private void Insert()
    {
        HashMap values = new HashMap<String, String>();
        values.put("medical_record_no", _medicalRecordNo.getValue());
        values.put("first_name", _firstName.getValue());
        values.put("middle_name", _middleName.getValue());
        values.put("last_name", _lastName.getValue());
        values.put("sex", (_sex.getValue().equals("мужчина") ? 1 : 0 ));
        values.put("date_of_birth", _dateOfBirth.getValue());
        values.put("id_adress", _adress.getIdAdress());
        values.put("phone", _phoneNumber.getValue());
        values.put("notice", _notice);
        connection.Insert(_tableName, values);
    }

    public void Update()
    {
        HashMap values = new HashMap<String, String>();
        values.put("medical_record_no", _medicalRecordNo.getValue());
        values.put("first_name", _firstName.getValue());
        values.put("middle_name", _middleName.getValue());
        values.put("last_name", _lastName.getValue());
        values.put("sex", (_sex.getValue().equals("мужчина") ? 1 : 0 ));
        values.put("date_of_birth", _dateOfBirth.getValue());
        values.put("id_adress", _adress.getIdAdress());
        values.put("phone", _phoneNumber.getValue());
        values.put("notice", _notice);
        connection.Update(_tableName, values, "WHERE id_patient = " + _idPatient);
        Log.log("UPDATE PATIENT " + toString());
    }

    public void Delete()
    {
        connection.Delete(_tableName, "WHERE id_patient = " + _idPatient);
        _adress.Delete();
        FillMedicalRecords();
        for(MedicalRecord medicalRecord : _medicalRecords) {
            medicalRecord.Delete();
        }
        Log.log("DELETE PATIENT " + toString());
    }

    @Override
    public String toString()
    {
        return _medicalRecordNo.getValue() + " " + _firstName.getValue() + " " + _middleName.getValue() + " " + _lastName.getValue() + " " + _dateOfBirth.getValue() + " " + _phoneNumber.getValue();
    }
}