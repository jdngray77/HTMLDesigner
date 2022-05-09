package com.jdngray77.htmldesigner

fun String.AssertEndsWith(that: String) =
    if (this.endsWith(that)) this else this + that

fun Warning(string: String) {
    System.err.println(string)
}

fun Message(string: String) {
    println(string)
}