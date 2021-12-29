package org.saulmm.marvel.characters.data.remote.models

enum class CharacterOrderDto(val value: String) {
    NAME("name"),
    MODIFIED("modified"),
    NAME_DESC("-name"),
    MODIFIED_DESC("-modified")
}