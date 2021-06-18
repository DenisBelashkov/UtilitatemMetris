package org.vsu.pt.team2.utilitatemmetrisapp.ui.components.fieldValidation

import android.content.Context
import org.vsu.pt.team2.utilitatemmetrisapp.R

object EmptyFieldValidator : IFieldValidator {
    //todo return string from resources, not hardcode like now
    override fun validate(value: String, context: Context): String {
        if (value.isEmpty())
            return context.getString(R.string.empty_field)
        return ""
    }
}