package org.vsu.pt.team2.utilitatemmetrisapp

import android.content.Context
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.runners.MockitoJUnitRunner
import org.vsu.pt.team2.utilitatemmetrisapp.ui.components.fieldValidation.EmailValidator

@RunWith(MockitoJUnitRunner::class)
class EmailValidatorTest {
    val incorrect_email = "ie"
    val empty_field = "ef"

    @Mock
    private lateinit var mockContext: Context

    @Test
    fun emailValidator_CorrectEmailSimple_ReturnsTrue() {
        // Given a mocked Context injected into the object under test...
        Mockito.`when`(mockContext.getString(R.string.incorrect_email))
            .thenReturn(incorrect_email)
        Mockito.`when`(mockContext.getString(R.string.empty_field))
            .thenReturn(empty_field)

        Assert.assertEquals(
            EmailValidator.validate("name@email.com", mockContext), ""
        )
        Assert.assertEquals(
            EmailValidator.validate("name@email", mockContext), incorrect_email
        )
        Assert.assertEquals(
            EmailValidator.validate("", mockContext), empty_field
        )
    }
}