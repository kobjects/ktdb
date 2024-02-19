package org.kobjects.ktdb.format.bibtex

import org.kobjects.ktdb.format.bibtex.BibtexTable
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.util.*

// Rolandstrasse 27, D-46045 Oberhausen, Germany
// All rights reserved.
//
// For licensing details, please refer to the file "license.txt",
// distributed with this file.


/**
 * @author Stefan Haustein
 */
class BibtexRecord internal constructor(table: BibtexTable, selected: Vector<*>?, fields: IntArray?) :
    RamResultSet(table, selected, fields) {
    var table: BibtexTable = table


    fun getObject(index: Int): Any? {
        val field: DbColumn = getField(index)
        if (field.getNumber() <= table.physicalFields) return super.getObject(index)

        try {
            val file: File = File(
                table.documentDir,
                values.get(BibtexTable.BIBKEY_INDEX)
                        + "."
                        + field.getName().substring(0, 3)
            )

            println("trying file: $file")

            if (!file.exists()) return null

            return FileInputStream(file)
        } catch (e: IOException) {
            throw RuntimeException(e.toString())
        }
    }
}
