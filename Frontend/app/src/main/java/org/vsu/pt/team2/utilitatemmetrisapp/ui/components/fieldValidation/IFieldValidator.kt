package org.vsu.pt.team2.utilitatemmetrisapp.ui.components.fieldValidation

import android.content.Context

interface IFieldValidator {
    fun validate(value: String, context: Context): String
}