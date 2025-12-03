/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import me.andannn.aozora.core.database.Tables.BOOK_TABLE

internal object BookColumns {
    const val BOOK_ID = "book_id"
    const val TITLE = "title"
    const val TITLE_KANA = "title_kana"
    const val TITLE_SORT_KANA = "title_sort_kana"
    const val SUBTITLE = "subtitle"
    const val SUBTITLE_KANA = "subtitle_kana"
    const val ORIGINAL_TITLE = "original_title"
    const val FIRST_APPEARANCE = "first_appearance"
    const val CATEGORY_NO = "category_no"
    const val ORTHOGRAPHY = "orthography"
    const val WORK_COPYRIGHT_FLAG = "work_copyright_flag"
    const val PUBLISH_DATE = "publish_date"
    const val LAST_UPDATE_DATE = "last_update_date"
    const val CARD_URL = "card_url"
    const val AUTHOR_ID = "author_id"
    const val AUTHOR_LAST_NAME = "author_last_name"
    const val AUTHOR_FIRST_NAME = "author_first_name"
    const val AUTHOR_LAST_NAME_KANA = "author_last_name_kana"
    const val AUTHOR_FIRST_NAME_KANA = "author_first_name_kana"
    const val AUTHOR_LAST_NAME_SORT_KANA = "author_last_name_sort_kana"
    const val AUTHOR_FIRST_NAME_SORT_KANA = "author_first_name_sort_kana"
    const val AUTHOR_LAST_NAME_ROMAJI = "author_last_name_romaji"
    const val AUTHOR_FIRST_NAME_ROMAJI = "author_first_name_romaji"
    const val AUTHOR_ROLE_FLAG = "author_role_flag"
    const val AUTHOR_BIRTH = "author_birth"
    const val AUTHOR_DEATH = "author_death"
    const val AUTHOR_COPYRIGHT_FLAG = "author_copyright_flag"
    const val SOURCE_BOOK1 = "source_book1"
    const val SOURCE_PUBLISHER1 = "source_publisher1"
    const val SOURCE_PUB_YEAR1 = "source_pub_year1"
    const val INPUT_EDITION1 = "input_edition1"
    const val PROOF_EDITION1 = "proof_edition1"
    const val PARENT_SOURCE_BOOK1 = "parent_source_book1"
    const val PARENT_SOURCE_PUBLISHER1 = "parent_source_publisher1"
    const val PARENT_SOURCE_PUB_YEAR1 = "parent_source_pub_year1"
    const val SOURCE_BOOK2 = "source_book2"
    const val SOURCE_PUBLISHER2 = "source_publisher2"
    const val SOURCE_PUB_YEAR2 = "source_pub_year2"
    const val INPUT_EDITION2 = "input_edition2"
    const val PROOF_EDITION2 = "proof_edition2"
    const val PARENT_SOURCE_BOOK2 = "parent_source_book2"
    const val PARENT_SOURCE_PUBLISHER2 = "parent_source_publisher2"
    const val PARENT_SOURCE_PUB_YEAR2 = "parent_source_pub_year2"
    const val INPUT_BY = "input_by"
    const val PROOF_BY = "proof_by"
    const val TEXT_FILE_URL = "text_file_url"
    const val TEXT_FILE_LAST_UPDATE = "text_file_last_update"
    const val TEXT_FILE_ENCODING = "text_file_encoding"
    const val TEXT_FILE_CHARSET = "text_file_charset"
    const val TEXT_FILE_REVISION = "text_file_revision"
    const val HTML_FILE_URL = "html_file_url"
    const val HTML_FILE_LAST_UPDATE = "html_file_last_update"
    const val HTML_FILE_ENCODING = "html_file_encoding"
    const val HTML_FILE_CHARSET = "html_file_charset"
    const val HTML_FILE_REVISION = "html_file_revision"
}

/**
 * | CSV 字段名               | 数据库字段名（BookColumns）      |
 * |--------------------------|----------------------------------|
 * | 作品ID                   | book_id                         |
 * | 作品名                   | title                           |
 * | 作品名読み               | title_kana                      |
 * | ソート用読み             | title_sort_kana                 |
 * | 副題                     | subtitle                        |
 * | 副題読み                 | subtitle_kana                   |
 * | 原題                     | original_title                  |
 * | 初出                     | first_appearance                |
 * | 分類番号                 | category_no                     |
 * | 文字遣い種別             | orthography                     |
 * | 作品著作権フラグ         | work_copyright_flag             |
 * | 公開日                   | publish_date                    |
 * | 最終更新日               | last_update_date                |
 * | 図書カードURL            | card_url                        |
 * | 人物ID                   | author_id                       |
 * | 姓                       | author_last_name                |
 * | 名                       | author_first_name               |
 * | 姓読み                   | author_last_name_kana           |
 * | 名読み                   | author_first_name_kana          |
 * | 姓読みソート用           | author_last_name_sort_kana      |
 * | 名読みソート用           | author_first_name_sort_kana     |
 * | 姓ローマ字               | author_last_name_romaji         |
 * | 名ローマ字               | author_first_name_romaji        |
 * | 役割フラグ               | author_role_flag                |
 * | 生年月日                 | author_birth                    |
 * | 没年月日                 | author_death                    |
 * | 人物著作権フラグ         | author_copyright_flag           |
 * | 底本名1                  | source_book1                    |
 * | 底本出版社名1            | source_publisher1               |
 * | 底本初版発行年1          | source_pub_year1                |
 * | 入力に使用した版1        | input_edition1                  |
 * | 校正に使用した版1        | proof_edition1                  |
 * | 底本の親本名1            | parent_source_book1             |
 * | 底本の親本出版社名1      | parent_source_publisher1        |
 * | 底本の親本初版発行年1    | parent_source_pub_year1         |
 * | 底本名2                  | source_book2                    |
 * | 底本出版社名2            | source_publisher2               |
 * | 底本初版発行年2          | source_pub_year2                |
 * | 入力に使用した版2        | input_edition2                  |
 * | 校正に使用した版2        | proof_edition2                  |
 * | 底本の親本名2            | parent_source_book2             |
 * | 底本の親本出版社名2      | parent_source_publisher2        |
 * | 底本の親本初版発行年2    | parent_source_pub_year2         |
 * | 入力者                   | input_by                        |
 * | 校正者                   | proof_by                        |
 * | テキストファイルURL      | text_file_url                   |
 * | テキストファイル最終更新日| text_file_last_update           |
 * | テキストファイル符号化方式| text_file_encoding              |
 * | テキストファイル文字集合 | text_file_charset               |
 * | テキストファイル修正回数 | text_file_revision              |
 * | XHTML/HTMLファイルURL    | html_file_url                   |
 * | XHTML/HTMLファイル最終更新日| html_file_last_update         |
 * | XHTML/HTMLファイル符号化方式| html_file_encoding            |
 * | XHTML/HTMLファイル文字集合| html_file_charset              |
 * | XHTML/HTMLファイル修正回数| html_file_revision             |
 */
@Entity(
    tableName = BOOK_TABLE,
    primaryKeys = [BookColumns.BOOK_ID, BookColumns.AUTHOR_ID],
)
data class BookEntity(
    @ColumnInfo(name = BookColumns.BOOK_ID)
    val bookId: String,
    @ColumnInfo(name = BookColumns.TITLE)
    val title: String,
    @ColumnInfo(name = BookColumns.TITLE_KANA)
    val titleKana: String,
    @ColumnInfo(name = BookColumns.TITLE_SORT_KANA)
    val titleSortKana: String?,
    @ColumnInfo(name = BookColumns.SUBTITLE)
    val subtitle: String?,
    @ColumnInfo(name = BookColumns.SUBTITLE_KANA)
    val subtitleKana: String?,
    @ColumnInfo(name = BookColumns.ORIGINAL_TITLE)
    val originalTitle: String?,
    @ColumnInfo(name = BookColumns.FIRST_APPEARANCE)
    val firstAppearance: String?,
    @ColumnInfo(name = BookColumns.CATEGORY_NO)
    val categoryNo: String?,
    @ColumnInfo(name = BookColumns.ORTHOGRAPHY)
    val orthography: String?,
    @ColumnInfo(name = BookColumns.WORK_COPYRIGHT_FLAG)
    val workCopyrightFlag: String?,
    @ColumnInfo(name = BookColumns.PUBLISH_DATE)
    val publishDate: String?,
    @ColumnInfo(name = BookColumns.LAST_UPDATE_DATE)
    val lastUpdateDate: String?,
    @ColumnInfo(name = BookColumns.CARD_URL)
    val cardUrl: String,
    @ColumnInfo(name = BookColumns.AUTHOR_ID)
    val authorId: String,
    @ColumnInfo(name = BookColumns.AUTHOR_LAST_NAME)
    val authorLastName: String,
    @ColumnInfo(name = BookColumns.AUTHOR_FIRST_NAME)
    val authorFirstName: String,
    @ColumnInfo(name = BookColumns.AUTHOR_LAST_NAME_KANA)
    val authorLastNameKana: String?,
    @ColumnInfo(name = BookColumns.AUTHOR_FIRST_NAME_KANA)
    val authorFirstNameKana: String?,
    @ColumnInfo(name = BookColumns.AUTHOR_LAST_NAME_SORT_KANA)
    val authorLastNameSortKana: String?,
    @ColumnInfo(name = BookColumns.AUTHOR_FIRST_NAME_SORT_KANA)
    val authorFirstNameSortKana: String?,
    @ColumnInfo(name = BookColumns.AUTHOR_LAST_NAME_ROMAJI)
    val authorLastNameRomaji: String?,
    @ColumnInfo(name = BookColumns.AUTHOR_FIRST_NAME_ROMAJI)
    val authorFirstNameRomaji: String?,
    @ColumnInfo(name = BookColumns.AUTHOR_ROLE_FLAG)
    val authorRoleFlag: String?,
    @ColumnInfo(name = BookColumns.AUTHOR_BIRTH)
    val authorBirth: String?,
    @ColumnInfo(name = BookColumns.AUTHOR_DEATH)
    val authorDeath: String?,
    @ColumnInfo(name = BookColumns.AUTHOR_COPYRIGHT_FLAG)
    val authorCopyrightFlag: String?,
    @ColumnInfo(name = BookColumns.SOURCE_BOOK1)
    val sourceBook1: String?,
    @ColumnInfo(name = BookColumns.SOURCE_PUBLISHER1)
    val sourcePublisher1: String?,
    @ColumnInfo(name = BookColumns.SOURCE_PUB_YEAR1)
    val sourcePubYear1: String?,
    @ColumnInfo(name = BookColumns.INPUT_EDITION1)
    val inputEdition1: String?,
    @ColumnInfo(name = BookColumns.PROOF_EDITION1)
    val proofEdition1: String?,
    @ColumnInfo(name = BookColumns.PARENT_SOURCE_BOOK1)
    val parentSourceBook1: String?,
    @ColumnInfo(name = BookColumns.PARENT_SOURCE_PUBLISHER1)
    val parentSourcePublisher1: String?,
    @ColumnInfo(name = BookColumns.PARENT_SOURCE_PUB_YEAR1)
    val parentSourcePubYear1: String?,
    @ColumnInfo(name = BookColumns.SOURCE_BOOK2)
    val sourceBook2: String?,
    @ColumnInfo(name = BookColumns.SOURCE_PUBLISHER2)
    val sourcePublisher2: String?,
    @ColumnInfo(name = BookColumns.SOURCE_PUB_YEAR2)
    val sourcePubYear2: String?,
    @ColumnInfo(name = BookColumns.INPUT_EDITION2)
    val inputEdition2: String?,
    @ColumnInfo(name = BookColumns.PROOF_EDITION2)
    val proofEdition2: String?,
    @ColumnInfo(name = BookColumns.PARENT_SOURCE_BOOK2)
    val parentSourceBook2: String?,
    @ColumnInfo(name = BookColumns.PARENT_SOURCE_PUBLISHER2)
    val parentSourcePublisher2: String?,
    @ColumnInfo(name = BookColumns.PARENT_SOURCE_PUB_YEAR2)
    val parentSourcePubYear2: String?,
    @ColumnInfo(name = BookColumns.INPUT_BY)
    val inputBy: String?,
    @ColumnInfo(name = BookColumns.PROOF_BY)
    val proofBy: String?,
    @ColumnInfo(name = BookColumns.TEXT_FILE_URL)
    val textFileUrl: String?,
    @ColumnInfo(name = BookColumns.TEXT_FILE_LAST_UPDATE)
    val textFileLastUpdate: String?,
    @ColumnInfo(name = BookColumns.TEXT_FILE_ENCODING)
    val textFileEncoding: String?,
    @ColumnInfo(name = BookColumns.TEXT_FILE_CHARSET)
    val textFileCharset: String?,
    @ColumnInfo(name = BookColumns.TEXT_FILE_REVISION)
    val textFileRevision: String?,
    @ColumnInfo(name = BookColumns.HTML_FILE_URL)
    val htmlFileUrl: String?,
    @ColumnInfo(name = BookColumns.HTML_FILE_LAST_UPDATE)
    val htmlFileLastUpdate: String?,
    @ColumnInfo(name = BookColumns.HTML_FILE_ENCODING)
    val htmlFileEncoding: String?,
    @ColumnInfo(name = BookColumns.HTML_FILE_CHARSET)
    val htmlFileCharset: String?,
    @ColumnInfo(name = BookColumns.HTML_FILE_REVISION)
    val htmlFileRevision: String?,
)
