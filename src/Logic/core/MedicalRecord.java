package Logic.core;

import Logic.controllers.GeneralController;
import Logic.utils.MD5Util;
import Logic.utils.connection;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by AstureS on 28.02.2017.
 */
public class MedicalRecord
{
    private static String _tableName = "core_medical_record";
    private int _id;
    private String _dateOfCreate;
    private Patient _patient;
    private Surgeon _surgeon;
    private Anaesthesiologist _anaesthesiologist;
    private Doctor _doctor;
    private Mkb _diagnosis;
    private String _notice;
    ArrayList<Attachment> _attachments;

    public static String getTableName()
    {
        return _tableName;
    }

    public int getId()
    {
        return _id;
    }

    public String getDateOfCreate()
    {
        return _dateOfCreate;
    }

    public void setDateOfCreate(String value)
    {
        _dateOfCreate = value;
    }

    public void setPatient(Patient value)
    {
        _patient = value;
    }

    public Surgeon getSurgeon()
    {
        return _surgeon;
    }

    public void setSurgeon(Surgeon value)
    {
        _surgeon = value;
    }

    public Anaesthesiologist getAnaesthesiologist()
    {
        return _anaesthesiologist;
    }

    public void setAnaesthesiologist(Anaesthesiologist value)
    {
        _anaesthesiologist = value;
    }

    public Doctor getDoctor()
    {
        return _doctor;
    }

    public void setDoctor(Doctor value)
    {
        _doctor = value;
    }

    public Mkb getDiagnosis()
    {
        return _diagnosis;
    }

    public void setDiagnosis(Mkb value)
    {
        _diagnosis = value;
    }

    public String getNotice()
    {
        return _notice;
    }

    public void setNotice(String value)
    {
        _notice = value;
    }

    public ArrayList<Attachment> getAttachments()
    {
        return _attachments;
    }

    public static HashMap<String, String> getColumns()
    {
        HashMap<String, String> columns = new HashMap<>();
        columns.put("id_medical_record", null);
        columns.put("id_patient", null);
        columns.put("date_of_create", null);
        columns.put("id_surgeon", null);
        columns.put("id_anaesthesiologist", null);
        columns.put("id_doctor", null);
        columns.put("id_diagnosis", null);
        columns.put("notice", null);

        return columns;
    }

    public MedicalRecord(HashMap<String, String> values)
    {
        _id = Integer.parseInt(values.get("id_medical_record"));
        _dateOfCreate = values.get("date_of_create");
        _surgeon = new Surgeon(connection.SelectOne(Surgeon.getTableName(), Surgeon.getColumns(), "WHERE id_surgeon = " + values.get("id_surgeon")));
        _anaesthesiologist = new Anaesthesiologist(connection.SelectOne(Anaesthesiologist.getTableName(), Anaesthesiologist.getColumns(), "WHERE id_anaesthesiologist = " + values.get("id_anaesthesiologist")));
        _doctor =new Doctor(connection.SelectOne(Doctor.getTableName(), Doctor.getColumns(), "WHERE id_doctor = " + values.get("id_doctor")));
        _diagnosis = new Mkb(connection.SelectOne(Mkb.getTableName(), Mkb.getColumns(), "WHERE id_diagnosis = " + values.get("id_diagnosis")));
        _notice = values.get("notice");
    }

    //New anaesthesiologist
    public MedicalRecord(String dateOfCreate, Patient patient, Surgeon surgeon, Anaesthesiologist anaesthesiologist, Doctor doctor, Mkb diagnosis, String notice) {
        _dateOfCreate = dateOfCreate;
        _patient = patient;
        _surgeon = surgeon;
        _anaesthesiologist = anaesthesiologist;
        _doctor = doctor;
        _diagnosis = diagnosis;
        _notice = notice;

        Insert();
        _id = connection.LastId(_tableName);
        Log.log("INSERT NEW MEDICAL RECORD id: " + _id + " PATIENT: " + _patient.toString());
    }

    public void FillAttachments() {
        _attachments = new ArrayList<>();
        ArrayList<HashMap<String, String>> temp = connection.SelectMany(Attachment.getTableName(), Attachment.getColumns(), "WHERE id_medical_record = " + _id);
        for (HashMap<String, String> row : temp) {
            _attachments.add(new Attachment(row));
        }
    }

    public Attachment FindAttachment(String fileName)
    {
        for(Attachment attachment : _attachments) {
            if(attachment.getFileName().equals(fileName)) {
                return attachment;
            }
        }
        return null;
    }

    public void RemoveAttachments()
    {
        FillAttachments();
        for(Attachment attachment : _attachments) {
                attachment.Delete();
        }
    }

    private void Insert()
    {
        HashMap values = new HashMap<String, String>();
        values.put("date_of_create", _dateOfCreate);
        values.put("id_patient", _patient.getId());
        values.put("id_surgeon", _surgeon.getId());
        values.put("id_anaesthesiologist", _anaesthesiologist.getId());
        values.put("id_doctor", _doctor.getId());
        values.put("id_diagnosis", _diagnosis.getId());
        values.put("notice",_notice);
        connection.Insert(_tableName, values);
    }

    public void Update()
    {
        HashMap values = new HashMap<String, String>();
        values.put("date_of_create", _dateOfCreate);
        values.put("id_patient", _patient.getId());
        values.put("id_surgeon", _surgeon.getId());
        values.put("id_anaesthesiologist", _anaesthesiologist.getId());
        values.put("id_doctor", _doctor.getId());
        values.put("id_diagnosis", _diagnosis.getId());
        values.put("notice",_notice);
        connection.Update(_tableName, values, "WHERE id_medical_record = " + _id);
        Log.log("UPDATE MEDICAL RECORD id: " + _id + " PATIENT: " + _patient.toString());
    }

    public void Delete()
    {
        connection.Delete(_tableName, "WHERE id_medical_record = " + _id);
        RemoveAttachments();
        Log.log("DELETE MEDICAL RECORD id: " + _id + " PATIENT: " + _patient.toString());
    }
}
