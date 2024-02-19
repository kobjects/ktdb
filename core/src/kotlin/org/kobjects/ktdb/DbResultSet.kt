package org.kobjects.ktdb

import org.kobjects.ktdb.DbException
import java.io.InputStream

/**
 *
 * Title:
 *
 * Description:
 *
 * Copyright: Copyright (c) 2002
 *
 * Company:
 * @author Joerg Pleumann
 * @version 1.0
 */
/**
 * To do:
 *
 */

//import javax.microedition.rms.*;
/**
 * A subset of the J2SE SQL Result Set, except from the additional
 * methods getRowCount, getColumnCount, getField, getTable, and getSize   */
interface DbResultSet {
    fun getObject(column: Int): Any?

    fun updateObject(column: Int, value: Any?)

    fun getBoolean(column: Int): Boolean

    fun getBytes(column: Int): ByteArray?

    val columnCount: Int

    fun getField(column: Int): DbColumn

    fun findColumn(name: String?): Int

    fun getInt(column: Int): Int

    fun getLong(column: Int): Long

    /**
     * Returns the size of an binary field. -1 means unknown, -2 means
     * the field is null. Some storages may not make a distinction
     * between -2 and 0  */
    //	public long getSize(int column);
    fun getString(column: Int): String?

    fun getBinaryStream(column: Int): InputStream?

    fun updateBoolean(column: Int, value: Boolean)

    fun updateInt(column: Int, value: Int)

    fun updateLong(column: Int, value: Long)

    fun updateString(column: Int, value: String?)

    fun updateBinaryStream(column: Int, value: InputStream?)

    fun updateBytes(column: Int, data: ByteArray?)

    @Throws(DbException::class)
    fun refreshRow()

    @Throws(DbException::class)
    fun updateRow()

    @Throws(DbException::class)
    fun insertRow()

    @Throws(DbException::class)
    fun moveToInsertRow()

    //    public void insert(Object[] values) throws DbException;
    /**
     * Aktuellen Record l�schen. �ndert nicht die Position innerhalb des
     * Result Sets. Stattdessen wird einfach nur "deleted" auf true gesetzt.
     */
    @Throws(DbException::class)
    fun deleteRow()

    /**
     * Alle Records in der Enumeration l�schen.
     */
    @Throws(DbException::class)
    fun deleteAll()

    val isModified: Boolean

    val isDeleted: Boolean

    /**
     * Returns the table this record belongs to.
     */
    val table: DbTable?

    /**
     * Resets the iterator before the first row.
     */
    @Throws(DbException::class)
    fun beforeFirst()

    val isLast: Boolean

    val isAfterLast: Boolean

    /**
     * Proceeds to the next element in this result set, returning its record ID.
     */
    @Throws(DbException::class)
    fun next(): Boolean


    /* Maps the given column name to a column index 
     * 
    public int findColumn(String name);*/
    /**
     * Returns the total number of rows. Influences by update(), insert() and
     * delete(), in case the Record is kept updated.
     */
    val rowCount: Int

    /**
     * Returns the current row number.
     */
    val row: Int

    /**
     * Jumps to a given row. Please note: Row counting starts with 1
     */
    @Throws(DbException::class)
    fun absolute(row: Int): Boolean

    /**
     * Throws away the record and all resources it has reserved.
     */
    fun close()

    /*
     * returns an int array containing the selected field
     * indices, or null, if all fields are selected 

    public int [] getSelectedFields (); */
    val isKeptUpdated: Boolean

    val isSorted: Boolean
}
