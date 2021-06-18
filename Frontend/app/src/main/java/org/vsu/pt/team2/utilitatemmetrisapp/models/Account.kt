package org.vsu.pt.team2.utilitatemmetrisapp.models

import org.vsu.pt.team2.utilitatemmetrisapp.api.model.Flat

class Account(
    var identifier: String,
    var address: String,
) {
    constructor(
        flat: Flat
    ) : this(flat.id, flat.address)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Account

        if (identifier != other.identifier) return false

        return true
    }

    override fun hashCode(): Int {
        return identifier.hashCode()
    }

}