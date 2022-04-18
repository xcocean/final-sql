package top.lingkang.finalsql.utils;

import java.math.BigDecimal;
import java.sql.*;


/**
 * @author lingkang
 * Created by 2022/4/18
 */
public class SetValueUtils {
    public static void setParam(PreparedStatement statement,int index,Object x) throws SQLException {
        if (x == null) {
            statement.setNull(index, 1111);
        } else if (x instanceof String) {
            statement.setString(index, (String)x);
        } else if (x instanceof BigDecimal) {
            statement.setBigDecimal(index, (BigDecimal)x);
        } else if (x instanceof Short) {
            statement.setShort(index, (Short)x);
        } else if (x instanceof Integer) {
            statement.setInt(index, (Integer)x);
        } else if (x instanceof Long) {
            statement.setLong(index, (Long)x);
        } else if (x instanceof Float) {
            statement.setFloat(index, (Float)x);
        } else if (x instanceof Double) {
            statement.setDouble(index, (Double)x);
        } else if (x instanceof byte[]) {
            statement.setBytes(index, (byte[])((byte[])x));
        } else if (x instanceof Date) {
            statement.setDate(index, (Date)x);
        } else if (x instanceof Time) {
            statement.setTime(index, (Time)x);
        } else if (x instanceof Timestamp) {
            statement.setTimestamp(index, (Timestamp)x);
        } else if (x instanceof Boolean) {
            statement.setBoolean(index, (Boolean)x);
        } else if (x instanceof Byte) {
            statement.setByte(index, (Byte)x);
        } else if (x instanceof Blob) {
            statement.setBlob(index, (Blob)x);
        } else if (x instanceof Clob) {
            statement.setClob(index, (Clob)x);
        } else if (x instanceof Array) {
            statement.setArray(index, (Array)x);
        } else {

        }
    }
}
