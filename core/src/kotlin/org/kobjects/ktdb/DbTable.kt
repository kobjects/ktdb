package org.kobjects.ktdb

import org.kobjects.ktdb.DbException

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
//import javax.microedition.rms.*;

/**
 * Represents a database table.
 */
interface DbTable {
    /**
     * Is called to connect to a table. Note that the given connector follows
     * the syntax used in the connect() factory method, so the actual table
     * name has to be derived from it.
     */
    @Throws(DbException::class)
    fun connect(connector: String?)

    /**
     * Returns the table's name.
     */
    val name: String?

    /**
     * Returns true if the table exists and false otherwise.
     */
    fun exists(): Boolean

    /**
     * Returns true if the table is open and false otherwise.
     */
    val isOpen: Boolean

    /**
     * Deletes the table. Succeeds if the table doesn't exist. Throws an error
     * if the table cannot be deleted for some reason.
     */
    @Throws(DbException::class)
    fun delete()

    /**
     * Creates the table. This method creates a new physical table or overwrites
     * an existing one. The method can only be called when the table is closed.
     * The new table will not automatically be opened.
     */
    @Throws(DbException::class)
    fun create()

    /**
     * Returns the index of the Id field of this table; -1 if there is no
     * id field.  */
    val idField: Int


    /**
     * Opens the table. Must throw an exception if the table is already open or
     * some other error occurs.
     */
    @Throws(DbException::class)
    fun open()

    /**
     * Closes the table. Always succeeds.
     */
    @Throws(DbException::class)
    fun close()

    /**
     * Adds a field to the table. The table must be closed for this method
     * to succeed.
     */
    @Throws(DbException::class)
    fun addField(name: String?, type: Int): DbColumn?

    /**
     * Returns the total number of fields.
     */
    val columnCount: Int

    /**
     * Returns the field with the given index.
     */
    fun getColumn(i: Int): DbColumn

    /**
     * Returns the index of the given field
     * or -1 if the field does not exist.
     * Please note that field indices start with 1.
     */
    fun findColumn(name: String?): Int

    /**
     * Select all records. This is a convenience method. The usual rules for
     * the "big" select() apply.
     */
    @Throws(DbException::class)
    fun select(updated: Boolean): DbResultSet?


    /**
     * Select the given field indices from all records for which the
     * given condition evaluates to true. If the given orderField is a
     * valid field number, the result set is sorted according to that
     * field. If the orderField is -1, the result set is sorted
     * according to the records' IDs. The order can be reversed using
     * the correspoding parameter. The last parameter decides whether
     * the whole result set is kept updated or not. The returned
     * result set is positioned before the first record.   */
    @Throws(DbException::class)
    fun select(
        fields: IntArray?, filter: DbCondition?,
        orderBy: IntArray?, reverse: Boolean,
        updated: Boolean
    ): DbResultSet?
}






