package com.software.cafejariapp.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.software.cafejariapp.presentation.theme.TextBlack
import com.software.cafejariapp.presentation.theme.Transparent
import com.software.cafejariapp.presentation.theme.White

@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    shape: Shape = MaterialTheme.shapes.small,
    text: String? = null,
    borderColor: Color = Transparent,
    elevation: Dp = 0.dp,
    contentPadding: Dp = 0.dp,
    textStyle: TextStyle = MaterialTheme.typography.body2,
    textColor: Color = TextBlack,
    backgroundColor: Color = White,
    disabledBackgroundColor: Color = White,
    iconImageResource: Int? = null,
    iconColor: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
    enabled: Boolean = true,
) {

    Button(
        modifier = modifier,
        onClick = onClick,
        shape = shape,
        border = BorderStroke(width = 1.dp, color = borderColor),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor, disabledBackgroundColor = disabledBackgroundColor
        ),
        elevation = ButtonDefaults.elevation(
            defaultElevation = elevation, pressedElevation = 0.dp
        ),
        enabled = enabled,
        contentPadding = PaddingValues(contentPadding)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (iconImageResource != null) {
                Icon(
                    painter = painterResource(id = iconImageResource),
                    contentDescription = text,
                    tint = iconColor
                )

                HorizontalSpacer(width = 5.dp)
            }
            if (text != null) {
                Text(
                    text = text,
                    style = textStyle,
                    textAlign = TextAlign.Center,
                    color = textColor
                )
            }
        }
    }
}

@Composable
fun PrimaryCtaButton(
    text: String,

    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    enabled: Boolean = true,
    isProgress: Boolean = false
) {
    Button(
        modifier = modifier.fillMaxWidth().height(60.dp),
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.primary,
            disabledBackgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.5f)
        ),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 0.dp, pressedElevation = (-2).dp
        ),
        enabled = enabled
    ) {
        if (isProgress) {
            CircularProgressIndicator(
                modifier = Modifier.size(16.dp),
                color = White,
                strokeWidth = 2.dp
            )
        } else {
            Text(
                text = text,
                style = MaterialTheme.typography.subtitle2,
                textAlign = TextAlign.Center,
                color = White
            )
        }
    }
}


@Composable
fun BorderedCtaButton(
    text: String,

    modifier: Modifier = Modifier, onClick: () -> Unit = {}, isProgress: Boolean = false
) {
    Button(
        modifier = modifier.fillMaxWidth().height(60.dp),
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = White
        ),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 0.dp, pressedElevation = (-2).dp
        ),
        border = BorderStroke(width = (1.5).dp, color = MaterialTheme.colors.primary)
    ) {
        if (isProgress) {
            CircularProgressIndicator(
                modifier = Modifier.size(16.dp),
                color = MaterialTheme.colors.primary,
                strokeWidth = 2.dp
            )
        } else {
            Text(
                text = text,
                style = MaterialTheme.typography.subtitle2,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.primary
            )
        }
    }
}