package org.vsu.pt.team2.utilitatemmetrisapp.models

import org.vsu.pt.team2.utilitatemmetrisapp.api.model.ItemPaymentHistory

data class PaymentData(
    val id: Int,
    val identifier: String,
    val date: String,
    val email: String,
    val cost: Number,
    val prevValue: Int,
    val curValue: Int
) {
    constructor(netModel: ItemPaymentHistory) : this(
        netModel.id,
        netModel.identifier,
        netModel.date,
        netModel.email,
        netModel.cost,
        netModel.prevValue,
        netModel.curValue
    )
}
