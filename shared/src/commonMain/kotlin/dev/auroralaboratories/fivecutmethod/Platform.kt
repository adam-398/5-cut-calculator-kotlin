package dev.auroralaboratories.fivecutmethod

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform