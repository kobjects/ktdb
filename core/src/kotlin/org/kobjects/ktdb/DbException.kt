package org.kobjects.ktdb

/**
 *
 * Title:
 *
 * Description:
 *
 * Copyright: Copyright (c) 2002
 *
 * Company:
 * @author J�rg Pleumann
 * @version 1.0
 */
class DbException : Exception {
    private var chained: Exception? = null

    constructor(message: String?) : super(message)

    constructor(message: String, chained: Exception) : super("$message ($chained)") {
        this.chained = chained
    }
}
