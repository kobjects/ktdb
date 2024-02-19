package org.kobjects.ktdb.format.arff

import org.kobjects.util.Csv

class ArffParser(table: ArffTable, reader: BufferedReader) {
    var table: ArffTable = table
    var reader: BufferedReader = reader

    fun cut(buf: StringBuffer): String {
        var i = 0
        var result = buf.toString()
        while (i < result.length && result[i] > ' ') i++
        result = result.substring(0, i)
        while (i < buf.length && buf[i] <= ' ') i++
        var j = 0
        while (i < buf.length) buf.setCharAt(j++, buf[i++])
        buf.setLength(j)
        return result
    }

    /**
     * Constructor for ArffParser.
     */
    init {
        while (true) {
            var line: String = reader.readLine() ?: throw RuntimeException("Unexpected EOF")
            line = line.trim { it <= ' ' }
            if (line == "" || line.startsWith("%")) continue

            if (line.equals("@data", ignoreCase = true)) break

            val buf = StringBuffer(line)

            val cmd = cut(buf).lowercase(Locale.getDefault())
            //            System.out.println("command: " + cmd);
            if (cmd == "@relation") table.name = buf.toString()
            else if (cmd == "@attribute") {
                val name = cut(buf)
                val remainder = buf.toString().trim { it <= ' ' }

                var values: Array<String?>? = null
                var type: Int

                if (remainder.equals("real", ignoreCase = true)) type = DbColumn.DOUBLE
                else if (remainder.startsWith("{")) {
                    type = DbColumn.STRING
                    values = org.kobjects.util.Csv.decode(remainder.substring(1, remainder.length - 1))
                } else {
                    System.err.println("unrecognized type: '$remainder' assuming string")
                    type = DbColumn.STRING
                }

                table.addField(name, type)
                if (values != null) {
                    val field: DbColumn = table.getColumn(table.getColumnCount())
                    field.setProperties(null, 0, 0, values)
                }
            }
        }
    }

    @Throws(IOException::class)
    fun read(): Array<Any?>? {
        var line: String
        do {
            line = reader.readLine()
            if (line == null) return null
        } while (line.startsWith("%"))

        val strings: Array<String> = Csv.decode(line)
        val objects = arrayOfNulls<Any>(table.getColumnCount())

        for (i in 0 until table.getColumnCount()) {
            val type: Int = table.getColumn(i + 1).getType()
            when (type) {
                DbColumn.DOUBLE -> objects[i] = strings[i]
                DbColumn.STRING -> objects[i] = strings[i]
                else -> throw RuntimeException("Unsupported type $type")
            }
        }
        return objects
    }
}