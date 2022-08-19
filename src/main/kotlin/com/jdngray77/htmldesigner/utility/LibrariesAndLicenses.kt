package com.jdngray77.htmldesigner.utility

import com.jdngray77.htmldesigner.backend.showWarningNotification
import kotlinx.serialization.json.*
import org.apache.commons.io.IOUtils
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.spdx.rdfparser.SPDXLicenseInfoFactory
import java.nio.charset.StandardCharsets

/**
 * Collects all licences associated with the project
 * for display to the user.
 */
object LibrariesAndLicenses {

    /**
     * A simple page that can be displayed when
     * there is no licence to be shown.
     */
    val page404 = Document("")
        .body()
        .attr("style", "background-color: #373737; color: white;")
        .insertChildren(0,
            Element("H1").text("Nothing to display.")
                .attr("style", "text-align: center; display: block; justify-content: center; align-items: center;"),

            Element("p").text("This library / license did not provide any content or a url that we could display."),
            Element("p").text("Reffer to the library notice to see what licence this library is under.")

        ).toString()


    /**
     * Reads the json licence report from the gradle plugin.
     */
    val licenseReport = Json.parseToJsonElement(
        IOUtils.toString(
            javaClass.getResourceAsStream("license-report.json"),
            StandardCharsets.UTF_8
        )
    ) as JsonArray

    /**
     * A list of SPDX valid licence ID's.
     */
    var licenseIDs = arrayListOf<String>()
        private set

    /**
     * A list of web URLs hosting the licences that couldn't be fetched
     * via SPDX.
     */
    val licenseURLs = hashMapOf<String, String?>()

    /**
     * A list of names of the libraries used in the project.
     */
    val libraries = hashMapOf<String, String?>()

    fun idAndURLs() = licenseURLs.keys + licenseIDs

    /**
     * Either returns the HTML of the license,
     * or null.
     *
     * usefull whe using [idAndURLs] to display a list of licenses,
     * as entries may be urls or ids.
     */
    fun fetchLicenceHTML(SPDXID: String) =
        if (licenseIDs.contains(SPDXID))
            getLicense(SPDXID)
        else
            null

    /**
     * Fetches the URL provided by the report for the given license.
     *
     * @return the URL of the license, or null if none was provided by the report.
     */
    fun fetchLicenceURL(LicenseName: String) = licenseURLs[LicenseName]

    init {

        // Cache the list of valid SPDX IDs.
        val licenseIDsCache = SPDXLicenseInfoFactory.getStandardLicenseIds()

        // For every dependency
        licenseReport.forEach {
            libraries[it.jsonObject["project"]!!.jsonPrimitive.content] = it.jsonObject["url"]?.jsonPrimitive?.content


            // For every license it has
            it.jsonObject["licenses"]!!.jsonArray.forEach {
                jsonLicense ->
                // If the report specifies a SPDX ID
                jsonLicense.jsonObject["license_id"]?.let {
                    // Check that it's valid.
                    if (licenseIDsCache.contains(it.jsonPrimitive.content)) {
                        licenseIDs.add(it.jsonPrimitive.content)
                        return@forEach
                    }
                }

                // If not specify an ID, or it's invalid, use the URL.
                // We'll display it as a webpage instead.
                jsonLicense.jsonObject["license_url"]?.let {
                    licenseURLs[jsonLicense.jsonObject["license"]!!.jsonPrimitive.content] = it.jsonPrimitive.content
                    return@forEach
                }

                // If neither, warn. No useable license information.
                showWarningNotification("Could not find a licence for ${jsonLicense.jsonObject["license"]?.jsonPrimitive?.content}")
            }
        }

        // Validation and sanitation.

        // Remove duplicates.
        licenseIDs = arrayListOf(*licenseIDs.distinct().toTypedArray())
    }

    /**
     * Fetches a license from SPDX by ID
     */
    fun getLicense(id: String) = SPDXLicenseInfoFactory.getStandardLicenseById(id).licenseTextHtml

}