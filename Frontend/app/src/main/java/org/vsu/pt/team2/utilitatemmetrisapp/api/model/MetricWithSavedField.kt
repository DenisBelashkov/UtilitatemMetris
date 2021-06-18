package org.vsu.pt.team2.utilitatemmetrisapp.api.model

class MetricWithSavedField(
    var isSaved: Boolean = false,
    var metric: Metric = Metric()
) {
    fun getIsSaved(): Boolean {
        return isSaved
    }

    fun setIsSaved(isSaved: Boolean) {
        this.isSaved = isSaved
    }
}