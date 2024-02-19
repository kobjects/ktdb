package org.kobjects.ktdb

/**
 * Describes a field in a table.
 */
class DbColumn
/**
 * Creates a new field.  From a user's perspective, new fields are
 * created using the table's `addField()` factory
 * method.
 */(
    /**
     * Holds the table this field belongs to.
     */
    private val table: DbTable,
    /**
     * Holds the field's number.
     */
    val number: Int,
    /**
     * Holds the field's name.
     */
    val name: String,
    /**
     * Holds the field's type.
     */
    val type: Int
) {
    /**
     * Returns the field's number. Field numbering starts from zero.
     */

    /**
     * Returns the field's name.
     */

    /**
     * Returns the field's type.
     */

    /**
     * Returns the field's maximum size.
     */
    /**
     * Holds the field's maximum size. This is most often used for strings and
     * currently only interpreted in the UI.
     */
    var maxSize: Int = 0
        private set

    /**
     * Returns the field's input constraints.
     */
    /**
     * Holds the field's input constraints. This is most often used for strings and
     * currently only interpreted in the UI.
     */
    var constraints: Int = 0
        private set

    /**
     * Returns the field's label.
     */
    /**
     * Holds the field's label. This is only interpreted in the UI. If the label
     * is null, the field's name is displayed instead.
     */
    var label: String? = null
        private set

    /**
     * Holds the field's possible values. This is only interpreted in the UI,
     * and its handling depends on the field type. Basically, a ChoiceGroup
     * holding the given values is displayed, and the user can select one of
     * these values. Free-text input is not possible.
     */
    private var values: Array<String>?

    /**
     * Returns the field's default value.
     */
    /**
     * Changes the field's default value. Note that:
     *
     *  * Wrapper classes have to be used for primitive types.
     *  * The actual class has to match the field type. Otherwise a
     * `ClassCastException` will occur later.
     *
     */
    /**
     * Holds the field's default value. This value is assigned to the
     * corresponding column of a newly inserted record.
     */
    var default: Any? = null

    /**
     * Returns the field's possible values.
     */
    fun getValues(): Array<String>? {
        if (values == null) return null

        val result = arrayOfNulls<String>(values!!.size)
        System.arraycopy(values, 0, result, 0, values!!.size)

        return values
    }

    /**
     * Changes the field's UI settings.
     */
    fun setProperties(label: String?, maxSize: Int, constraints: Int, values: Array<String>?) {
        this.label = label
        this.maxSize = maxSize
        this.constraints = constraints
        this.values = values
    }

    companion object {
        /**
         * Is the type constant for boolean fields.
         */
        const val BOOLEAN: Int = 16 // java.sql.types.BOOLEAN

        /**
         * Is the type constant for integer fields.
         */
        const val INTEGER: Int = 4 // java.sql.types.INTEGER

        /**
         * Is the type constant for long values.
         */
        const val LONG: Int = -5 // java.sql.types.BIGINT

        /**
         * Is the type constant for String values.
         */
        const val STRING: Int = 12 // java.sql.types.VARCHAR

        /**
         * Is the type constant for binary values.
         */
        const val BINARY: Int = -3 // java.sql.types.VARBINARY

        /**
         * Is the type constant for sets of up to 32 items. Fields of type BITSET
         * are stored like INTEGER fields, but are handled differently in the user
         * interface.
         */
        const val BITSET: Int = 3000 // 3xxx not used in java sql

        /**
         * Is the type constant used for date/time values. Fields of type DATETIME
         * are stored like LONG fields, but are handled differently in the user
         * interface.
         */
        const val DATETIME: Int = 93 // java.sql.types.TIMESTAMP

        /**
         * Is the type constant used for graphics values. Fields of type GRAPHICS
         * are stored like BINARY fields, but are handled differently in the user
         * interface.
         */
        const val GRAPHICS: Int = 3001 // 3xxx not used in java sql

        const val DOUBLE: Int = 8 // java.sql.types.DOUBLE


        /**
         * Utility method to compare two field values. The type of the values must
         * be specified. The method returns a value smaller than zero if value1 < value2,
         * a value greater than zero if if value1 > value2, and zero otherwiese.
         * Binary values are compared according to their length, just to have a
         * defined behaviour for all types.
         */
        fun compare(type: Int, value1: Any, value2: Any?): Int {
            when (type) {
                INTEGER, BITSET -> {
                    return (value1 as Int) - ((value2 as Int?)!!)
                }

                LONG, DATETIME -> {
                    val l = (value1 as Long) - ((value2 as Long?)!!)

                    return if (l < 0) -1 else if (l > 0) 1 else 0
                }

                STRING, BOOLEAN -> {
                    return value1.toString().compareTo(value2.toString())
                }

                BINARY, GRAPHICS -> return (value1 as ByteArray).size - (value2 as ByteArray?)!!.size

                else -> {
                    throw IllegalArgumentException("Illegal type \"$type\"")
                }
            }
        }
    }
}
