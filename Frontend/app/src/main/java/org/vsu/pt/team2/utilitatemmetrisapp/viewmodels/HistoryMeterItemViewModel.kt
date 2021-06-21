package org.vsu.pt.team2.utilitatemmetrisapp.viewmodels

import org.vsu.pt.team2.utilitatemmetrisapp.models.MeterType

class HistoryMeterItemViewModel(
    var paymentId: Int,
    var identifier: String,
    var type: MeterType,
    var sum: Double,
    var date: String,
    var address: String,
    var email: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HistoryMeterItemViewModel

        if (paymentId != other.paymentId) return false
        if (identifier != other.identifier) return false
        if (type != other.type) return false
        if (sum != other.sum) return false
        if (date != other.date) return false
        if (address != other.address) return false
        if (email != other.email) return false

        return true
    }

    override fun hashCode(): Int {
        var result = paymentId
        result = 31 * result + identifier.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + sum.hashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + address.hashCode()
        result = 31 * result + email.hashCode()
        return result
    }
}