package com.fetch.exercise.data.model

typealias ItemCollection = List<ItemModel>

data class ItemModel (
  val id: Long = -1,
  val listId: Int = -1,
  val name: String? = null,
)
