package org.vsu.pt.team2.utilitatemmetrisapp.api.model

import org.vsu.pt.team2.utilitatemmetrisapp.models.MeterType

data class InformationAboutPayment(
    /**
     * identifier of metric
     */
    val identifierMetric: String? = null,
    /**
     * here date-time
     */
    val dataWith: String? = null,
    /**
     * here date-time
     */
    val dataTo: String? = null,

    /**
     * one of [electic,heating,coldwater,hotwater,gas]
     */
    val typeMetric: MeterType? = null,
)