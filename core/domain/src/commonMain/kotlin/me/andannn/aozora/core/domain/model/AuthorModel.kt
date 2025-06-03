package me.andannn.aozora.core.domain.model

data class AuthorModel(
    val authorId: String,
    val lastName: String,
    val firstName: String,
    val lastNameKana: String?,
    val firstNameKana: String?,
    val lastNameSortKana: String?,
    val firstNameSortKana: String?,
    val lastNameRomaji: String?,
    val firstNameRomaji: String?,
    val birth: String?,
    val death: String?,
    val copyrightFlag: String?,
)
