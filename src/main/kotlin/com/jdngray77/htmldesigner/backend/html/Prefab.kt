/*░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
 ░                                                                                                ░
 ░ Jordan T. Gray's                                                                               ░
 ░                                                                                                ░
 ░          HTML Designer                                                                         ░
 ░                                                                                                ░
 ░ FOSS 2022.                                                                                     ░
 ░ License decision pending.                                                                      ░
 ░                                                                                                ░
 ░ https://www.github.com/jdngray77/HTMLDesigner/                                                 ░
 ░ https://www.jordantgray.uk                                                                     ░
 ░                                                                                                ░
 ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░*/

package com.jdngray77.htmldesigner.backend.html

import com.jdngray77.htmldesigner.backend.BackgroundTask
import com.jdngray77.htmldesigner.backend.showErrorAlert
import com.jdngray77.htmldesigner.backend.showNotification
import com.jdngray77.htmldesigner.frontend.Editor
import com.jdngray77.htmldesigner.frontend.Editor.Companion.mvc
import com.jdngray77.htmldesigner.utility.*
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Evaluator
import org.jsoup.select.Evaluator.AttributeWithValueContaining
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

/**
 * A wrapper to read and write elements to disk, as a master / template.
 *
 * This creates a way to design an element, then save it for later use.
 *
 * Saved pre-fabs are not unique to any given page, and can be entered anywhere at will.
 * Bound to the prefab by uuid, any changes to the prefab will be applied to all instances of the prefab
 * found within any given page.
 *
 *
 * @constructor Saves [element] to [locationOnDisk].
 *              However, If no [element] is provided, [locationOnDisk] is used to load, instead.
 *
 * Setting new content into [element] automatically saves prefab to disk.
 */
class Prefab(

    /**
     * The file where this [element] will be saved
     */
    val locationOnDisk: File,

    /**
     * IFF creating a new prefab, then this is the element to save. When provided
     * it's assumed that you're saving a new prefab.
     *
     * When this is not provided, [locationOnDisk] will be used as target of loading
     * as opposed to saving.
     *
     * If no element to save is provided, but also [locationOnDisk] does not exist
     * a [NoSuchFileException] is thrown.
     */
    elementToSave: Element? = null

) {

    /**
     * Creates [locationOnDisk] relative to [Project.PREFABS] with [subPath]
     */
    constructor(
        subPath: String,
        element: Element? = null
    ) : this(Editor.mvc().Project.PREFABS.subFile(subPath.assertEndsWith(".html")), element)


    /**
     * The element in this prefab.
     *
     * Either provided in constructor OR loaded from disk.
     */
    lateinit var element: Element
        private set


    init {
        if (elementToSave != null) {
            // Create a new prefab.
            element = elementToSave
            writeUUIDToElement(UUID.randomUUID().toString())
            doSave()
        } else {
            // Load an existing prefab.
            if (locationOnDisk.exists())
                doLoadFromDisk()
            else
                throw NoSuchFileException(
                    locationOnDisk,
                    reason = "Prefab loading constructor was used, but there was no file to load."
                )
        }
    }

    /**
     * Raised when this [Prefab] was loaded from disk in constructor,
     * rather than created anew.
     */
    var wasLoaded = false
        private set

    /**
     * The unique identifier of this prefab, used to identify it in the DOM.
     */
    val uuid = getUUIDFromElement()

    fun getUUIDFromElement() = element.attr("prefab-uuid")

    fun writeUUIDToElement(uuid: String) =
        element.attr("prefab-uuid", uuid)

    /**
     * Provides user-friendly name of the prefab defined by the user.
     */
    val name = locationOnDisk.nameWithoutExtension

    /**
     * This replaces anything already saved to [locationOnDisk]
     * with the [newElement]
     *
     * The [newElement] will use assume the [uuid] of this prefab,
     * and any instances of the prefab in the project will be updated.
     */
    fun updateWith(newElement: Element) {
        run copyUUID@ {
            if (newElement === element) return@copyUUID

            val uuid = getUUIDFromElement()

            element = newElement

            writeUUIDToElement(uuid)
        }

        commitChange()
    }

    /**
     * After changing the [element], call this to save the changes to disk and
     * update any instances of this prefab in the project.
     *
     * This itterates through all pages in the project, and replaces any elements
     * with a prefab-uuid attribute matching this prefab's uuid.
     *
     * This is not blocking, and operates in a background thread -
     * however, only one prefab will update at a time if multiple are
     * updated at the same time. Other threads will wait thier turn.
     */
    fun commitChange() {
        doSave()

        // Background task that craws the project and updates all instances of the prefab,
        // in all documents.
        BackgroundTask.submit {
            println("hello")
            showNotification("Updating prefab: $name", "Updating all of your pages may take a few seconds.\n\nWe suggest waiting until this is complete.")
            updateAllPagesWithChange()
        }
    }

    /**
     * Performs the work of updating all pages.
     *
     * Syncronized & blocking, so that only one update operation can be performed at a time,
     * even if multiple prefab update background tasks have been submitted.
     *
     * One prefab will be updated, and any other updaters will be forced to wait here
     * due to the syncronization annotation.
     *
     * This prevents race conditions between multiple prefab-updaters.
     *
     * @param change A function that is applied to every instance of the prefab that is found.
     *               Most commonly [replaceElement], but can be used to apply any operation to
     *               all prefab instances.
     */
    private fun updateAllPagesWithChange(change: (Element) -> Unit = this@Prefab::replaceElement) {
        mvc().apply mvc@ {
            // Fetch a list of all documents.
            val pages = Project.HTML.flattenTree()

            var instanceCount = 0
            var pageCount = 0

            // Prioritise the editors that are currently open, as these
            // are most likely to be interfered with by the user whilst this
            // task is running - and it provides feedback to them faster.
            getOpenEditors().forEach applyChangeToEditor@ {
                subCommitChange(it.document, it.file, pages, change).apply {
                    instanceCount += this

                    if (instanceCount != 0)
                        pageCount++

                    it.documentChanged("Updated prefab instances: $name")
                }
            }


            // Now apply changes to all remaining documents, which are not loaded.
            pages.concmod().forEach { file ->
                Project.loadDocument(file).apply {
                    subCommitChange(this, file, pages, change).apply {
                        instanceCount += this

                        if (instanceCount != 0)
                            pageCount++
                    }
                    Project.saveDocument(this, file)
                }
            }

            if (pages.isNotEmpty())
                showErrorAlert("Failed apply changes to prefab '$name' on the pages:\n\n${pages.joinToString("\n")}")
            else
                showNotification("Prefab updated", "$instanceCount instances of the '$name' have been updated\nacross $pageCount pages.")
        }
    }

    /**
     * [updateAllPagesWithChange] sub routine that applies the change to a single document.
     *
     * @param document The document to apply the change to.
     * @param file The file that the document was loaded from.
     * @param pages The list of all pages that [updateAllPagesWithChange] is working on. [file] will be removed.
     * @param payload The change to apply to all instances of the prefab in the [document].
     */
    private fun subCommitChange(document: Document, file: File, pages: ArrayList<File>, payload: (Element) -> Unit) : Int {
        var instances = 0
        println("start document: $file")

        document.select(AttributeWithValueContaining("prefab-uuid",uuid)).forEach instances@ { prefabInstance ->
            instances++

            // If instances match, then the change will already be within the document. Skip this instance.
            if (prefabInstance === element) return@instances

            payload(prefabInstance)
        }

        pages.remove(file)

        println("end document: $file")
        return instances
    }


    private fun replaceElement(element: Element) {
        element.replaceWith(element)
    }

    /**
     * Loads [locationOnDisk] to [element], replacing [element]
     * @throws NoSuchFileException if [locationOnDisk] does not exist.
     */
    private fun doLoadFromDisk() {
        if (!locationOnDisk.exists())
            throw NoSuchFileException(locationOnDisk, reason = "Tried to load a prefab from disk, but it wasn't there.")

        wasLoaded = true
        element = locationOnDisk.readText().asElement()
    }

    /**
     * Saves [element] to [locationOnDisk]
     */
    private fun doSave() {
        element.prepareForExport()

        locationOnDisk.assertExists()
        locationOnDisk.writeText(element.outerHtml())
    }
}