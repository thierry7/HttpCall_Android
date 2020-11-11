package com.thierryoke.googlebootsapi


data class BooksResponse(
    val items: List<ItemsDescription>
)

data class ItemsDescription (
    val volumeInfo: VolumeInfo
)



data class VolumeInfo(
    val title: String,
    val subtitle: String
)