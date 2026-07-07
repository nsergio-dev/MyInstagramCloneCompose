package com.nsergio.dev.myinstagramcompose.features.common

import java.util.Locale
import kotlin.random.Random

object FakeNameUser {

    private val maleNames = listOf(
        "Carlos", "Luis", "Juan", "Pedro", "Andrés",
        "Mateo", "Felipe", "Sebastián", "David", "Tomás",
        "Julián", "Santiago", "Alejandro", "Nicolás", "Esteban",
        "Miguel", "Iván", "Adrián", "Gustavo", "Héctor", "John", "Mike"
    )

    private val femaleNames = listOf(
        "Ana", "María", "Sofía", "Camila", "Valentina",
        "Daniela", "Isabella", "Gabriela", "Manuela", "Lucía",
        "Paula", "Juliana", "Catalina", "Andrea", "Fernanda",
        "Mónica", "Laura", "Ángela", "Patricia", "Carolina",
        "Chloe", "Lily"
    )

    private val lastNames = listOf(
        "Pérez", "Gómez", "Torres", "Rodríguez", "Martínez", "Herrera",
        "López", "Sánchez", "Ramírez", "Castro", "Moreno", "Ortiz",
        "Jiménez", "Vargas", "Mendoza", "Cortés", "Navarro", "Guerrero", "Romero", "Silva",
        "Miller", "Brown", "Doe", "Williams", "Johnson", "Garcia", "Martinez"
    )

    fun getFullName(isFemale: Boolean = Random.nextBoolean()): NameUser {
        val lastName = lastNames.random()
        var nickName: String

        val userName: String = if (isFemale) {
            femaleNames.random()
        } else {
            maleNames.random()
        }

        nickName = "${userName.lowercase(Locale.ROOT)}_${lastName.lowercase(Locale.ROOT)}"
        return NameUser(
            nickName = nickName,
            realName = "$userName $lastName"
        )
    }
}

data class NameUser(
    val nickName: String,
    val realName: String
)