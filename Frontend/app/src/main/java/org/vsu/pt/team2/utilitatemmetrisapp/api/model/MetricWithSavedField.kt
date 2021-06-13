package org.vsu.pt.team2.utilitatemmetrisapp.api.model

data class MetricWithSavedField(
    val metric: Metric = Metric(),
    val isSaved: Boolean = false
)