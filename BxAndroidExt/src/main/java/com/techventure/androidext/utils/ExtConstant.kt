package com.techventure.androidext.utils

object ExtConstant {

    const val APP_NAME = "salbi_app"
    // String Integer region
    const val ZERO = 0
    const val ONE = 1
    // end region

    // String Float region
    const val ONE_FLOAT = 1f
    // end region

    // String character region
    const val HIPHAN = "-"
    const val UNDER_SCORE = "_"
    const val SPACE = " "
    const val EMPTY_STRING = ""
    const val PLUS = "+"
    const val COMMA = ","
    const val APPOSTROPHY = "'"
    const val DOT = "."
    // end region

    // region file type
    const val MB = "MB"
    const val KB = "KB"
    const val GB = "GB"
    const val PNG_MIME_TYPE = "image/png"
    const val JPG_MIME_TYPE = "image/jpg"
    const val JPEG_MIME_TYPE = "image/jpeg"
    val IMAGE_EXTENSION = mutableListOf<String>("png", "jpg", "jpeg")
    val MEDIA_MIME_TYPE= mutableListOf<String>("image/png", "image/jpg", "image/jpeg")
    val NON_MEDIA_MIME_TYPE= mutableListOf<String>("application/pdf", "application/msword")
    const val PDF_MIME_TYPE = "application/pdf"
    const val DOCX_MIME_TYPE = "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    const val DOCX_DATA = "data:${DOCX_MIME_TYPE};base64,"
    const val PDF_DATA = "data:${PDF_MIME_TYPE};base64,"
    const val DOC_MIME_TYPE = "application/msword"
    const val PDF_EXTENSION = ".pdf"
    const val DOCX_EXTENSION = ".docx"
    const val JPG_EXTENSION = ".jpg"
    const val PNG_EXTENSION = ".png"
    const val JPEG_EXTENSION = ".jpeg"
    const val IMAGE_FILE_TYPE = "img"
    const val APP_PDF = "application/pdf"
    const val ALL_TYPE = "*/*"
    // end region
}