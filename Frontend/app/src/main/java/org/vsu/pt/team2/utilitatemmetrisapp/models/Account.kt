package org.vsu.pt.team2.utilitatemmetrisapp.models

class Account(
        var identifier: String,
        var address: String,
        var meters: List<Meter> = listOf()
)