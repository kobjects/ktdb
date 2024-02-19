package org.kobjects.ktdb.ram

import java.util.*

open class RamTable : DbTable {
    protected var fields: Vector<*> = Vector<Any?>()
    @JvmField
    var records: Vector<*>? = Vector<Any?>()

    //    protected Hashtable index;
    var isOpen: Boolean = false
        protected set
    @JvmField
    protected var exists: Boolean = false
    protected var modified: Boolean = false

    //    protected int idField = -1;
    @Throws(DbException::class)
    open fun connect(connector: String?) {
    }

    fun findColumn(name: String?): Int {
        val cnt = columnCount
        for (i in 1..cnt) if (getColumn(i).getName().equals(name)) return i

        return -1
    }

    fun addField(name: String?, type: Int): DbColumn {
        val i = findColumn(name)
        if (i > 0) return getColumn(i)

        val f: DbColumn = DbColumn(this, fields.size + 1, name, type)
        fields.addElement(f)
        return f
    }

    @Throws(DbException::class)
    protected fun checkOpen(required: Boolean) {
        if (isOpen != required) throw DbException(
            "DB must "
                    + (if (required) "" else "not ")
                    + "be open"
        )
    }

    open val name: String?
        get() = "RamTable"

    @Throws(DbException::class)
    open fun open() {
        if (!exists) throw DbException("Db does not exist!")
        checkOpen(false)
        isOpen = true
    }

    @Throws(DbException::class)
    fun create() {
        checkOpen(false)
        exists = true
    }

    @Throws(DbException::class)
    open fun close() {
        isOpen = false
        records = null
    }

    @Throws(DbException::class)
    fun delete() {
        close()
    }

    fun exists(): Boolean {
        return exists
    }

    open val idField: Int
        get() = -1

    fun getColumn(index: Int): DbColumn {
        return fields.elementAt(index - 1) as DbColumn
    }

    val columnCount: Int
        get() = fields.size


    open val physicalFieldCount: Int
        get() = columnCount

    /**
     * Overwrites the fields of the existing object with contents of the given entry  */
    @Throws(DbException::class)
    open fun update(recordIndex: Int, entry: Array<Any?>?) {
        if (entry == null) {
            records!!.setElementAt(null, recordIndex)
            modified = true
            return
        }

        val rec =
            if (recordIndex == INSERT_ROW) arrayOfNulls(entry.size) else ((records!!.elementAt(recordIndex) as Array<Any?>))

        System.arraycopy(entry, 0, rec, 0, entry.size)

        if (recordIndex == INSERT_ROW) {
            /*    		if (idField > 0) 
	    		index.put (entry[idField], new Integer(records.size()));
*/
            records!!.addElement(rec)
        }

        modified = true
    }

    @Throws(DbException::class)
    fun select(updated: Boolean): DbResultSet {
        return select(null, null, null, false, updated)
    }

    @Throws(DbException::class)
    fun select(
        fields: IntArray?,
        condition: DbCondition?,
        sortfield: IntArray?,
        inverse: Boolean,
        updated: Boolean
    ): DbResultSet {
        checkOpen(true)

        val selected: Vector<*> = Vector<Any?>()

        for (i in records!!.indices) {
            val r = records!!.elementAt(i) as Array<Any>

            if (r != null
                && (condition == null || condition.evaluate(r))
            ) selected.addElement(i)
        }

        if (sortfield != null || updated) throw RuntimeException("support for sorting and updated fields is NYI")

        return getRecords(selected, fields)
        //       return new RamRecord(this, selected, fields);
    }


    protected open fun getRecords(selected: Vector<*>, fields: IntArray?): RamResultSet {
        return RamResultSet(this, selected, fields)
    } /*
    public void setIdField(int idField) throws DbException {

        checkOpen(false);
        this.idField = idField;
        index = new Hashtable();
    }
*/


    companion object {
        // deleted records are marked by null values
        const val INSERT_ROW: Int = -2
    }
}