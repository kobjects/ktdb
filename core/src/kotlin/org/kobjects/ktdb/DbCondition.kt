package org.kobjects.ktdb

import java.util.*

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

/**
 * Represents a compound condition for filtering a table. Basically the
 * WHERE [...] part in an SQL SELECT statment.
 */
class DbCondition {
    /**
     * Denotes the node's type. Legal values are the constants above.
     */
    private var operator: Int

    /**
     * Holds the node's field number, in case this is a leaf node.
     */
    private var field = 0

    /**
     * Holds the node's field type, in case this is a leaf node. This value
     * is set when the Condition is assigned a table.
     */
    private var type = 0

    /**
     * Holds the value to compare the given field with, in case this is a leaf
     * node.
     */
    private var value: Any? = null

    /**
     * Holds the array of subordinate notes, in case this in an inner node.
     */
    private var children: Array<DbCondition>


    private var table: DbTable? = null

    /**
     * Creates a new Condition node for a relation between a field (given by its
     * number) and a value. The operator must be one of LT, GT, LE, GE, EQ, NE,
     * or EQIC.
     */
    constructor(operator: Int, field: Int, value: Any) {
        var value = value
        if ((operator < LT) || (operator > EQ_TEXT)) {
            throw DbException("Illegal type code \"$type\" for leaf node.")
        }

        if (operator == EQ_TEXT) {
            value = value.toString().uppercase(Locale.getDefault())
        }

        this.operator = operator
        this.field = field
        this.value = value
    }

    /**
     * Creates a new Condition node for an AND, OR, XOR or NOT operator.
     */
    constructor(operator: Int, children: Array<DbCondition>) {
        if ((operator < AND) || (operator > NOT)) {
            throw DbException("Illegal type code \"$type\" for inner node.")
        }

        this.operator = operator
        this.children = children
    }

    /**
     * Assigns this condition a table.
     */
    fun setTable(table: DbTable) {
        this.table = table
        if (operator >= AND) {
            for (i in children.indices) {
                children[i].setTable(table)
            }
        } else {
            type = table.getColumn(field).type
        }
    }

    /**
     * Evaluates this Condition for the given record.
     */
    fun evaluate(values: Array<Any>): Boolean {
        //System.out.println("evaluate(): " + this.toString() + " of type " + operator);

        if (operator < AND) {
            val obj = values[field - 1]

            when (operator) {
                LT -> {
                    return DbColumn.Companion.compare(type, obj, value) < 0
                }

                GT -> {
                    return DbColumn.Companion.compare(type, obj, value) > 0
                }

                LE -> {
                    return DbColumn.Companion.compare(type, obj, value) <= 0
                }

                GE -> {
                    return DbColumn.Companion.compare(type, obj, value) >= 0
                }

                EQ -> {
                    return DbColumn.Companion.compare(type, obj, value) == 0
                }

                NE -> {
                    return DbColumn.Companion.compare(type, obj, value) != 0
                }

                EQ_TEXT -> {
                    return obj.toString().uppercase(Locale.getDefault()) == value.toString()
                }
            }
        } else {
            when (operator) {
                AND -> {
                    var i = 0
                    while (i < children.size) {
                        if (!children[i].evaluate(values)) return false
                        i++
                    }

                    return true
                }

                OR -> {
                    var i = 0
                    while (i < children.size) {
                        if (children[i].evaluate(values)) return true
                        i++
                    }

                    return false
                }

                XOR -> {
                    return children[0].evaluate(values) xor children[1].evaluate(values)
                }

                NOT -> {
                    return !children[0].evaluate(values)
                }
            }
        }

        return false // To make compiler happy
    }


    override fun toString(): String {
        if (operator < AND) {
            val f = table!!.getColumn(field).name

            val v = if (value is String) "'$value'" else "" + value

            return when (operator) {
                LT -> "$f < $v"
                GT -> "$f > $v"
                LE -> "$f <= $v"
                GE -> "$f >= $v"
                EQ -> "$f = $v"
                NE -> "$f != $v"
                EQ_TEXT -> throw RuntimeException("NYI") //return f + " = " + v;

                else -> throw RuntimeException("illegal operator: $operator")
            }
        } else if (operator == NOT) {
            return "NOT(" + children[0] + ")"
        } else {
            val buf = StringBuffer("(")
            buf.append(children[0].toString())
            for (i in 1 until children.size) {
                when (operator) {
                    AND -> buf.append(" AND ")
                    OR -> buf.append(" OR ")
                    XOR -> buf.append(" XOR ")
                    else -> throw RuntimeException("illegal operator: $operator")
                }
                buf.append(children[i].toString())
            }
            buf.append(")")
            return buf.toString()
        }
    }

    companion object {
        /**
         * Type constant for "less than".
         */
        const val LT: Int = 1

        /**
         * Type constant for "greater than".
         */
        const val GT: Int = 2

        /**
         * Type constant for "less or equal".
         */
        const val LE: Int = 3

        /**
         * Type constant for "greater or equal".
         */
        const val GE: Int = 4

        /**
         * Type constant for "equal".
         */
        const val EQ: Int = 5

        /**
         * Type constant for "not equal".
         */
        const val NE: Int = 6

        /**
         * Type constant for "textually equal". Results in a case-insensitive String
         * comparison.
         */
        const val EQ_TEXT: Int = 7

        /**
         * Type constant for AND operator. The value of a node of type AND is
         * true if and only if all its children evaluate to true.
         */
        const val AND: Int = 16

        /**
         * Type constant for OR operator. The value of a node of type OR is
         * true if at least one its children evaluates to true, and false otherwise.
         */
        const val OR: Int = 17

        /**
         * Type constant for XOR operator. A node of type XOR must have exactly two
         * children. It evaluates to true if and only if its children evaluate
         * to different truth values.
         */
        const val XOR: Int = 18

        /**
         * Type constant for NOT operator. A node of type NOT must have exactly one
         * child. It evaluates to true if and only if this child evaluates to false.
         */
        const val NOT: Int = 19
    }
}
