package org.vsu.pt.team2.utilitatemmetrisapp.ui.components.fieldValidation

object EmptyFieldValidator : IFieldValidator {
    //todo return string from resources, not hardcode like now
    override fun validate(value: String): String {
        if (value.isEmpty())
            return "Пустое значение"
        return ""
    }
}