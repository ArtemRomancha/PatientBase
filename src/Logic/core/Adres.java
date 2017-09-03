package Logic.core;

import Logic.utils.MD5Util;
import Logic.utils.connection;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringPropertyBase;

import java.util.HashMap;

/**
 * Created by astures on 12.02.17.
 */
public class Adres {
    private static String _tableName = "info_adress";
    private int _idAdres;
    private String _region;
    private String _city;
    private String _street;
    private String _houseNumber;
    private int _flatNumber;

    public static String getTableName()
    {
        return _tableName;
    }

    public int getIdAdress()
    {
        return _idAdres;
    }

    public String getRegion()
    {
        return _region;
    }

    public void setRegion(String value)
    {
        _region = value;
    }

    public String getCity()
    {
        return _city;
    }

    public void setCity(String value)
    {
        _city = value;
    }

    public String getStreet()
    {
        return _street;
    }

    public void setStreet(String value)
    {
        _street = value;
    }

    public String getHouseNumber()
    {
        return _houseNumber;
    }

    public void setHouseNumber(String value)
    {
        _houseNumber = value;
    }

    public int getFlatNumber()
    {
        return _flatNumber;
    }

    public void setFlatNumber(int value)
    {
        _flatNumber = value;
    }

    public String getFullAdress()
    {
        return _region + ", " + _city + ", " + _street + " " + _houseNumber + ", " + (_flatNumber == -1 ? "" : _flatNumber);
    }

    public String getAdress()
    {
        return _city + ", " + _street + " " + _houseNumber;
    }

    public static HashMap<String, String> getColumns()
    {
        HashMap<String, String> columns = new HashMap<>();
        columns.put("id_adress", null);
        columns.put("region", null);
        columns.put("city", null);
        columns.put("street", null);
        columns.put("house_number", null);
        columns.put("flat_number", null);

        return columns;
    }

    //Read from DB
    public Adres(HashMap<String, String> values)
    {
        if(values.size() != 0) {
            _idAdres = Integer.parseInt(values.get("id_adress"));
            _region = values.get("region");
            _city = values.get("city");
            _street = values.get("street");
            _houseNumber = values.get("house_number");
            _flatNumber = Integer.parseInt(values.get("flat_number"));
        } else {
            return;
        }
    }

    //New user
    public Adres(String region, String city, String street, String houseNumber, int flatNumber)
    {
        _region = region;
        _city = city;
        _street = street;
        _houseNumber = houseNumber;
        _flatNumber = flatNumber;

        Insert();
        _idAdres = connection.LastId(_tableName);
    }

    public static Adres FindAdres(int id)
    {
        return new Adres(connection.SelectOne(_tableName, getColumns(), "WHERE id_adress = " + id));
    }

    private void Insert()
    {
        HashMap values = new HashMap<String, String>();
        values.put("region", _region);
        values.put("city", _city);
        values.put("street", _street);
        values.put("house_number", _houseNumber);
        values.put("flat_number", _flatNumber);
        connection.Insert(_tableName, values);
    }

    public void Update()
    {
        HashMap values = new HashMap<String, String>();
        values.put("region", _region);
        values.put("city", _city);
        values.put("street", _street);
        values.put("house_number", _houseNumber);
        values.put("flat_number", _flatNumber);
        connection.Update(_tableName, values, "WHERE id_adress = " + _idAdres);
    }

    public void Delete()
    {
        connection.Delete(_tableName, "WHERE id_adress = " + _idAdres);
    }
}