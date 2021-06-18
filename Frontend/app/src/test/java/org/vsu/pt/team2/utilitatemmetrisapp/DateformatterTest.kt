package org.vsu.pt.team2.utilitatemmetrisapp

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.runners.MockitoJUnitRunner
import org.vsu.pt.team2.utilitatemmetrisapp.dateutils.DateFormatter
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class DateformatterTest {

    @Test
    fun dateFormatter_convertDateToStrSimple() {
        val dateZero = Date().apply {
            time = 0
        }
        val dateStr = DateFormatter.toString(dateZero)
        Assert.assertEquals(
            "Cant convert simple date string",
            dateStr, "01.01.1970 00:00:00"
        )
    }

    @Test
    fun dateFormatter_convertationServerFormattedDateToDate(){
        val serverStr = "2021-05-27T14:04:15.988Z"
        try {
            DateFormatter.fromNetworkString(serverStr)
        }catch (e:Exception){
            Assert.fail("Cant convert server date string")
        }
    }

    @Test
    fun dateFormatter_convertationServerFormattedDateToDate2(){
        val serverStr = "2021/05/27T14:04:15.988Z"
        try {
            DateFormatter.fromNetworkString(serverStr)
            Assert.fail("Wrong convert server date string")
        }catch (e:Exception){
        }
    }

    @Test
    fun dateFormatter_serverFormattedDateToUiStrDate(){
        val serverStr = "2021-05-27T14:04:15.988Z"
        try {
            val resDateStr = DateFormatter.fromNetworkStringToString(serverStr)
            Assert.assertEquals(resDateStr,"27.05.2021 14:04:15")
        }catch (e:Exception){
            Assert.fail("Cant convert server date string")
        }
    }
}