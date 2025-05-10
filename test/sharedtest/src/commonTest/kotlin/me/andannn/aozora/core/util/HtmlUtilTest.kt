/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.util

import me.andannn.core.util.parseBookSourceAsAnnotatedString
import kotlin.test.Test

class HtmlUtilTest {
    @Test
    fun parseHtmlAsAnnotatedStringTest() {
        val string =
            parseBookSourceAsAnnotatedString(
                """
                <div class="bibliographical_information">
                <hr />
                <br />
                底本：「異妖の怪談集　岡本綺堂伝奇小説集　其ノ二」原書房
                <br />
                　　　1999（平成11）年7月2日第1刷<br />
                初出：「文學時代」<br />
                　　　1932（昭和7）年2月<br />
                入力：網迫、土屋隆<br />
                校正：門田裕志、小林繁雄<br />
                2005年6月26日作成<br />
                青空文庫作成ファイル：<br />
                このファイルは、インターネットの図書館、<a href="http://www.aozora.gr.jp/">青空文庫（http://www.aozora.gr.jp/）</a>で作られました。入力、校正、制作にあたったのは、ボランティアの皆さんです。<br />
                <br />
                <br />
                </div>
                """.trimIndent(),
            )
        println(string)
    }
}
