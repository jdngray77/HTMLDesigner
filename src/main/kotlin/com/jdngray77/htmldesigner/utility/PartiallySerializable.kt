package com.jdngray77.htmldesigner.utility

import java.io.Serializable

/**
 * A serializable class with transient fields.
 *
 * Used to re-populate transient fields on de-serialization.
 */
interface PartiallySerializable : Serializable {

    fun recreateTransientProperties()

}