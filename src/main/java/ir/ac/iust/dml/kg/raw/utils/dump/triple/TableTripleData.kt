package ir.ac.iust.dml.kg.raw.utils.dump.triple

import com.google.gson.annotations.SerializedName

data class TableTripleData(
    @SerializedName("class")
    var clazz: String? = null,
    var module: String? = null,
    @SerializedName("object")
    var objekt: String? = null,
    var predicate: String? = null,
    var subject: String? = null,
    var version: String? = null
)