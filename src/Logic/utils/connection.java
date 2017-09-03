package Logic.utils;

/**
 * Created by astures on 03.02.17.
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class connection {
    public static Connection conn;
    public static Statement statmt;
    public static ResultSet resSet;

    // --------ПОДКЛЮЧЕНИЕ К БАЗЕ ДАННЫХ--------
    public static void Connect(){
        try {
            conn = null;
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:Patients.db");
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public static void Update(String tableName, HashMap<String, String> newValues, String condition) {
        Connect();
        String statement = "UPDATE '" + tableName + "' SET "; //Формирование начала запроса
        for (Map.Entry entry : newValues.entrySet()) {
            statement += entry.getKey().toString() + " = '" + entry.getValue().toString() + "', "; //Добавляем новые значения ключ = значение через запятую
        }
        Execute(statement.substring(0, statement.length() - 2) + " " + condition + ";"); //Выыполняем запрос
        CloseDB();
    }

    public static void Insert(String tableName, HashMap<String, String> tableValues) {
        Connect();
        String statement = "INSERT INTO '" + tableName + "' ("; //Формирование начала запроса
        String keys = ""; //Формирование строки с названиями вставляемых полей
        String values = ""; //Формирование строки со значениями вставляемых полей
        for (Map.Entry entry : tableValues.entrySet()) {
            keys += "'" + entry.getKey().toString() + "', "; //Добавляем новый ключ в кавычках + запятая
            values += "'" + entry.getValue().toString() + "', "; //Добавляем соответствующее значение в кавычках + запятая
        }
        Execute(statement + keys.substring(0, keys.length() - 2) + ") VALUES (" + values.substring(0, values.length() - 2) + ");"); //Выыполняем запрос
        CloseDB();
    }

    public static HashMap<String, String> SelectOne(String tableName, HashMap<String, String> columns, String condition) {
        HashMap<String, String> result = new HashMap<>();

        Connect();
        result = new HashMap<>();
        try {
            statmt = conn.createStatement();
            resSet = statmt.executeQuery("SELECT * FROM '" + tableName + "' " + condition + ";");
            if (resSet.next()) {
                for (String key : columns.keySet()) {
                    result.put(key, resSet.getString(key));
                }
            }
            CloseDB();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return result;
    }

    public static ArrayList<HashMap<String, String>> SelectMany(String tableName, HashMap<String, String> columns, String condition) {
        ArrayList<HashMap<String, String>> results = new ArrayList<>();
        Connect();
        results = new ArrayList<>();
        try {
            statmt = conn.createStatement();
            resSet = statmt.executeQuery("SELECT * FROM '" + tableName + "' " + condition + "; ");
            while (resSet.next()) {
                HashMap<String, String> temp = new HashMap<>();
                for (String key : columns.keySet()) {
                    temp.put(key, resSet.getString(key));
                }
                results.add(temp);
            }
            CloseDB();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return results;
    }

    public static int LastId(String tableName)
    {
        try {
            Connect();
            statmt = conn.createStatement();
            resSet = statmt.executeQuery("SELECT * from SQLITE_SEQUENCE WHERE name = '" + tableName + "';"); //???????????????? ;
            while (resSet.next()) {
                int result = resSet.getInt("seq");
                statmt.close();
                resSet.close();
                CloseDB();
                return result;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        CloseDB();
        return -1;
    }

    public static void Delete(String tableName, String condition) {
        Connect();
        String statement = "DELETE FROM '" + tableName + "' " + condition;
        Execute(statement);
        CloseDB();
    }

    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!PRIVATE MUST BE
    public static void Execute(String statement) {
        Connect();
        try {
            statmt = conn.createStatement();
            statmt.execute(statement);
            statmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        CloseDB();
    }

    public static void CloseDB()
    {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
