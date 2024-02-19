package org.kobjects.ktdb.format.mp3

import java.io.FileInputStream
import java.io.IOException
import java.util.*

/**
 * @author haustein
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */

class Mp3Record internal constructor(table: RamTable?, selected: Vector<*>?, fields: IntArray?) :
    RamResultSet(table, selected, fields) {
    fun getObject(index: Int): Any {
        return if (index == 7) {
            try {
                FileInputStream(values.get(1) as String)
            } catch (e: IOException) {
                throw RuntimeException(e.toString())
            }
        } else super.getObject(index)
    }
}
