package org.vsu.pt.team2.utilitatemmetrisapp.offlineTools

import org.vsu.pt.team2.utilitatemmetrisapp.dateutils.DateFormatter
import java.text.DateFormat
import java.util.*
import javax.inject.Inject
import kotlin.random.Random

class RandomTools @Inject constructor() : Random() {
    val random = Random.Default
    fun randomMeterIdentifier(): String = List(16) {
        if (it <= 5 || it > 10)
            (('a'..'z') + ('A'..'Z')).random()
        else
            ('0'..'9').random()
    }.joinToString("")


    override fun nextBits(bitCount: Int): Int {
        return random.nextBits(bitCount)
    }
}