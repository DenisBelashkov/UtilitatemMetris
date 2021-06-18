package org.vsu.pt.team2.utilitatemmetrisapp.ui.components.fieldValidation

import android.content.Context

object PasswordValidator : IFieldValidator {
    override fun validate(value: String, context: Context): String {
        //todo check length, or something else
        return EmptyFieldValidator.validate(value, context)
    }
}