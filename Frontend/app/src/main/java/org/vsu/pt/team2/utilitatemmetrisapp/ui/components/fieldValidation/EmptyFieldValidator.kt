package org.vsu.pt.team2.utilitatemmetrisapp.ui.components.fieldValidation

import android.content.Context
import org.vsu.pt.team2.utilitatemmetrisapp.R

object EmptyFieldValidator : IFieldValidator {
    override fun validate(value: String, context: Context): String {
        if (value.isEmpty())
            return context.getString(R.string.empty_field)
        return ""
    }
}