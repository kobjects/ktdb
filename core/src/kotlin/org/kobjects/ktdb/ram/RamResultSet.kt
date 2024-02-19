package org.kobjects.ktdb.ram

import org.kobjects.ktdb.ram.RamTable
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.*

open class RamResultSet(
    table: RamTable,
    selection: Vector<*>,
    selectedFields: IntArray?
) : DbResultSet {
    protected var selection: Vector<*>

    /** current row in selection (first index is 1)  */
    var row: Int = 0
        protected set

    /** 0 based index in table.records  */
    protected var recordIndex: Int = 0 // index in records vector

    protected var table: RamTable

    /** (complete) copy of current values  */
    protected var values: Array<Any?>

    /*
		public void insert(Object[] values) throws DbException {
			moveToInsertRow();

			for (int i = 0; i < values.length; i++) {
				updateObject(i, values[i]);
			}
		}
	*/
    var isModified: Boolean = false
        protected set
    var isDeleted: Boolean = false
    var selectedFields: IntArray

    init {
        var selectedFields = selectedFields
        this.table = table
        this.selection = selection

        if (selectedFields == null) {
            selectedFields = IntArray(table.getColumnCount())
            for (i in 1..table.getColumnCount()) selectedFields[i - 1] = i
        }

        println(
            "first selected field: " + selectedFields[0]
        )

        this.selectedFields = selectedFields

        values = arrayOfNulls(table.getPhysicalFieldCount())
    }

    fun clear() {
        values = arrayOfNulls(table.getPhysicalFieldCount())
        isModified = true
    }

    fun findColumn(name: String?): Int {
        for (i in 1..columnCount) {
            if (getField(i).getName().equals(name)) return i
        }
        return -1
    }

    // XXX does deleteRow move the cursor?!??!
    @Throws(DbException::class)
    fun deleteAll() {
        beforeFirst()
        while (next()) {
            deleteRow()
        }
    }

    protected fun getObjectImpl(column: Int): Any? {
        //System.out.println ("column: "+column + " selectedFields[column-1]="+selectedFields[column-1]);

        return values[selectedFields[column - 1] - 1]
    }


    fun getBoolean(column: Int): Boolean {
        val b = getObject(column) as Boolean?
        return if ((b == null)) false else b
    }

    fun getField(index: Int): DbColumn {
        return table.getColumn(selectedFields[index - 1])
    }

    val columnCount: Int
        get() = selectedFields.size

    fun getSize(column: Int): Long {
        val value = getObjectImpl(column) ?: return -2

        return if ((value is ByteArray)
        ) value.size.toLong()
        else -1
    }

    fun getInt(column: Int): Int {
        val i = getObject(column) as Int?
        return if ((i == null)) 0 else i
    }

    fun getLong(column: Int): Long {
        val l = getObject(column) as Long?
        return if ((l == null)) 0 else l
    }

    val rowCount: Int
        get() = selection.size

    fun getString(column: Int): String? {
        val o = getObject(column)
        return if ((o == null)) null else o.toString()
    }

    fun updateBoolean(column: Int, value: Boolean) {
        updateObject(column, value)
    }

    fun updateInt(column: Int, value: Int) {
        updateObject(column, value)
    }

    fun updateLong(column: Int, value: Long) {
        updateObject(column, value)
    }


    fun updateString(column: Int, value: String?) {
        updateObject(column, value)
    }


    @Throws(DbException::class)
    fun moveToInsertRow() {
        isModified = true
        recordIndex = RamTable.INSERT_ROW

        val cnt: Int = table.getPhysicalFieldCount()
        for (i in 0 until cnt) {
            values[i] = table.getColumn(i).getDefault()
        }
    }

    @Throws(DbException::class)
    fun absolute(position: Int): Boolean {
        beforeFirst()
        for (i in 0 until position) if (!next()) return false

        return true
    }

    fun refreshRow() {
        recordIndex =
            (selection.elementAt(row - 1) as Int)
        val content =
            table.records!!.elementAt(recordIndex) as Array<Any>

        isDeleted = content == null

        for (i in content.indices) {
            //	System.out.println("values["+i+"]:"+content[i]);
            values[i] = if (isDeleted) null else content[i]
        }

        isModified = false
    }

    @Throws(DbException::class)
    fun insertRow() {
        if (recordIndex != RamTable.INSERT_ROW) throw DbException("Not on Insert Row")

        table.update(recordIndex, if (isDeleted) null else values)
    }

    @Throws(DbException::class)
    fun updateRow() {
        if (recordIndex == RamTable.INSERT_ROW) throw DbException("use insertRow for inserting records")

        table.update(recordIndex, if (isDeleted) null else values)
    }

    fun deleteRow() {
        isDeleted = true
    }

    fun getTable(): DbTable {
        return table
    }

    val isAfterLast: Boolean
        get() = row > selection.size

    val isLast: Boolean
        get() = row == selection.size

    @Throws(DbException::class)
    fun next(): Boolean {
        if (isAfterLast) return false
        row++ // if on last, just go to afterlast
        if (isAfterLast) return false
        recordIndex =
            (selection.elementAt(row - 1) as Int)
        refreshRow()
        return true
    }

    /** Places the cursor before the first record  */
    @Throws(DbException::class)
    fun beforeFirst() {
        row = 0
    }

    /** Dispose does not need to do much in the case of
     * ramtable  */
    fun close() {
        //throw new RuntimeException ("NYI");
    }


    fun updateObject(column: Int, value: Any?) {
        if (column < 1 || column > selectedFields.size) throw IndexOutOfBoundsException("colum " + column + " out of range 1.." + selectedFields.size)

        values[selectedFields[column - 1] - 1] = value
        isModified = true
    }

    /**
     * @see org.kobjects.db.DbResultSet.getBytes
     */
    fun getBytes(column: Int): ByteArray? {
        return getObject(column) as ByteArray?
    }


    fun getBinaryStream(column: Int): InputStream {
        return ByteArrayInputStream(getObject(column) as ByteArray?)
    }

    val isKeptUpdated: Boolean
        get() {
            throw RuntimeException("NYI")
        }

    val isSorted: Boolean
        get() {
            throw RuntimeException("NYI")
        }


    fun updateBinaryStream(column: Int, value: InputStream) {
        //byte[] bytes = new byte[value.length];
        //System.arraycopy(value, 0, bytes, 0, value.length);

        try {
            val bos = ByteArrayOutputStream()
            val buf = ByteArray(128)
            while (true) {
                val cnt = value.read(buf)
                if (cnt == -1) break
                bos.write(buf, 0, cnt)
            }
            updateObject(column, bos.toByteArray()) // was: bytes
        } catch (e: IOException) {
            throw RuntimeException(e.toString())
        }
    }

    /**
     * @see org.kobjects.db.DbResultSet.updateBytes
     */
    fun updateBytes(column: Int, data: ByteArray?) {
        updateObject(column, data)
    }


    open fun getObject(column: Int): Any? {
        return values[selectedFields[column - 1] - 1]

        /*
			return value = getObjectImpl(column);

			return (value instanceof byte[])
				? new ByteArrayInputStream((byte[]) value)
				: value; */
    }
}