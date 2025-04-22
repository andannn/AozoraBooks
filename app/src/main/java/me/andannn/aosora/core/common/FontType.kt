package me.andannn.aosora.core.common

import android.graphics.Typeface
import androidx.core.content.res.ResourcesCompat
import me.andannn.aosora.AozoraApplication
import me.andannn.aosora.R

enum class FontType {
    NOTO_SANS,
    NOTO_SERIF,
    ;
    companion object {
        // ANDROID Default. No need setting typeface to paint.
        val DEFAULT = NOTO_SANS
    }
}

fun FontType.getTypeface(): Typeface? {
    return ResourcesCompat.getFont(AozoraApplication.context, R.font.noto_serif_jp_regular)
}