package com.example.ambrosianaapp.components


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import com.example.ambrosianaapp.ui.theme.AmbrosianaColor

@Composable
fun AmbrosianaTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            // Unfocused colors
            unfocusedBorderColor = AmbrosianaColor.Green,
            unfocusedLabelColor = AmbrosianaColor.Green,
            unfocusedTextColor = AmbrosianaColor.Black,

            // Focused colors
            focusedBorderColor = AmbrosianaColor.Green,
            focusedLabelColor = AmbrosianaColor.Green,
            focusedTextColor = AmbrosianaColor.Black,

            // Error colors
            errorBorderColor = AmbrosianaColor.Details,
            errorLabelColor = AmbrosianaColor.Details,
            errorTextColor = AmbrosianaColor.Details,

            // Disabled colors
            disabledBorderColor = AmbrosianaColor.Details,
            disabledLabelColor = AmbrosianaColor.Details,
            disabledTextColor = AmbrosianaColor.Details,

            // Container (background) colors
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            errorContainerColor = Color.Transparent,

            // Cursor and selection colors
            cursorColor = AmbrosianaColor.Green,
            selectionColors = TextSelectionColors(
                handleColor = AmbrosianaColor.Green,
                backgroundColor = AmbrosianaColor.Green.copy(alpha = 0.4f)
            )
        ),
        singleLine = true,
        isError = isError,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon
    )
}