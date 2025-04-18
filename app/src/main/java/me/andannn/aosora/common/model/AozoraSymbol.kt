package me.andannn.aosora.common.model

/**
 * 【テキスト中に現れる記号について】
 *
 * 《》：ルビ
 * （例）吾輩《わがはい》
 *
 * ｜：ルビの付く文字列の始まりを特定する記号
 * （例）一番｜獰悪《どうあく》
 *
 * ［＃］：入力者注　主に外字の説明や、傍点の位置の指定
 * 　　　（数字は、JIS X 0213の面区点番号またはUnicode、底本のページと行数）
 * （例）※［＃「言＋墟のつくり」、第4水準2-88-74］
 *
 * 〔〕：アクセント分解された欧文をかこむ
 * （例）〔Quid aliud est mulier nisi amicitiae& inimica〕
 * アクセント分解についての詳細は下記URLを参照してください
 * http://www.aozora.gr.jp/accent_separation.html
 */
enum class AozoraSymbol(
    val symbol: Char
) {
    RubyStart(symbol = '《'),
    RubyEnd(symbol = '》'),
    RubyStartMark(symbol = '｜'),
}