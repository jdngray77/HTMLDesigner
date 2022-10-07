package com.jdngray77.htmldesigner.backend

object PlatformType {

    enum class PlatformTypes {
        MAC,
        WINDOWS,
        LINUX
    }

    /**
     *
     */
    fun getPlatformType() : PlatformTypes {

        with(System.getProperty("os.name").lowercase()) {
            return when {
                contains("mac") -> PlatformTypes.MAC
                contains("windows") -> PlatformTypes.WINDOWS
                contains("linux") -> PlatformTypes.LINUX

                else -> PlatformTypes.WINDOWS.also { showWarningNotification("OS", "") }
            }
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println(getPlatformType())
    }
}