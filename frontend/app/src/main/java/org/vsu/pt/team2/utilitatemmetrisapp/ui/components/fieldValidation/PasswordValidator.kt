package org.vsu.pt.team2.utilitatemmetrisapp.ui.components.fieldValidation

object PasswordValidator : IFieldValidator {
    override fun validate(value: String): String {
        //todo check length, or something else
        return EmptyFieldValidator.validate(value)
    }
}