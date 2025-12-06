package me.andannn.aozora.core.pagesource

import me.andannn.aozora.core.domain.model.AozoraElement
import me.andannn.aozora.core.domain.model.AozoraTextStyle
import me.andannn.aozora.core.pagesource.page.AozoraBlock

internal fun textBlockOf(
    vararg textElements: String,
    indent: Int = 0,
): AozoraBlock.TextBlock =
    AozoraBlock.TextBlock(
        blockIndex = 0,
        textStyle = AozoraTextStyle.PARAGRAPH,
        indent = indent,
        elements =
            textElements.map { text ->
                AozoraElement.Text(text)
            },
    )
