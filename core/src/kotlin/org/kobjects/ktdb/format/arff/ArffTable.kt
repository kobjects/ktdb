package org.kobjects.ktdb.formats.arff

import org.kobjects.ktdb.DbException
import org.kobjects.ktdb.DbTable
import org.kobjects.ktdb.ram.RamTable
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException

class ArffTable : RamTable {
    override var name: String? = null
    var filename: String? = null

    constructor()

    constructor(filename: String) {
        connect("arff:$filename")
    }

    @Throws(DbException::class)
    override fun connect(connector: String?) {
        filename =
            connector!!.substring(connector.indexOf(':') + 1)
        exists = File(filename).exists()
        if (exists) {
            try {
                val parser: ArffParser =
                    ArffParser(
                        this,
                        BufferedReader(
                            FileReader(filename)
                        )
                    )

                while (true) {
                    val data: Array<Any> = parser.read() ?: break
                    records!!.addElement(data)
                }
                /*
                		records.addElement(r);
                	} */
            } catch (e: IOException) {
                throw DbException(e.toString())
            }
        }
    }

    companion object {
        @Throws(DbException::class)
        @JvmStatic
        fun main(args: Array<String>) {
            val table: DbTable =
                ArffTable("/home/haustein/projects/infolayer/samples/arff/weather.arff")

            table.open()
            println("table: $table")

            for (i in 0 until table.columnCount) {
                println("field: " + table.getColumn(i))
            }

            val record = table.select(false)

            while (record!!.next()) {
                println("record: $record")
            }
        }
    }
}
