package org.vsu.pt.team2.utilitatemmetrisapp.ui.components.fieldValidation

object EmailValidator : IFieldValidator {
    override fun validate(value: String): String {
        //todo check for correct email
        return EmptyFieldValidator.validate(value)
    }
}