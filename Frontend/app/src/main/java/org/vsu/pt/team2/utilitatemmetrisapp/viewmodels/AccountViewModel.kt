package org.vsu.pt.team2.utilitatemmetrisapp.viewmodels

import org.vsu.pt.team2.utilitatemmetrisapp.models.MeterType

class AccountViewModel(
    var identifier: String,
    var address: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AccountViewModel

        if (identifier != other.identifier) return false
        if (address != other.address) return false

        return true
    }

    override fun hashCode(): Int {
        var result = identifier.hashCode()
        result = 31 * result + address.hashCode()
        return result
    }
}