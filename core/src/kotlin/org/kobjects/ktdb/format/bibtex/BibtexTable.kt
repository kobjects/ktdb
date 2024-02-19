package org.kobjects.ktdb.format.bibtex

import org.kobjects.bibtex.*
import java.io.Reader
import java.util.*

/**
 * A DbTable implementation for bibtex databases. Creates an
 * id automatically.  */
class BibtexTable : RamTable {
    internal inner class SaveThread : Thread() {
        override fun run() {
            while (open) {
                try {
                    sleep(15000)
                    if (modified) rewrite()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    /** Maps IDs to record numbers (base 1)  */
    protected var idTable: Hashtable<*, *> = Hashtable<Any?, Any?>()

    /** Maps keys to record numbers (base 1)  */
    protected var keyTable: TreeMap<*, *> = TreeMap<Any?, Any?>()

    var physicalFields: Int = -1

    protected var filename: String? = null
    var documentDir: String? = null

    constructor()

    constructor(filename: String) {
        connect("bibtex:$filename")
    }

    @Throws(DbException::class)
    fun connect(connector: String) {
        filename =
            connector.substring(connector.indexOf(':') + 1)

        documentDir = null

        val cut = filename!!.indexOf(";")
        if (cut != -1) {
            documentDir = filename!!.substring(cut + 1)
            filename = filename!!.substring(0, cut)
        }

        val file: File = File(filename)

        exists = file.exists()

        println(
            "trying to (re)load bib file: "
                    + file.getAbsoluteFile()
                    + " existing:"
                    + exists
        )

        for (i in DEFAULT_FIELDS.indices) {
            if (findColumn(DEFAULT_FIELDS[i]) <= 0) addField(DEFAULT_FIELDS[i], DbColumn.STRING)
        }

        if (exists) {
            try {
                val reader: Reader =
                    BufferedReader(FileReader(file))
                val parser: BibtexParser = BibtexParser(reader)

                var fields: Int = getColumnCount()
                val lastInc = 0

                while (true) {
                    val entry: Hashtable<*, *> = parser.nextEntry() ?: break
                    var dst = arrayOfNulls<Any>(fields)

                    val e: Enumeration<*> = entry.keys()
                    while (e.hasMoreElements()
                    ) {
                        val name = e.nextElement() as String

                        var i: Int = findColumn(name)

                        if (i <= 0) {
                            addField(name, DbColumn.STRING)
                                .getNumber()
                            fields++
                            val tmp = arrayOfNulls<Any>(fields)
                            System.arraycopy(
                                dst,
                                0,
                                tmp,
                                0,
                                dst.size
                            )
                            dst = tmp
                            i = fields
                        }
                        dst[i - 1] = entry.get(name)
                    }

                    if (dst[ID_INDEX] == null) dst[ID_INDEX] = generateId()

                    val box: Int = records.size()
                    idTable.put(dst[ID_INDEX], box)

                    if (dst[BIBKEY_INDEX] != null) {
                        keyTable.put(dst[BIBKEY_INDEX], box)
                    }

                    records.addElement(dst)
                }

                reader.close()
            } catch (e: IOException) {
                throw DbException(e.toString())
            }

            // ensure equal record sizes
            for (i in 0 until records.size()) {
                val r = records.elementAt(i) as Array<Any>
                val s = arrayOfNulls<Any>(getColumnCount())
                if (r.size == s.size) break
                System.arraycopy(r, 0, s, 0, r.size)
                records.setElementAt(s, i)
            }
        }

        physicalFields = getColumnCount()
        addField("pdfFile", DbColumn.BINARY)
    }

    val physicalFieldCount: Int
        get() = if (physicalFields == -1
        ) getColumnCount()
        else physicalFields

    @Throws(DbException::class)
    fun open() {
        super.open()
        SaveThread().start()
    }

    @Synchronized
    @Throws(DbException::class)
    protected fun update(
        recordIndex: Int,
        entry: Array<Any?>?
    ) {
        if (recordIndex != INSERT_ROW) {
            val old =
                records.elementAt(recordIndex) as Array<Any>
            idTable.remove(old[ID_INDEX])
            keyTable.remove(old[BIBKEY_INDEX])
        }

        if (entry != null && entry[ID_INDEX] == null) entry[ID_INDEX] = generateId()

        super.update(recordIndex, entry)

        if (entry != null) {
            val box = if (recordIndex == INSERT_ROW
            ) records.size() - 1
            else recordIndex

            idTable.put(entry[ID_INDEX], box)

            if (entry[BIBKEY_INDEX] != null) keyTable.put(entry[BIBKEY_INDEX], box)
        }

        modified = true
    }

    @Throws(IOException::class)
    protected fun writeEntry(bw: BibtexWriter, entry: Array<Any?>?) {
        if (entry == null) return

        bw.startEntry(entry[0] as String?, entry[1] as String?)

        for (j in 2 until entry.size) {
            if (entry[j] == null || "" == entry[j]) continue

            bw.writeField(
                getColumn(j + 1).getName(),
                entry[j].toString()
            )
        }

        bw.endEntry()
    }

    @Synchronized
    @Throws(DbException::class)
    fun rewrite() {
        println("BibtexTable: rewrite() triggered")
        try {
            val nf: File = File("$filename.new")
            val w: BufferedWriter =
                BufferedWriter(FileWriter(nf))

            val bw: BibtexWriter = BibtexWriter(w)

            modified = false

            val i: Iterator<*> = keyTable.keys.iterator()
            while (i.hasNext()
            ) {
                val box = keyTable.get(i.next()) as Int
                writeEntry(
                    bw,
                    records.elementAt(
                        box
                    ) as Array<Any?>
                )
            }
            bw.close()
            w.close()

            File("$filename.bak").delete()
            File(filename).renameTo(
                File("$filename.bak")
            )
            nf.renameTo(File(filename))
        } catch (e: IOException) {
            modified = true
            throw DbException("" + e)
        }
        println("BibtexTable: rewrite() finished")
    }

    @Throws(DbException::class)
    fun close() {
        if (modified) {
            rewrite()
        }
        super.close()
    }

    val idField: Int
        get() = ID_INDEX + 1

    protected fun getRecords(
        selected: Vector<*>?,
        fields: IntArray?
    ): RamResultSet {
        return BibtexRecord(this, selected, fields)
    } /*
    	public static void main(String argv[]) throws DbException {
    
    		DbTable table = DbManager.connect("bibtex:" + argv[0]);
    
    		table.open();
    
    		DbRecord r = table.select(false);
    
    		while (r.hasNext()) {
    			r.next();
    
    			System.out.println(r.getId());
    		}
    	}
    */

    companion object {
        // type and key MUST be first field (assumed by writer)
        val DEFAULT_FIELDS: Array<String> = arrayOf(
            "bibtype",
            "bibkey",
            "id",
            "address",
            "author",
            "title",
            "chapter",
            "crossref",
            "edition",
            "editor",
            "howpublished",
            "institution",
            "journal",
            "key",
            "month",
            "note",
            "number",
            "organization",
            "pages",
            "publisher",
            "school",
            "series",
            "title",
            "type",
            "volume",
            "year"
        )

        /** Base 0 Key field index  */
        const val BIBKEY_INDEX: Int = 1

        /** Base 0 id field index  */
        protected const val ID_INDEX: Int = 2

        fun hex(buf: StringBuffer, l: Long, digits: Int) {
            val h = java.lang.Long.toHexString(l)
            for (i in h.length until digits) buf.append('0')

            buf.append(h)
        }

        protected fun generateId(): String {
            val time0 = System.currentTimeMillis()
            var time = System.currentTimeMillis()

            while (time == time0) {
                Thread.yield()
                time = System.currentTimeMillis()
            }

            while (time == System.currentTimeMillis()) {
                Thread.yield()
            }

            var adr = try {
                InetAddress.getLocalHost().getAddress()
            } catch (e: Exception) {
                ByteArray(0)
            }

            val buf = StringBuffer()

            for (i in adr.indices) hex(buf, ((adr[i].toInt()) and 255).toLong(), 2)

            hex(buf, time, 16)

            return buf.toString()
        }
    }
}