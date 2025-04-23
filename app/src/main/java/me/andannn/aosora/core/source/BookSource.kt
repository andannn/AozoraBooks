package me.andannn.aosora.core.source

import me.andannn.aosora.core.pager.AozoraPage
import me.andannn.aosora.core.common.model.PageMetaData
import me.andannn.aosora.core.pager.generatePageSequence
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.useLines
import kotlin.streams.asSequence

class BookSource(
    val dictionary: Path,
) {
    val bookPath: Path by lazy {
        Files.walk(dictionary)
            .asSequence()
            .first { it.toString().endsWith(".txt") }
    }

    fun pageSource(meta: PageMetaData) : Sequence<AozoraPage> = sequence {
        bookPath.useLines(
            charset = Charset.forName("Shift_JIS"),
            block = { generatePageSequence(it, meta) }
        )
    }
}


