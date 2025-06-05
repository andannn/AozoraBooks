/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.syncer.internal.util

import kotlinx.io.Buffer
import kotlinx.io.writeString
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
                    "062212","萎れた花・心の花輪","しおれたはな・こころのはなわ","しおれたはなこころのはなわ","","","Phka Sropon, Mealea Duong Chet","","","新字新仮名","あり",2023-07-20,2023-10-16,"https://www.aozora.gr.jp/cards/002321/card62212.html","002289","岡田","知子","おかだ","ともこ","おかた","ともこ","Okada","Tomoko","翻訳者","1966","","あり","萎れた花・心の花輪","公益財団法人　大同生命国際文化基金","2015（平成27）年9月18日","","","","","","","","","","","","","","公益財団法人大同生命国際文化基金","","","","","","","","","","",""
                    "062707","二十一世紀ミャンマー作品集","にじゅういっせいきミャンマーさくひんしゅう","にしゆういつせいきみやんまあさくひんしゆう","","","Hnah Hsal Tit Yarzu Myanmer Sar Baungzu","","","新字新仮名","あり",2024-07-18,2024-07-06,"https://www.aozora.gr.jp/cards/002395/card62707.html","002398","ゼーヤーリン","","ゼーヤーリン","","せえやありん","","Zeyar Lynn","","著者","1958","","あり","二十一世紀ミャンマー作品集","公益財団法人　大同生命国際文化基金","2015（平成27）年11月17日","","","","","","","","","","","","","","公益財団法人大同生命国際文化基金","","","","","","","","","","",""
                    """.trimIndent(),
                )
            }
        val result = buffer.parseAsBookModel().toList()
        println(result)
        assertEquals(2, result.size)
        assertEquals("Phka Sropon, Mealea Duong Chet", result[0].originalTitle)
        assertEquals("062212", result[0].bookId)
        assertEquals(null, result[0].categoryNo)
    }
}
