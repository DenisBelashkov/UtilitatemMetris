package org.vsu.pt.team2.utilitatemmetrisapp.viewmodels

import org.vsu.pt.team2.utilitatemmetrisapp.models.MeterType

class MeterViewModel(
    var identifier: String,
    var type: MeterType,
    var backlog: Double,
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MeterViewModel

        if (identifier != other.identifier) return false
        if (type != other.type) return false
        if (backlog != other.backlog) return false

        return true
    }

    override fun hashCode(): Int {
        var result = identifier.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + backlog.hashCode()
        return result
    }
}