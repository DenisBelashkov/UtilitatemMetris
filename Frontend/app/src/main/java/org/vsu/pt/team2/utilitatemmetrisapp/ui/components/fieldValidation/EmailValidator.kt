package org.vsu.pt.team2.utilitatemmetrisapp.ui.components.fieldValidation

import android.content.Context
import org.vsu.pt.team2.utilitatemmetrisapp.R

object EmailValidator : IFieldValidator {
    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    private val regex = emailPattern.toRegex()

    override fun validate(value: String, context: Context): String {
        EmptyFieldValidator.validate(value, context).also {
            if (it.isNotBlank())
                return it
        }

        return if (value.matches(regex))
            ""
        else
            context.getString(R.string.incorrect_email)

    }
}