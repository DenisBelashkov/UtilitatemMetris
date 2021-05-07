package org.vsu.pt.team2.utilitatemmetrisapp.viewmodels

data class GeneralButtonViewModel(
    var buttonText: String,
    var onClick: (() -> Unit)
)