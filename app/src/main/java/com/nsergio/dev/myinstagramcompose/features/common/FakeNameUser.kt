package com.nsergio.dev.myinstagramcompose.features.common

import kotlin.random.Random

object FakeNameUser {

    private val maleNames = listOf(
        "Carlos", "Luis", "Juan", "Pedro", "Andrés",
        "Mateo", "Felipe", "Sebastián", "David", "Tomás",
        "Julián", "Santiago", "Alejandro", "Nicolás", "Esteban",
        "Miguel", "Iván", "Adrián", "Gustavo", "Héctor"
    )

    private val femaleNames = listOf(
        "Ana", "María", "Sofía", "Camila", "Valentina",
        "Daniela", "Isabella", "Gabriela", "Manuela", "Lucía",
        "Paula", "Juliana", "Catalina", "Andrea", "Fernanda",
        "Mónica", "Laura", "Ángela", "Patricia", "Carolina"
    )

    private val lastNames = listOf(
        "Pérez", "Gómez", "Torres", "Rodríguez", "Martínez", "Herrera",
        "López", "Sánchez", "Ramírez", "Castro", "Moreno", "Ortiz",
        "Jiménez", "Vargas", "Mendoza", "Cortés", "Navarro", "Guerrero", "Romero", "Silva"
    )

    fun getFullName(isFemale: Boolean = Random.nextBoolean()): String {
        val lastName = lastNames.random().replaceFirstChar { it.lowercase() }

        val userName: String = if (isFemale) {
            val name = femaleNames.random().replaceFirstChar { it.lowercase() }
            "${name}_$lastName"
        } else {
            val name = maleNames.random().replaceFirstChar { it.lowercase() }
            "${name}_$lastName"
        }
        return userName
    }
}