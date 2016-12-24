package com.gradle.fury.example

import java.util.*

/**
 * Application that returns passed arguments
 */
object Main {

    /**
     * Print passed arguments formatted as an array.
     *
     * @param args passed command line arguments
     */
    @JvmStatic
    fun main(args: Array<String>) {
        println(Arrays.toString(args))
    }

}