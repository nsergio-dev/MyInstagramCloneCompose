// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    //parcelize
    alias(libs.plugins.kotlin.parcelize) apply false
    // hilt
    alias(libs.plugins.hilt.android) apply false
    //ksp
    alias(libs.plugins.ksp) apply false
    //kover
    id("org.jetbrains.kotlinx.kover") version "0.6.1"
}