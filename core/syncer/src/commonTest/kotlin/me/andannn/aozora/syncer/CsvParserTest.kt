/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.syncer

import kotlinx.io.Buffer
import kotlinx.io.writeString
import me.andannn.aozora.syncer.internal.util.parseAsBookModel
import kotlin.test.Test
import kotlin.test.assertEquals

class CsvParserTest {
    @Test
    fun parseBookModelCsvTest() {
        val buffer =
            Buffer().apply {
                writeString(
                    """
                    作品ID,作品名,作品名読み,ソート用読み,副題,副題読み,原題,初出,分類番号,文字遣い種別,作品著作権フラグ,公開日,最終更新日,図書カードURL,人物ID,姓,名,姓読み,名読み,姓読みソート用,名読みソート用,姓ローマ字,名ローマ字,役割フラグ,生年月日,没年月日,人物著作権フラグ,底本名1,底本出版社名1,底本初版発行年1,入力に使用した版1,校正に使用した版1,底本の親本名1,底本の親本出版社名1,底本の親本初版発行年1,底本名2,底本出版社名2,底本初版発行年2,入力に使用した版2,校正に使用した版2,底本の親本名2,底本の親本出版社名2,底本の親本初版発行年2,入力者,校正者,テキストファイルURL,テキストファイル最終更新日,テキストファイル符号化方式,テキストファイル文字集合,テキストファイル修正回数,XHTML/HTMLファイルURL,XHTML/HTMLファイル最終更新日,XHTML/HTMLファイル符号化方式,XHTML/HTMLファイル文字集合,XHTML/HTMLファイル修正回数
                    "059898","ウェストミンスター寺院","ウェストミンスターじいん","うえすとみんすたあしいん","","","","","NDC 933","新字新仮名","なし",2020-04-03,2020-03-28,"https://www.aozora.gr.jp/cards/001257/card59898.html","001257","アーヴィング","ワシントン","アーヴィング","ワシントン","ああういんく","わしんとん","Irving","Washington","著者","1783-04-03","1859-11-28","なし","スケッチ・ブック","新潮文庫、新潮社","1957（昭和32）年5月20日","2000（平成12）年2月20日33刷改版","2000（平成12）年2月20日33刷改版","","","","","","","","","","","","えにしだ","砂場清隆","https://www.aozora.gr.jp/cards/001257/files/59898_ruby_70679.zip","2020-03-28","ShiftJIS","JIS X 0208","0","https://www.aozora.gr.jp/cards/001257/files/59898_70731.html","2020-03-28","ShiftJIS","JIS X 0208","0"
                    "056078","駅伝馬車","えきでんばしゃ","えきてんはしや","","","","","NDC 933","旧字旧仮名","なし",2013-09-20,2014-09-16,"https://www.aozora.gr.jp/cards/001257/card56078.html","001257","アーヴィング","ワシントン","アーヴィング","ワシントン","ああういんく","わしんとん","Irving","Washington","著者","1783-04-03","1859-11-28","なし","スケッチ・ブック","岩波文庫、岩波書店","1935（昭和10）年9月15日","2010（平成22）年2月23日第31刷","1992（平成4）年2月26日第30刷","","","","","","","","","","","","雀","小林繁雄","https://www.aozora.gr.jp/cards/001257/files/56078_ruby_51155.zip","2013-09-03","ShiftJIS","JIS X 0208","0","https://www.aozora.gr.jp/cards/001257/files/56078_51422.html","2013-09-03","ShiftJIS","JIS X 0208","0"
                    "060224","駅馬車","えきばしゃ","えきはしや","","","","","NDC 933","新字新仮名","なし",2021-05-18,2021-04-27,"https://www.aozora.gr.jp/cards/001257/card60224.html","001257","アーヴィング","ワシントン","アーヴィング","ワシントン","ああういんく","わしんとん","Irving","Washington","著者","1783-04-03","1859-11-28","なし","スケッチ・ブック","新潮文庫、新潮社","1957（昭和32）年5月20日","2000（平成12）年2月20日33刷改版","2000（平成12）年2月20日33刷改版","","","","","","","","","","","","砂場清隆","noriko saito","https://www.aozora.gr.jp/cards/001257/files/60224_ruby_73172.zip","2021-04-27","ShiftJIS","JIS X 0208","0","https://www.aozora.gr.jp/cards/001257/files/60224_73212.html","2021-04-27","ShiftJIS","JIS X 0208","0"
                    """.trimIndent(),
                )
            }
        val result = buffer.parseAsBookModel().toList()
        assertEquals(3, result.size)
        assertEquals("059898", result.first().bookId)
    }
}
