package org.kobjects.db

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
 * @author unascribed
 * @version 1.0
 */
object DbManager {
    /**
     * Connects to a table. This factory method examines the given connector and
     * loads a suitable table implementation by name, connecting it to the given
     * table. The result is the same as for instatiating and connecting the
     * table explicitly, i.e. the table is not opened. The connector string
     * obeys the following URI-like naming scheme:
     * <pre>
     * "protocol:table;parameters"
    </pre> *
     * The actual table implementation is chosen depending on the "protocol"
     * value. The package name always is "org.kobjects.db" plus a sub-package
     * named after the protocol. The class name consists of the protocol values
     * with the first letter being uppercased plus the fixed suffix "Table". As
     * an example, for a connector string
     * <pre>
     * "rms:MyTable;user=joerg;password=secret"
    </pre> *
     * the class `org.kobjects.db.rms.RmsTable` would be instantiated
     * and the new instance connects to a table "MyTable". Interpretation of the
     * parameters following the ";" is up to the table implementation.
     */
    @Throws(DbException::class)
    fun connect(connector: String): DbTable? {
        var table: DbTable? = null

        try {
            val p = connector.indexOf(':')
            var type = connector.substring(0, p)
            if ("https" == type) type = "http"
            var name = type[0].uppercaseChar().toString() + type.substring(1) + "Table"

            // hack; I'll change this back asap
            if ("http" == type) name = name + "SE"

            table = Class.forName("org.kobjects.db.$type.$name").newInstance() as DbTable
        } catch (e: Exception) {
            throw DbException("Can't connect to table \"$connector\"", e)
        }

        table!!.connect(connector)

        return table
    }
}
