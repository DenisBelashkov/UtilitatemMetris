package org.vsu.pt.team2.utilitatemmetrisapp.api.model

import org.vsu.pt.team2.utilitatemmetrisapp.models.MeterType

data class InformationAboutPayment(
    /**
     * identifier of metric
     */
    val identifierMetric: String? = null,
    /**
     * here date-time
     * like "2021-05-27T14:01:16.169Z"
     */
    val dateWith: String? = null,
    /**
     * here date-time
     * like "2021-05-27T14:01:16.169Z"
     */
    val dateTo: String? = null,

    /**
     * one of [electic,heating,coldwater,hotwater,gas]
     */
    val typeMetric: MeterType? = null,
)