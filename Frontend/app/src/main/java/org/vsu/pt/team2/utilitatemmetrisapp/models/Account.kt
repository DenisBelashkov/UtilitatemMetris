package org.vsu.pt.team2.utilitatemmetrisapp.models

import org.vsu.pt.team2.utilitatemmetrisapp.api.model.Flat

class Account(
    var identifier: String,
    var address: String,
) {
    constructor(
        flat: Flat
    ) : this(flat.identifier, flat.address)
}