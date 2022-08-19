package com.jdngray77.htmldesigner.utility

import kotlinx.serialization.json.JsonArray
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class LibrariesAndLicensesTest {

    @Test
    fun getPage404() {
        assert(LibrariesAndLicenses.page404 is String)
    }

    @Test
    fun getLicenseReport() {
        assertNotNull(javaClass.getResourceAsStream("license-report.json")) { "License report is missing or can't be loaded. Generate one from gradle." }

        assertDoesNotThrow { LibrariesAndLicenses.licenseReport }
        assert(LibrariesAndLicenses.licenseReport is JsonArray)
        assert(LibrariesAndLicenses.licenseReport.isNotEmpty()) { "License report contains no entries." }
    }

    @Test
    fun collectionsPopulated() {
        assert(LibrariesAndLicenses.licenseIDs.isNotEmpty())
        assert(LibrariesAndLicenses.licenseURLs.isNotEmpty())
        assert(LibrariesAndLicenses.libraries.isNotEmpty())
    }

    @Test
    fun idAndURLs() {
        with(LibrariesAndLicenses) {
            assertEquals(licenseURLs.size + licenseIDs.size, idAndURLs().size) { "Id and url collections differ in size." }
        }
    }

    @Test
    fun fetchLicenceHTML() {
        with(LibrariesAndLicenses) {
            licenseIDs.forEach {
                assertNotNull(fetchLicenceHTML(it)) { " Unable to retrieve HTML for a spdx id " }
            }
        }
    }
}