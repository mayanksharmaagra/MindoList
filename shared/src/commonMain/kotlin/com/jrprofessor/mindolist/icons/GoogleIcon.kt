package com.jrprofessor.mindolist.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.unit.dp

/**
 * The standard multicolor Google "G" mark, as a Compose ImageVector.
 * Built by parsing the same SVG path data Google publishes for the icon
 * (four filled quadrants: blue, green, yellow, red) via [PathParser], the
 * same technique Android Studio's Vector Asset Studio uses when it
 * generates ImageVectors from raw SVG/AVD sources.
 *
 * Usage:
 *   Icon(imageVector = GoogleLogo, contentDescription = "Google", tint = Color.Unspecified, modifier = Modifier.size(20.dp))
 *   (tint MUST be Color.Unspecified — otherwise Icon flattens all four colors into one)
 */
val GoogleLogo: ImageVector
    get() {
        if (_GoogleLogo != null) return _GoogleLogo!!
        _GoogleLogo = ImageVector.Builder(
            name = "GoogleLogo",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {

            // Blue — right arc / the "swoosh" into the crossbar
            addPath(
                pathData = PathParser().parsePathString(
                    "M23.5 12.3c0,-0.8 -0.1,-1.6 -0.2,-2.3H12v4.5h6.5c-0.3,1.5 -1.2,2.8 -2.5,3.6v3h4C22.2,19.2 23.5,16 23.5,12.3z"
                ).toNodes(),
                fill = SolidColor(Color(0xFF4285F4))
            )

            // Green — bottom arc
            addPath(
                pathData = PathParser().parsePathString(
                    "M12 24c3.2,0 6,-1.1 7.9,-2.9l-4,-3c-1.1,0.7 -2.5,1.2 -3.9,1.2 -3,0 -5.6,-2 -6.5,-4.8H1.4v3.1C3.3,21.3 7.3,24 12,24z"
                ).toNodes(),
                fill = SolidColor(Color(0xFF34A853))
            )

            // Yellow — left arc
            addPath(
                pathData = PathParser().parsePathString(
                    "M5.5 14.5c-0.2,-0.7 -0.4,-1.4 -0.4,-2.2s0.1,-1.5 0.4,-2.2V7H1.4C0.6,8.6 0,10.3 0,12.3s0.6,3.7 1.4,5.3z"
                ).toNodes(),
                fill = SolidColor(Color(0xFFFBBC05))
            )

            // Red — top arc
            addPath(
                pathData = PathParser().parsePathString(
                    "M12 4.8c1.7,0 3.3,0.6 4.5,1.8l3.4,-3.4C17.9,1.2 15.1,0 12,0 7.3,0 3.3,2.7 1.4,6.7l4.1,3.2C6.4,6.8 9,4.8 12,4.8z"
                ).toNodes(),
                fill = SolidColor(Color(0xFFEA4335))
            )
        }.build()
        return _GoogleLogo!!
    }

private var _GoogleLogo: ImageVector? = null