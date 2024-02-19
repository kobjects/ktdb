package org.kobjects.ktdb.format.p3

import org.kobjects.db.DbManager.connect
import java.io.File
import java.io.IOException
import java.io.RandomAccessFile
import java.util.*

/**
 * @author haustein
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
class Mp3Table : RamTable() {
    var base: String? = null

    /**
     * @see org.kobjects.db.DbTable.connect
     */
    @Throws(DbException::class)
    fun connect(connector: String) {
        base = connector.substring(4)
        exists = true

        addField("id", DbColumn.STRING) // 0
        addField("file", DbColumn.STRING) // 1 
        addField("track", DbColumn.STRING) // 2
        addField("artist", DbColumn.STRING) // 3
        addField("album", DbColumn.STRING) // 4
        addField("year", DbColumn.STRING) // 5
        addField("comment", DbColumn.STRING) // 6 
        addField("data", DbColumn.BINARY) // 7
    }

    val name: String
        /**
         * @see org.kobjects.db.DbTable.getName
         */
        get() = "mp3"

    fun clean(s: String?): String? {
        var s = s ?: return null
        val cut = s.indexOf('\u0000')
        if (cut != -1) s = s.substring(0, cut)
        return s.trim { it <= ' ' }
    }

    @Throws(DbException::class)
    fun addFile(file: File) {
        val data = arrayOfNulls<Any>(getColumnCount())
        data[0] = "" + (counter++)
        data[1] = file.toString()
        records.addElement(data)

        if (file.length() > 128) {
            try {
                val raf = RandomAccessFile(file, "r")
                raf.seek(file.length() - 128)
                val buf = ByteArray(128)
                raf.readFully(buf)
                raf.close()
                if (String(buf, 0, 3) == "TAG") {
                    data[2] = clean(String(buf, 3, 30)) // track
                    data[3] = clean(String(buf, 33, 30)) //artist
                    data[4] = clean(String(buf, 63, 30)) // album
                    data[5] = clean(String(buf, 93, 4)) //year
                    data[6] = clean(String(buf, 97, 127 - 97)) // comment
                }
            } catch (e: IOException) {
                throw DbException(e.toString())
            }
        }
    }

    @Throws(DbException::class)
    fun recurse(dir: File) {
        val files = dir.listFiles()

        for (i in files.indices) {
            val file = files[i]
            if (file.isDirectory) recurse(file)
            else if (file.name.lowercase(Locale.getDefault()).endsWith(".mp3")) {
                addFile(file)
            }
        }
    }

    @Throws(DbException::class)
    fun open() {
        super.open()
        recurse(File(base))
    }

    protected fun getRecords(selected: Vector<*>?, fields: IntArray?): RamResultSet {
        return Mp3Record(this, selected, fields)
    }

    companion object {
        var counter: Int = 0

        @Throws(DbException::class)
        @JvmStatic
        fun main(argv: Array<String>) {
            val table: DbTable? = connect("mp3:/Users/haustein/Music")
            table.open()
            val record: DbResultSet = table.select(false)

            while (record.next()) {
                for (i in 0 until table.getColumnCount() - 1) {
                    System.out.println(record.getString(i))
                }
            }
        }
    }
}
