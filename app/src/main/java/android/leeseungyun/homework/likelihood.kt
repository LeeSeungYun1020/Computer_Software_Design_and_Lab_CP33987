package android.leeseungyun.homework

import kotlin.math.pow
import kotlin.math.sqrt

fun main() {
    val list = listOf(-1F, -0.5F, 0F, 0.5F, 1F)
    val mean = -1F
    val distribution = 1F
    println(likelihood(list, mean, distribution))
}

fun likelihood(observation: List<Float>, mean: Float, standardDeviation: Float): Float {
    var likelihood = 1.0
    observation.forEach {
        likelihood *= 1 / (sqrt(2 * Math.PI) * standardDeviation) * (Math.E).pow(
            -(it - mean).pow(2).toDouble() / (2 * standardDeviation.pow(2))
        )
    }
    return likelihood.toFloat()
}