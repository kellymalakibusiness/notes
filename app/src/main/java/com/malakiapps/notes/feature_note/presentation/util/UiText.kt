package com.malakiapps.notes.feature_note.presentation.util

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed class UiText {
    data class DynamicString(val value: String): UiText()
    class StringResource(
        @StringRes val resId: Int,
        vararg val args: UiText
    ): UiText()

    @Composable
    fun asString(): String{
        return when(this){
            is DynamicString -> value
            is StringResource -> {
                val stringArgs = args.simplifyArgs()
                stringResource(
                    id = resId,
                    *stringArgs
                )
            }
        }
    }

    //For effects
    fun asString(context: Context): String{
        return when(this){
            is DynamicString -> value
            is StringResource -> {
                val stringArgs = args.simplifyArgs(context)
                context.getString(resId, *stringArgs)
            }
        }
    }
}

@Composable
private fun Array<out UiText>.simplifyArgs(): Array<String>{
    return map {
        when(it){
            is UiText.DynamicString -> it.value
            is UiText.StringResource -> stringResource(it.resId)
        }
    }.toTypedArray()
}

private fun Array<out UiText>.simplifyArgs(context: Context): Array<String>{
    return map {
        when(it){
            is UiText.DynamicString -> it.value
            is UiText.StringResource -> context.getString(it.resId)
        }
    }.toTypedArray()
}