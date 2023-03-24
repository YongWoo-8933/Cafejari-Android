package com.software.cafejariapp.presentation.component

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.software.cafejariapp.presentation.theme.*

@Composable
fun CustomOutlinedField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    onFocusIn: () -> Unit,
    onFocusOut: () -> Unit,
    keyboardType: KeyboardType,
    imeAction: ImeAction,
    keyboardActions: KeyboardActions,
    isError: Boolean,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: (@Composable () -> Unit)? = null,
) {

    OutlinedTextField(
        value = value,
        enabled = enabled,
        readOnly = readOnly,
        onValueChange = onValueChange,
        textStyle = MaterialTheme.typography.body1,
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.body1,
                color = if (isError) CrowdedRed else Gray,
                overflow = TextOverflow.Visible
            )
        },
        singleLine = singleLine,
        modifier = modifier
            .height(70.dp)
            .onFocusChanged {
                if (it.isFocused) onFocusIn() else onFocusOut()
            },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType, imeAction = imeAction
        ),
        keyboardActions = keyboardActions,
        isError = isError,
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = TextBlack,
            backgroundColor = White,
            errorCursorColor = MaterialTheme.colors.error,
            errorBorderColor = MaterialTheme.colors.error,
            focusedBorderColor = LightGray,
            unfocusedBorderColor = MoreLightGray,
            disabledBorderColor = MoreLightGray,
            cursorColor = TextBlack,
            trailingIconColor = HeavyGray,
        ),
        shape = MaterialTheme.shapes.small
    )
}