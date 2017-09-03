package Logic.controllers;

import Logic.utils.connection;

import java.sql.SQLException;

/**
 * Created by astures on 03.02.17.
 */
public class TablesController
{
    public static void CreateAnaesthesiologistTable()
    {
        connection.Execute("CREATE TABLE if not exists 'core_anaesthesiologist' (" +
                "'id_anaesthesiologist' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "'first_name' TEXT, " +
                "'middle_name' TEXT, " +
                "'last_name' TEXT);");
    }

    // --------Уронить таблицу анестезиологов--------
    public static void DropAnaesthesiologistTable()
    {
        connection.Execute("DROP TABLE 'core_anaesthesiologist';");
    }

    // --------Очистить таблицу анестезиологов--------
    public static void ClearAnaesthesiologistTable()
    {
        connection.Execute("DELETE from 'core_anaesthesiologist';");
    }

    // -------------------------------------------------------------------------------------------
    // --------Создание таблицы вложений--------
    public static void CreateAttachmentTable()
    {
        connection.Execute("CREATE TABLE if not exists 'core_attachment' (" +
                "'id_medical_record' INTEGER, " +
                "'file_name' TEXT);");
    }

    // --------Уронить таблицу вложений--------
    public static void DropAttachmentTable()
    {
        connection.Execute("DROP TABLE 'core_attachment';");
    }

    // --------Очистить таблицу вложений--------
    public static void ClearAttachmentTable()
    {
        connection.Execute("DELETE from 'core_attachment';");
    }

    // -------------------------------------------------------------------------------------------
    // --------Создание таблицы докторов--------
    public static void CreateDoctorTable()
    {
        connection.Execute("CREATE TABLE if not exists 'core_doctor' (" +
                "'id_doctor' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "'first_name' TEXT, " +
                "'middle_name' TEXT, " +
                "'last_name' TEXT);");
    }

    // --------Уронить таблицу докторов--------
    public static void DropDoctorTable()
    {
        connection.Execute("DROP TABLE 'core_doctor';");
    }

    // --------Очистить таблицу докторов--------
    public static void ClearDoctorTable()
    {
        connection.Execute("DELETE from 'core_doctor';");
    }

/*    "'id_surgeon' INTEGER, " +
            "'id_anaesthesiologist' INTEGER, " +
            "'id_diagnosis' INTEGER, " +
            "'id_doctor' INTEGER, " +
*/
    // -------------------------------------------------------------------------------------------
    // --------Создание таблицы мед записей--------
    public static void CreateMedicRecordTable()
    {
        connection.Execute("CREATE TABLE if not exists 'core_medical_record' (" +
                "'id_medical_record' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "'id_patient' INTEGER, " +
                "'date_of_create' TEXT, " +
                "'id_surgeon' INTEGER, " +
                "'id_anaesthesiologist' INTEGER, " +
                "'id_doctor' INTEGER, " +
                "'id_diagnosis' INTEGER, " +
                "'notice' TEXT);");
    }

    // --------Уронить таблицу мед записей--------
    public static void DropMedicRecordTable()
    {
        connection.Execute("DROP TABLE 'core_medical_record';");
    }

    // --------Очистить таблицу мед записей--------
    public static void ClearMedicRecordTable()
    {
        connection.Execute("DELETE from 'core_medical_record';");
    }


    // -------------------------------------------------------------------------------------------
    // --------Создание таблицы MKB--------
    public static void CreateMKBTable()
    {
        connection.Execute("CREATE TABLE if not exists 'core_mkb_10' (" +
                "'id_diagnosis' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "'mkb_code' TEXT, " +
                "'description' TEXT);");
    }

    // --------Уронить таблицу MKB--------
    public static void DropMKBTable()
    {
        connection.Execute("DROP TABLE 'core_mkb_10';");
    }

    // --------Очистить таблицу MKB--------
    public static void ClearMKBTable()
    {
        connection.Execute("DELETE from 'core_mkb_10';");
    }

    // -------------------------------------------------------------------------------------------
    // --------Создание таблицы пациентов--------
    public static void CreatePatientTable()
    {
        connection.Execute("CREATE TABLE if not exists 'core_patient' (" +
                "'id_patient' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "'medical_record_no' TEXT, " +
                "'first_name' TEXT, " +
                "'middle_name' TEXT, " +
                "'last_name' TEXT, " +
                "'sex' INTEGER, " +
                "'date_of_birth' TEXT, " +
                "'id_adress' INTEGER, " +
                "'phone' TEXT, " +
                "'notice' TEXT); ");
    }

    // --------Уронить таблицу пациентов--------
    public static void DropPatientTable()
    {
        connection.Execute("DROP TABLE 'core_patient';");
    }

    // --------Очистить таблицу пациентов--------
    public static void ClearPatientTable()
    {
        connection.Execute("DELETE from 'core_patient';");
    }

    // -------------------------------------------------------------------------------------------
    // --------Создание таблицы хирургов--------
    public static void CreateSurgeonTable()
    {
        connection.Execute("CREATE TABLE if not exists 'core_surgeon' (" +
                "'id_surgeon' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "'first_name' TEXT, " +
                "'middle_name' TEXT, " +
                "'last_name' TEXT);");
    }

    // --------Уронить таблицу хирургов--------
    public static void DropSurgeonTable()
    {
        connection.Execute("DROP TABLE 'core_surgeon';");
    }

    // --------Очистить таблицу хирургов--------
    public static void ClearSurgeonTable()
    {
        connection.Execute("DELETE from 'core_surgeon';");
    }

    // -------------------------------------------------------------------------------------------
    // --------Создание таблицы адресов--------
    public static void CreateAdressTable()
    {
        connection.Execute("CREATE TABLE if not exists 'info_adress' (" +
                "'id_adress' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "'region' TEXT, " +
                "'city' TEXT, " +
                "'street' TEXT, " +
                "'house_number' TEXT, " +
                "'flat_number' INTEGER);");
    }

    // --------Уронить таблицу адресов--------
    public static void DropAdressTable()
    {
        connection.Execute("DROP TABLE 'info_adress';");
    }

    // --------Очистить таблицу адресов--------
    public static void ClearAdressTable()
    {
        connection.Execute("DELETE from 'info_adress';");
    }

    // -------------------------------------------------------------------------------------------
    // --------Создание таблицы полов--------
    public static void CreateSexTable()
    {
        connection.Execute("CREATE TABLE if not exists 'info_sex' (" +
                "'id' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "'description' TEXT);");

        connection.Execute("INSERT INTO 'info_sex' ('id', 'description') VALUES (0, 'женский');");
        connection.Execute("INSERT INTO 'info_sex' ('id', 'description') VALUES (1, 'мужской');");
    }

    // --------Уронить таблицу полов--------
    public static void DropSexTable()
    {
        connection.Execute("DROP TABLE 'info_sex';");
    }

    // --------Очистить таблицу полов--------
    public static void ClearSexTable()
    {
        connection.Execute("DELETE from 'info_sex';");
    }

    // -------------------------------------------------------------------------------------------
    // --------Создание таблицы логов--------
    public static void CreateLogTable()
    {
        connection.Execute("CREATE TABLE if not exists 'log' (" +
                "'id_log' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "'id_user' INTEGER, " +
                "'date' TEXT, " +
                "'event' TEXT);");
    }

    // --------Уронить таблицу логов--------
    public static void DropLogTable()
    {
        connection.Execute("DROP TABLE 'log';");
    }

    // --------Очистить таблицу логов--------
    public static void ClearLogTable()
    {
        connection.Execute("DELETE from 'log';");
    }

    // -------------------------------------------------------------------------------------------
    // --------Создание таблицы событий лога--------
    public static void CreateLogEventTable()
    {
        connection.Execute("CREATE TABLE if not exists 'log_event' (" +
                "'id_event' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "'name' TEXT, " +
                "'description' TEXT);");
    }

    // --------Уронить таблицу событий лога--------
    public static void DropLogEventTable()
    {
        connection.Execute("DROP TABLE 'log_event';");
    }

    // --------Очистить таблицу событий лога--------
    public static void ClearLogEventTable()
    {
        connection.Execute("DELETE from 'log_event';");
    }

    // -------------------------------------------------------------------------------------------
    // --------Создание таблицы пользователей--------
    public static void CreateUserTable()
    {
        connection.Execute("CREATE TABLE if not exists 'user' (" +
                "'id_user' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "'login' TEXT, " +
                "'FIO' TEXT, " +
                "'password_hash' TEXT, " +
                "'admin_access' INTEGER, " +
                "'super_admin_access' INTEGER);");
    }

    // --------Уронить таблицу пользователей--------
    public static void DropUserTable()
    {
        connection.Execute("DROP TABLE 'user';");
    }

    // --------Очистить пользователей лога--------
    public static void ClearUserTable()
    {
        connection.Execute("DELETE from 'user';");
    }
}
