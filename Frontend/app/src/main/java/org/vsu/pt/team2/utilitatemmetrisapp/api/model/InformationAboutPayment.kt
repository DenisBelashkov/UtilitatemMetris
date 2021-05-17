package org.vsu.pt.team2.utilitatemmetrisapp.api.model

data class InformationAboutPayment(
        val idUser: Int,
        /**
         * here date-time
         */
        val dataWith: String,
        /**
         * here date-time
         */
        val dataTo: String
)