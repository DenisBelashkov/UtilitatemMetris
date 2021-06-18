package org.vsu.pt.team2.utilitatemmetrisapp.api.model

data class InformationAboutPayment(
    /**
     * identifier of metric
     */
    val identifierMetric: String = "",
    /**
     * here date-time
     */
    val dataWith: String = "",
    /**
     * here date-time
     */
    val dataTo: String = "",

    /**
     * one of [electic,heating,coldwater,hotwater,gas]
     */
    val typeMetric: String = "",
)