package org.kobjects.ktdb

import java.io.InputStream
import java.io.Reader
import java.math.BigDecimal
import java.net.URL
import java.sql.*
import java.sql.Array
import java.util.*

// (C) 2002 by Stefan Haustein 
// Rolandstrasse 27, D-46045 Oberhausen, Germany
// All rights reserved.
//
// For licensing details, please refer to the file "license.txt",
// distributed with this file.
/**
 * @author Stefan Haustein
 */
class ResultSetWrapper

    : ResultSet, ResultSetMetaData {
    var resultSet: DbResultSet? = null

    /**
     * Constructor for ResultSetWrapper.
     */
    constructor(connector: String) {
        try {
            val table = DbManager.connect(connector)
            table!!.open()
            resultSet = table.select(false)
        } catch (e: DbException) {
            throw SQLException(e.toString())
        }
    }

    constructor(resultSet: DbResultSet?) {
        this.resultSet = resultSet
    }


    /**
     * @see java.sql.ResultSet.next
     */
    @Throws(SQLException::class)
    override fun next(): Boolean {
        try {
            return resultSet!!.next()
        } catch (e: DbException) {
            throw SQLException(e.toString())
        }
    }

    /**
     * @see java.sql.ResultSet.close
     */
    @Throws(SQLException::class)
    override fun close() {
        resultSet!!.close()
    }

    /**
     * @see java.sql.ResultSet.wasNull
     */
    @Throws(SQLException::class)
    override fun wasNull(): Boolean {
        throw RuntimeException("wasNull not supported!")
    }

    /**
     * @see java.sql.ResultSet.getString
     */
    @Throws(SQLException::class)
    override fun getString(columnIndex: Int): String {
        return resultSet!!.getString(columnIndex)!!
    }

    /**
     * @see java.sql.ResultSet.getBoolean
     */
    @Throws(SQLException::class)
    override fun getBoolean(columnIndex: Int): Boolean {
        return resultSet!!.getBoolean(columnIndex)
    }

    /**
     * @see java.sql.ResultSet.getByte
     */
    @Throws(SQLException::class)
    override fun getByte(columnIndex: Int): Byte {
        return resultSet!!.getInt(columnIndex).toByte()
    }

    /**
     * @see java.sql.ResultSet.getShort
     */
    @Throws(SQLException::class)
    override fun getShort(columnIndex: Int): Short {
        return resultSet!!.getInt(columnIndex).toShort()
    }

    /**
     * @see java.sql.ResultSet.getInt
     */
    @Throws(SQLException::class)
    override fun getInt(columnIndex: Int): Int {
        return resultSet!!.getInt(columnIndex)
    }

    /**
     * @see java.sql.ResultSet.getLong
     */
    @Throws(SQLException::class)
    override fun getLong(columnIndex: Int): Long {
        return resultSet!!.getLong(columnIndex)
    }

    /**
     * @see java.sql.ResultSet.getFloat
     */
    @Throws(SQLException::class)
    override fun getFloat(columnIndex: Int): Float {
        return (resultSet!!.getObject(columnIndex) as Number)
            .toFloat()
    }

    /**
     * @see java.sql.ResultSet.getDouble
     */
    @Throws(SQLException::class)
    override fun getDouble(columnIndex: Int): Double {
        return (resultSet!!.getObject(columnIndex) as Number)
            .toDouble()
    }

    /**
     * @see java.sql.ResultSet.getBigDecimal
     */
    @Deprecated("")
    @Throws(SQLException::class)
    override fun getBigDecimal(columnIndex: Int, scale: Int): BigDecimal {
        throw RuntimeException("BigDecimal not supported!")
    }

    /**
     * @see java.sql.ResultSet.getBytes
     */
    @Throws(SQLException::class)
    override fun getBytes(columnIndex: Int): ByteArray {
        return null
    }

    /**
     * @see java.sql.ResultSet.getDate
     */
    @Throws(SQLException::class)
    override fun getDate(columnIndex: Int): Date {
        throw RuntimeException("getDate not supported")
    }

    /**
     * @see java.sql.ResultSet.getTime
     */
    @Throws(SQLException::class)
    override fun getTime(columnIndex: Int): Time {
        throw RuntimeException("getTime not supported")
    }

    /**
     * @see java.sql.ResultSet.getTimestamp
     */
    @Throws(SQLException::class)
    override fun getTimestamp(columnIndex: Int): Timestamp {
        throw RuntimeException("getTimestamp not supported")
    }

    /**
     * @see java.sql.ResultSet.getAsciiStream
     */
    @Throws(SQLException::class)
    override fun getAsciiStream(columnIndex: Int): InputStream {
        throw RuntimeException("getAsciiStream not supported")
    }

    /**
     * @see java.sql.ResultSet.getUnicodeStream
     */
    @Deprecated("")
    @Throws(SQLException::class)
    override fun getUnicodeStream(columnIndex: Int): InputStream {
        throw RuntimeException("getUnicodeStream not supported")
    }

    /**
     * @see java.sql.ResultSet.getBinaryStream
     */
    @Throws(SQLException::class)
    override fun getBinaryStream(columnIndex: Int): InputStream {
        return resultSet!!.getBinaryStream(columnIndex)!!
    }

    /**
     * @see java.sql.ResultSet.getString
     */
    @Throws(SQLException::class)
    override fun getString(columnName: String): String {
        return resultSet!!.getString(
            resultSet!!.findColumn(columnName)
        )!!
    }

    /**
     * @see java.sql.ResultSet.getBoolean
     */
    @Throws(SQLException::class)
    override fun getBoolean(columnName: String): Boolean {
        return resultSet!!.getBoolean(
            resultSet!!.findColumn(columnName)
        )
    }

    /**
     * @see java.sql.ResultSet.getByte
     */
    @Throws(SQLException::class)
    override fun getByte(columnName: String): Byte {
        return getByte(resultSet!!.findColumn(columnName))
    }

    /**
     * @see java.sql.ResultSet.getShort
     */
    @Throws(SQLException::class)
    override fun getShort(columnName: String): Short {
        return getShort(resultSet!!.findColumn(columnName))
    }

    /**
     * @see java.sql.ResultSet.getInt
     */
    @Throws(SQLException::class)
    override fun getInt(columnName: String): Int {
        return resultSet!!.getInt(
            resultSet!!.findColumn(columnName)
        )
    }

    /**
     * @see java.sql.ResultSet.getLong
     */
    @Throws(SQLException::class)
    override fun getLong(columnName: String): Long {
        return resultSet!!.getLong(
            resultSet!!.findColumn(columnName)
        )
    }

    /**
     * @see java.sql.ResultSet.getFloat
     */
    @Throws(SQLException::class)
    override fun getFloat(columnName: String): Float {
        return getFloat(resultSet!!.findColumn(columnName))
    }

    /**
     * @see java.sql.ResultSet.getDouble
     */
    @Throws(SQLException::class)
    override fun getDouble(columnName: String): Double {
        return getDouble(resultSet!!.findColumn(columnName))
    }

    /**
     * @see java.sql.ResultSet.getBigDecimal
     */
    @Deprecated("")
    @Throws(SQLException::class)
    override fun getBigDecimal(
        columnName: String,
        scale: Int
    ): BigDecimal {
        return getBigDecimal(findColumn(columnName))
    }

    /**
     * @see java.sql.ResultSet.getBytes
     */
    @Throws(SQLException::class)
    override fun getBytes(columnName: String): ByteArray {
        return getBytes(findColumn(columnName))
    }

    /**
     * @see java.sql.ResultSet.getDate
     */
    @Throws(SQLException::class)
    override fun getDate(columnName: String): Date {
        return getDate(findColumn(columnName))
    }

    /**
     * @see java.sql.ResultSet.getTime
     */
    @Throws(SQLException::class)
    override fun getTime(columnName: String): Time {
        return getTime(findColumn(columnName))
    }

    /**
     * @see java.sql.ResultSet.getTimestamp
     */
    @Throws(SQLException::class)
    override fun getTimestamp(columnName: String): Timestamp {
        return getTimestamp(findColumn(columnName))
    }

    /**
     * @see java.sql.ResultSet.getAsciiStream
     */
    @Throws(SQLException::class)
    override fun getAsciiStream(columnName: String): InputStream {
        return getAsciiStream(findColumn(columnName))
    }

    /**
     * @see java.sql.ResultSet.getUnicodeStream
     */
    @Deprecated("")
    @Throws(SQLException::class)
    override fun getUnicodeStream(columnName: String): InputStream {
        return getUnicodeStream(findColumn(columnName))
    }

    /**
     * @see java.sql.ResultSet.getBinaryStream
     */
    @Throws(SQLException::class)
    override fun getBinaryStream(columnName: String): InputStream {
        return getBinaryStream(findColumn(columnName))
    }

    /**
     * @see java.sql.ResultSet.getWarnings
     */
    @Throws(SQLException::class)
    override fun getWarnings(): SQLWarning {
        throw RuntimeException("getWarnings not supported")
    }

    /**
     * @see java.sql.ResultSet.clearWarnings
     */
    @Throws(SQLException::class)
    override fun clearWarnings() {
        throw RuntimeException("clearWarnings not supported")
    }

    /**
     * @see java.sql.ResultSet.getCursorName
     */
    @Throws(SQLException::class)
    override fun getCursorName(): String {
        throw RuntimeException("getCursorName not supported")
    }

    /**
     * @see java.sql.ResultSet.getMetaData
     */
    @Throws(SQLException::class)
    override fun getMetaData(): ResultSetMetaData {
        return this
    }

    /**
     * @see java.sql.ResultSet.getObject
     */
    @Throws(SQLException::class)
    override fun getObject(columnIndex: Int): Any {
        return resultSet!!.getObject(columnIndex)!!
    }

    /**
     * @see java.sql.ResultSet.getObject
     */
    @Throws(SQLException::class)
    override fun getObject(columnName: String): Any {
        return resultSet!!.getObject(findColumn(columnName))!!
    }

    /**
     * @see java.sql.ResultSet.findColumn
     */
    @Throws(SQLException::class)
    override fun findColumn(columnName: String): Int {
        return resultSet!!.findColumn(columnName)
    }

    /**
     * @see java.sql.ResultSet.getCharacterStream
     */
    @Throws(SQLException::class)
    override fun getCharacterStream(columnIndex: Int): Reader {
        throw RuntimeException("getCharacterStream() not supported")
    }

    /**
     * @see java.sql.ResultSet.getCharacterStream
     */
    @Throws(SQLException::class)
    override fun getCharacterStream(columnName: String): Reader {
        return getCharacterStream(findColumn(columnName))
    }

    /**
     * @see java.sql.ResultSet.getBigDecimal
     */
    @Throws(SQLException::class)
    override fun getBigDecimal(columnIndex: Int): BigDecimal {
        throw RuntimeException("getBigDecimal not supported!")
    }

    /**
     * @see java.sql.ResultSet.getBigDecimal
     */
    @Throws(SQLException::class)
    override fun getBigDecimal(columnName: String): BigDecimal {
        return getBigDecimal(findColumn(columnName))
    }

    /**
     * @see java.sql.ResultSet.isBeforeFirst
     */
    @Throws(SQLException::class)
    override fun isBeforeFirst(): Boolean {
        throw RuntimeException("isBeforeFirst() not supported")
    }

    /**
     * @see java.sql.ResultSet.isAfterLast
     */
    @Throws(SQLException::class)
    override fun isAfterLast(): Boolean {
        return resultSet.getRow() == 0
    }

    /**
     * @see java.sql.ResultSet.isFirst
     */
    @Throws(SQLException::class)
    override fun isFirst(): Boolean {
        return resultSet.getRow() == 1
    }

    /**
     * @see java.sql.ResultSet.isLast
     */
    @Throws(SQLException::class)
    override fun isLast(): Boolean {
        return resultSet!!.isLast
    }

    /**
     * @see java.sql.ResultSet.beforeFirst
     */
    @Throws(SQLException::class)
    override fun beforeFirst() {
        try {
            resultSet!!.beforeFirst()
        } catch (e: DbException) {
            throw SQLException(e.toString())
        }
    }

    /**
     * @see java.sql.ResultSet.afterLast
     */
    @Throws(SQLException::class)
    override fun afterLast() {
        try {
            resultSet!!.absolute(resultSet.getRowCount() + 1)
        } catch (e: DbException) {
            throw SQLException(e.toString())
        }
    }

    /**
     * @see java.sql.ResultSet.first
     */
    @Throws(SQLException::class)
    override fun first(): Boolean {
        try {
            resultSet!!.beforeFirst()
            return resultSet!!.next()
        } catch (e: DbException) {
            throw SQLException(e.toString())
        }
    }

    /**
     * @see java.sql.ResultSet.last
     */
    @Throws(SQLException::class)
    override fun last(): Boolean {
        try {
            if (resultSet.getColumnCount() == 0) return false
            resultSet!!.absolute(resultSet.getColumnCount())
            return true
        } catch (e: DbException) {
            throw SQLException(e.toString())
        }
    }

    /**
     * @see java.sql.ResultSet.getRow
     */
    @Throws(SQLException::class)
    override fun getRow(): Int {
        return resultSet.getRow()
    }

    /**
     * @see java.sql.ResultSet.absolute
     */
    @Throws(SQLException::class)
    override fun absolute(row: Int): Boolean {
        try {
            return resultSet!!.absolute(row)
        } catch (e: DbException) {
            throw SQLException(e.toString())
        }
    }

    /**
     * @see java.sql.ResultSet.relative
     */
    @Throws(SQLException::class)
    override fun relative(rows: Int): Boolean {
        return absolute(resultSet.getRow() + rows)
    }

    /**
     * @see java.sql.ResultSet.previous
     */
    @Throws(SQLException::class)
    override fun previous(): Boolean {
        return relative(-1)
    }

    /**
     * @see java.sql.ResultSet.setFetchDirection
     */
    override fun setFetchDirection(direction: Int) {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.getFetchDirection
     */
    @Throws(SQLException::class)
    override fun getFetchDirection(): Int {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.setFetchSize
     */
    @Throws(SQLException::class)
    override fun setFetchSize(rows: Int) {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.getFetchSize
     */
    @Throws(SQLException::class)
    override fun getFetchSize(): Int {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.getType
     */
    @Throws(SQLException::class)
    override fun getType(): Int {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.getConcurrency
     */
    @Throws(SQLException::class)
    override fun getConcurrency(): Int {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.rowUpdated
     */
    @Throws(SQLException::class)
    override fun rowUpdated(): Boolean {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.rowInserted
     */
    @Throws(SQLException::class)
    override fun rowInserted(): Boolean {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.rowDeleted
     */
    @Throws(SQLException::class)
    override fun rowDeleted(): Boolean {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.updateNull
     */
    @Throws(SQLException::class)
    override fun updateNull(columnIndex: Int) {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.updateBoolean
     */
    @Throws(SQLException::class)
    override fun updateBoolean(columnIndex: Int, x: Boolean) {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.updateByte
     */
    @Throws(SQLException::class)
    override fun updateByte(columnIndex: Int, x: Byte) {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.updateShort
     */
    @Throws(SQLException::class)
    override fun updateShort(columnIndex: Int, x: Short) {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.updateInt
     */
    @Throws(SQLException::class)
    override fun updateInt(columnIndex: Int, x: Int) {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.updateLong
     */
    @Throws(SQLException::class)
    override fun updateLong(columnIndex: Int, x: Long) {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.updateFloat
     */
    @Throws(SQLException::class)
    override fun updateFloat(columnIndex: Int, x: Float) {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.updateDouble
     */
    @Throws(SQLException::class)
    override fun updateDouble(columnIndex: Int, x: Double) {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.updateBigDecimal
     */
    @Throws(SQLException::class)
    override fun updateBigDecimal(columnIndex: Int, x: BigDecimal) {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.updateString
     */
    @Throws(SQLException::class)
    override fun updateString(columnIndex: Int, x: String) {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.updateBytes
     */
    @Throws(SQLException::class)
    override fun updateBytes(columnIndex: Int, x: ByteArray) {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.updateDate
     */
    @Throws(SQLException::class)
    override fun updateDate(columnIndex: Int, x: Date) {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.updateTime
     */
    @Throws(SQLException::class)
    override fun updateTime(columnIndex: Int, x: Time) {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.updateTimestamp
     */
    @Throws(SQLException::class)
    override fun updateTimestamp(columnIndex: Int, x: Timestamp) {
    }

    /**
     * @see java.sql.ResultSet.updateAsciiStream
     */
    @Throws(SQLException::class)
    override fun updateAsciiStream(
        columnIndex: Int,
        x: InputStream,
        length: Int
    ) {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.updateBinaryStream
     */
    @Throws(SQLException::class)
    override fun updateBinaryStream(
        columnIndex: Int,
        x: InputStream,
        length: Int
    ) {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.updateCharacterStream
     */
    @Throws(SQLException::class)
    override fun updateCharacterStream(
        columnIndex: Int,
        x: Reader,
        length: Int
    ) {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.updateObject
     */
    @Throws(SQLException::class)
    override fun updateObject(
        columnIndex: Int,
        x: Any,
        scale: Int
    ) {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.updateObject
     */
    @Throws(SQLException::class)
    override fun updateObject(columnIndex: Int, x: Any) {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.updateNull
     */
    @Throws(SQLException::class)
    override fun updateNull(columnName: String) {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.updateBoolean
     */
    @Throws(SQLException::class)
    override fun updateBoolean(columnName: String, x: Boolean) {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.updateByte
     */
    @Throws(SQLException::class)
    override fun updateByte(columnName: String, x: Byte) {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.updateShort
     */
    @Throws(SQLException::class)
    override fun updateShort(columnName: String, x: Short) {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.updateInt
     */
    @Throws(SQLException::class)
    override fun updateInt(columnName: String, x: Int) {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.updateLong
     */
    @Throws(SQLException::class)
    override fun updateLong(columnName: String, x: Long) {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.updateFloat
     */
    @Throws(SQLException::class)
    override fun updateFloat(columnName: String, x: Float) {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.updateDouble
     */
    @Throws(SQLException::class)
    override fun updateDouble(columnName: String, x: Double) {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.updateBigDecimal
     */
    @Throws(SQLException::class)
    override fun updateBigDecimal(
        columnName: String,
        x: BigDecimal
    ) {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.updateString
     */
    @Throws(SQLException::class)
    override fun updateString(columnName: String, x: String) {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.updateBytes
     */
    @Throws(SQLException::class)
    override fun updateBytes(columnName: String, x: ByteArray) {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.updateDate
     */
    @Throws(SQLException::class)
    override fun updateDate(columnName: String, x: Date) {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.updateTime
     */
    @Throws(SQLException::class)
    override fun updateTime(columnName: String, x: Time) {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.updateTimestamp
     */
    @Throws(SQLException::class)
    override fun updateTimestamp(columnName: String, x: Timestamp) {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.updateAsciiStream
     */
    @Throws(SQLException::class)
    override fun updateAsciiStream(
        columnName: String,
        x: InputStream,
        length: Int
    ) {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.updateBinaryStream
     */
    @Throws(SQLException::class)
    override fun updateBinaryStream(
        columnName: String,
        x: InputStream,
        length: Int
    ) {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.updateCharacterStream
     */
    @Throws(SQLException::class)
    override fun updateCharacterStream(
        columnName: String,
        reader: Reader,
        length: Int
    ) {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.updateObject
     */
    @Throws(SQLException::class)
    override fun updateObject(
        columnName: String,
        x: Any,
        scale: Int
    ) {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.updateObject
     */
    @Throws(SQLException::class)
    override fun updateObject(columnName: String, x: Any) {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.insertRow
     */
    @Throws(SQLException::class)
    override fun insertRow() {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.updateRow
     */
    @Throws(SQLException::class)
    override fun updateRow() {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.deleteRow
     */
    @Throws(SQLException::class)
    override fun deleteRow() {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.refreshRow
     */
    @Throws(SQLException::class)
    override fun refreshRow() {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.cancelRowUpdates
     */
    @Throws(SQLException::class)
    override fun cancelRowUpdates() {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.moveToInsertRow
     */
    @Throws(SQLException::class)
    override fun moveToInsertRow() {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.moveToCurrentRow
     */
    @Throws(SQLException::class)
    override fun moveToCurrentRow() {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.getStatement
     */
    @Throws(SQLException::class)
    override fun getStatement(): Statement {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.getObject
     */
    @Throws(SQLException::class)
    override fun getObject(i: Int, map: Map<*, *>?): Any {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.getRef
     */
    @Throws(SQLException::class)
    override fun getRef(i: Int): Ref {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.getBlob
     */
    @Throws(SQLException::class)
    override fun getBlob(i: Int): Blob {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.getClob
     */
    @Throws(SQLException::class)
    override fun getClob(i: Int): Clob {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.getArray
     */
    @Throws(SQLException::class)
    override fun getArray(i: Int): Array {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.getObject
     */
    @Throws(SQLException::class)
    override fun getObject(colName: String, map: Map<*, *>?): Any {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.getRef
     */
    @Throws(SQLException::class)
    override fun getRef(colName: String): Ref {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.getBlob
     */
    @Throws(SQLException::class)
    override fun getBlob(colName: String): Blob {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.getClob
     */
    @Throws(SQLException::class)
    override fun getClob(colName: String): Clob {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.getArray
     */
    @Throws(SQLException::class)
    override fun getArray(colName: String): Array {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.getDate
     */
    @Throws(SQLException::class)
    override fun getDate(columnIndex: Int, cal: Calendar): Date {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.getDate
     */
    @Throws(SQLException::class)
    override fun getDate(columnName: String, cal: Calendar): Date {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.getTime
     */
    @Throws(SQLException::class)
    override fun getTime(columnIndex: Int, cal: Calendar): Time {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.getTime
     */
    @Throws(SQLException::class)
    override fun getTime(columnName: String, cal: Calendar): Time {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.getTimestamp
     */
    @Throws(SQLException::class)
    override fun getTimestamp(columnIndex: Int, cal: Calendar): Timestamp {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.getTimestamp
     */
    @Throws(SQLException::class)
    override fun getTimestamp(
        columnName: String,
        cal: Calendar
    ): Timestamp {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.getURL
     */
    @Throws(SQLException::class)
    override fun getURL(columnIndex: Int): URL {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.getURL
     */
    @Throws(SQLException::class)
    override fun getURL(columnName: String): URL {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.updateRef
     */
    @Throws(SQLException::class)
    override fun updateRef(columnIndex: Int, x: Ref) {
        throw RuntimeException(NYI)
    }


    /**
     * @see java.sql.ResultSet.updateRef
     */
    @Throws(SQLException::class)
    override fun updateRef(columnName: String, x: Ref) {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.updateBlob
     */
    @Throws(SQLException::class)
    override fun updateBlob(columnIndex: Int, x: Blob) {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.updateBlob
     */
    @Throws(SQLException::class)
    override fun updateBlob(columnName: String, x: Blob) {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.updateClob
     */
    @Throws(SQLException::class)
    override fun updateClob(columnIndex: Int, x: Clob) {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.updateClob
     */
    @Throws(SQLException::class)
    override fun updateClob(columnName: String, x: Clob) {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.updateArray
     */
    @Throws(SQLException::class)
    override fun updateArray(columnIndex: Int, x: Array) {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSet.updateArray
     */
    @Throws(SQLException::class)
    override fun updateArray(columnName: String, x: Array) {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSetMetaData.getColumnCount
     */
    @Throws(SQLException::class)
    override fun getColumnCount(): Int {
        return resultSet.getColumnCount()
    }

    /**
     * @see java.sql.ResultSetMetaData.isAutoIncrement
     */
    @Throws(SQLException::class)
    override fun isAutoIncrement(column: Int): Boolean {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSetMetaData.isCaseSensitive
     */
    @Throws(SQLException::class)
    override fun isCaseSensitive(column: Int): Boolean {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSetMetaData.isSearchable
     */
    @Throws(SQLException::class)
    override fun isSearchable(column: Int): Boolean {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSetMetaData.isCurrency
     */
    @Throws(SQLException::class)
    override fun isCurrency(column: Int): Boolean {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSetMetaData.isNullable
     */
    @Throws(SQLException::class)
    override fun isNullable(column: Int): Int {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSetMetaData.isSigned
     */
    @Throws(SQLException::class)
    override fun isSigned(column: Int): Boolean {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSetMetaData.getColumnDisplaySize
     */
    @Throws(SQLException::class)
    override fun getColumnDisplaySize(column: Int): Int {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSetMetaData.getColumnLabel
     */
    @Throws(SQLException::class)
    override fun getColumnLabel(column: Int): String {
        return resultSet!!.getField(column).label
    }

    /**
     * @see java.sql.ResultSetMetaData.getColumnName
     */
    @Throws(SQLException::class)
    override fun getColumnName(column: Int): String {
        return resultSet!!.getField(column).name
    }

    /**
     * @see java.sql.ResultSetMetaData.getSchemaName
     */
    @Throws(SQLException::class)
    override fun getSchemaName(column: Int): String {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSetMetaData.getPrecision
     */
    @Throws(SQLException::class)
    override fun getPrecision(column: Int): Int {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSetMetaData.getScale
     */
    @Throws(SQLException::class)
    override fun getScale(column: Int): Int {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSetMetaData.getTableName
     */
    @Throws(SQLException::class)
    override fun getTableName(column: Int): String {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSetMetaData.getCatalogName
     */
    @Throws(SQLException::class)
    override fun getCatalogName(column: Int): String {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSetMetaData.getColumnType
     */
    @Throws(SQLException::class)
    override fun getColumnType(column: Int): Int {
        return resultSet!!.getField(column).type
    }

    /**
     * @see java.sql.ResultSetMetaData.getColumnTypeName
     */
    @Throws(SQLException::class)
    override fun getColumnTypeName(column: Int): String {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSetMetaData.isReadOnly
     */
    @Throws(SQLException::class)
    override fun isReadOnly(column: Int): Boolean {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSetMetaData.isWritable
     */
    @Throws(SQLException::class)
    override fun isWritable(column: Int): Boolean {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSetMetaData.isDefinitelyWritable
     */
    @Throws(SQLException::class)
    override fun isDefinitelyWritable(column: Int): Boolean {
        throw RuntimeException(NYI)
    }

    /**
     * @see java.sql.ResultSetMetaData.getColumnClassName
     */
    @Throws(SQLException::class)
    override fun getColumnClassName(column: Int): String {
        throw RuntimeException(NYI)
    }

    companion object {
        const val NYI: String = "Method not (yet) implemented/supported"
    }
}
