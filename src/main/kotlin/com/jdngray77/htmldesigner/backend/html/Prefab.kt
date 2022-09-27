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

import com.jdngray77.htmldesigner.backend.*
import com.jdngray77.htmldesigner.frontend.IDE
import com.jdngray77.htmldesigner.frontend.IDE.Companion.mvc
import com.jdngray77.htmldesigner.utility.*
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Evaluator.Attribute
import org.jsoup.select.Evaluator.AttributeWithValueContaining
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.test.assertNotNull

/**
 * A wrapper to read and write elements to disk, as a master / template.
 *
 * This creates a way to design an element, then save it for later use.
 *
 * Saved pre-fabs are not unique to any given page, and can be entered anywhere at will.
 * Bound to the prefab by uuid, any changes to the prefab will be applied to all instances of the prefab
 * found within any given page.
 *
 * Terminology:
 *
 * Master - The master prefab, the one that is saved to disk in the PREFABS folder.
 * Instance - A copy of the prefab's master that is placed on a page.
 *
 *
 * @constructor Saves [masterElement] to [locationOfMaster].
 *              However, If no [element] is provided, [locationOnDisk] is used to load, instead.
 *
 * Setting new content into [masterElement] automatically saves prefab to disk.
 */
class Prefab(

    /**
     * The file where this [masterElement] will be saved
     */
    val locationOfMaster: File,

    /**
     * IFF creating a new prefab, then this is the element to save. When provided
     * it's assumed that you're saving a new prefab.
     *
     * When this is not provided, [locationOfMaster] will be used as target of loading
     * as opposed to saving.
     *
     * If no element to save is provided, but also [locationOfMaster] does not exist
     * a [NoSuchFileException] is thrown.
     */
    elementToSave: Element? = null

) {

    /**
     * Creates [locationOfMaster] relative to [Project.PREFABS] with [subPath]
     */
    constructor(
        subPath: String,
        element: Element? = null
    ) : this(IDE.mvc().Project.PREFABS.subFile(subPath.assertEndsWith(".html")), element)


    /**
     * The element in this prefab.
     *
     * Either provided in constructor OR loaded from disk.
     */
    lateinit var masterElement: Element
        private set


    init {
        if (elementToSave != null) {
            // Create a new prefab.
            masterElement = elementToSave
            writeUUIDToMaster(UUID.randomUUID().toString())
            saveMaster()
        } else {
            // Load an existing prefab.
            if (locationOfMaster.exists())
                doLoadFromDisk()
            else
                throw NoSuchFileException(
                    locationOfMaster,
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
    val uuid = getUUIDFromMaster()

    fun getUUIDFromMaster() = masterElement.attr(PREFAB_UUID_ATTR)

    /**
     * Removes the [prefab_uuid] from the master element.
     *
     * This is used when the prefab is deleted, or replaced.
     *
     * Private, as to not allow random changes to the uuid as
     * it's crutial to functionality.
     */
    private fun purgeUUIDFromMaster() = masterElement.removeAttr(PREFAB_UUID_ATTR)

    /**
     * Updates the master prefab with a given uuid.
     *
     * Private, as to not allow random changes to the uuid as
     * it's crutial to functionality.
     */
    private fun writeUUIDToMaster(uuid: String) =
        masterElement.attr(PREFAB_UUID_ATTR, uuid)

    /**
     * Provides user-friendly name of the prefab defined by the user.
     */
    val name = locationOfMaster.nameWithoutExtension

    /**
     * Replaces the prefab master with a new element, and updates
     * all instances with the changes.
     *
     * This replaces anything already saved to [locationOfMaster]
     * with the [newElement]
     *
     * The [newElement] will use assume the [prefab_uuid] of this prefab,
     * and any instances of the prefab in the project will be updated.
     */
    fun updateMasterAndInstances(newElement: Element) {
        updateMaster(newElement)
        updateInstances()
    }

    /**
     *
     */
    fun updateMaster(newElement: Element) {
        if (newElement === masterElement) return

        val uuid = getUUIDFromMaster()

        // Revoke id from current master
        purgeUUIDFromMaster()

        // Replace master with new element
        masterElement = newElement

        // Assign the id to the new master
        writeUUIDToMaster(uuid)

        saveMaster()
    }

    /**
     * After changing the [masterElement], call this to save the changes to disk and
     * update any instances of this prefab in the project.
     *
     * This itterates through all pages in the project, and replaces any elements
     * with a prefab-uuid attribute matching this prefab's uuid.
     *
     * This is not blocking, and operates in a background thread -
     * however, only one prefab will update at a time if multiple are
     * updated at the same time. Other threads will wait thier turn.
     */
    fun updateInstances() {
        // Background task that craws the project and updates all instances of the prefab,
        // in all documents.
        BackgroundTask.submit {
            showNotification("Updating prefab: $name", "Updating all of your pages may take a few seconds.\n\nWe suggest waiting until this is complete.")
            updateAllInstances()
        }
    }

    /**
     * Performs the work of updating all pages.
     *
     * Applies a function to all instances of the master on all pages,
     * typically [updPayload_ReplaceInstaceWithMaster]
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
    @Synchronized
    private fun updateAllInstances(change: (Element) -> Unit = this@Prefab::updPayload_ReplaceInstaceWithMaster) {
        mvc().apply mvc@ {
            // Fetch a list of all documents.
            val pages = Project.HTML.flattenTree()

            var instanceCount = 0
            var pageCount = 0

            // Prioritise the editors that are currently open, as these
            // are most likely to be interfered with by the user whilst this
            // task is running - and it provides feedback to them faster.
            getOpenEditors().forEach applyChangeToEditor@ {
                updateInstancesOnDocument(it.document, it.file, pages, change).apply {
                    instanceCount += this

                    if (instanceCount != 0)
                        pageCount++

                    it.documentChanged("Updated prefab instances: $name")
                }
            }


            // Now apply changes to all remaining documents, which are not loaded.
            pages.concmod().forEach { file ->
                Project.loadDocument(file).apply {
                    updateInstancesOnDocument(this, file, pages, change).apply {
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
     * Removes the [prefab_uuid] attribute from every instance of this prefab.
     */
    fun unlinkAllInstances() {
        updateAllInstances(this::updPayload_UnlinkInstance)
    }

    /**
     * Deletes all instances of this prefab from the project.
     */
    fun deleteAllInstances() {
        updateAllInstances(this::updPayload_DeleteInstance)
    }

    /**
     * @return [getAllInstancesOf] this prefab.
     */
    fun instances() =
        getAllInstancesOf(this)

    /**
     * Creates a new instance of the element for use in the DOM.
     *
     * i.e body().appendChild(prefab.newInstance())
     */
    fun newInstance() =
        masterElement.clone()

    /**
     * [updateAllInstances] sub routine that applies the change to a single document.
     *
     * @param document The document to apply the change to.
     * @param file The file that the document was loaded from.
     * @param pages The list of all pages that [updateAllInstances] is working on. [file] will be removed.
     * @param payload The change to apply to all instances of the prefab in the [document].
     */
    private fun updateInstancesOnDocument(document: Document, file: File, pages: ArrayList<File>, payload: (Element) -> Unit) : Int {
        var instances = 0
        document.select(AttributeWithValueContaining("prefab-uuid",uuid)).forEach instances@ { prefabInstance ->
            instances++

            // If instances match, then the change will already be within the document. Skip this instance.
            if (prefabInstance === masterElement) return@instances

            payload(prefabInstance)
        }

        pages.remove(file)
        return instances
    }


    /**
     * Most common payload for [updateInstancesOnDocument].
     *
     * Replaces instance of this prefab entirely with a copy of the
     * master.
     */
    private fun updPayload_ReplaceInstaceWithMaster(element: Element) {

        if (element == null) {
            logWarning("Attempting to replace null element with prefab.")
            return
        }

        if(element.parent() == null) {
            logWarning("JSoup issue 1843. Element has no parent.")
            return
        }

        // Jsoup issue 1843, pr 1844
        // Prevents a bug with jsoup where replacing an element with itself fucks everything
        // and makes the document unusable.
        // I've reported and pr'd a fix, but it's not approved yet,
        // so this is here in the meantime.
        if (element === this.masterElement)
            return

        element.purgePrefabUUID()

        element.replaceWith(this.masterElement.clone())
    }

    private fun updPayload_UnlinkInstance(element: Element) {
        element.purgePrefabUUID()
    }

    private fun updPayload_DeleteInstance(element: Element) {
        element.remove()
    }

    /**
     * Loads [locationOfMaster] to [masterElement], replacing [masterElement]
     * @throws NoSuchFileException if [locationOfMaster] does not exist.
     */
    private fun doLoadFromDisk() {
        if (!locationOfMaster.exists())
            throw NoSuchFileException(locationOfMaster, reason = "Tried to load a prefab from disk, but it wasn't there.")

        wasLoaded = true
        masterElement = locationOfMaster.readText().asElement()
    }

    /**
     * Saves [masterElement] to [locationOfMaster]
     */
    private fun saveMaster() {
        masterElement.prepareForExport()

        locationOfMaster.assertExists()
        locationOfMaster.writeText(masterElement.outerHtml())
    }

    companion object {

        /**
         * The attribute on an element that stores the uuid of the master.
         */
        const val PREFAB_UUID_ATTR = "prefab-uuid"

        /**
         * @returns true if the [element] is an instance of [master].
         *          false if not instance of master, or is not a prefab.
         */
        fun isInstanceOf(master: Element, element: Element) : Boolean =
            master.prefab_uuid() == element.prefab_uuid()

        /**
         * @returns true if the [element] is an instance of [master].
         *          false if not instance of master, or is not a prefab.
         */
        fun isInstanceOf(master: Prefab, element: Element) =
            isInstanceOf(master.masterElement, element)

        /**
         * @returns true if the [element] is an instance of [master].
         *          false if not instance of master, or is not a prefab.
         */
        fun Element.isInstanceOf(master: Prefab) =
            isInstanceOf(master, this)

        /**
         * @returns true if the [element] is an instance of [master].
         *          false if not instance of master, or is not a prefab.
         */
        @JvmName("elementInstanceOf")
        fun Element.isInstanceOf(master: Element) =
            isInstanceOf(master, this)

        /**
         * Returns the uuid of this element, or null if it is not a prefab.
         *
         * @see isPrefabInstance to check if uuid is present.
         */
        fun Element.prefab_uuid() : String =
            attr(PREFAB_UUID_ATTR)

        /**
         * Removes the uuid from this element, if it is a prefab.
         */
        fun Element.purgePrefabUUID() {
            removeAttr(PREFAB_UUID_ATTR)
        }

        /**
         * @return true if this element is an instance of a prefab.
         */
        fun Element.isPrefabInstance() : Boolean =
            this.hasAttr(PREFAB_UUID_ATTR)

        /**
         * If this element is an instance of a prefab, then this will return the
         * master prefab that it's an instance of.
         *
         * @return null if not a prefab, or if no matching master can be found.
         */
        fun Element.getPrefabMaster() : Prefab? {
            if (!isPrefabInstance())
                return null

            return findMasterByUUID(prefab_uuid())
        }

        /**
         * Finds and returns a master prefab by it's uuid.
         *
         * @return matching prefab, or null if no matching prefab can be found.
         */
        fun findMasterByUUID(uuid: String) : Prefab? =
            getAllPrefabs().find { it.uuid == uuid }

        /**
         * Finds and returns a master prefab by it's name.
         *
         * @return matching prefab, or null if no matching prefab can be found.
         */
        fun findMasterByName(name: String) : Prefab? =
            getAllPrefabs().find { it.uuid == name }

        /**
         * Updates a master (and optionally all instances of the master) from an instance.
         *
         *  When editing a copy of a prefab within a page, this can be used to
         * apply the changes made on the instance to the prefab master itself.
         *
         * @param updateAllInstances If true, then all instances of the prefab will be updated.
         *                           If false or unprovided, then only the master will be updated.
         *
         * @return The prefab that this is an instance of, and which was updated.
         */
        fun Element.updatePrefabFromInstance(updateAllInstances : Boolean = false) : Prefab {
            val uuid = prefab_uuid()

            assertNotNull(uuid)
            assert(uuid.isNotBlank())

            if (uuid == null || uuid.isBlank())
                throw IllegalArgumentException("Element provided is not a prefab instance, or is missing the $PREFAB_UUID_ATTR attribute.")

            val master = findMasterByUUID(uuid)
                ?: throw NoSuchElementException("Could not find a prefab master with the uuid '$uuid'.")

            master.updateMaster(this)

            if (updateAllInstances)
                master.updateAllInstances()

            return master
        }


        /**
         * Quick convenience method to create a new prefab from an element.
         *
         * @return the new prefab that was created.
         */
        fun Element.createPrefab(): Prefab {

            var ret: Prefab? = null

            userInput("Enter a name / path for this prefab") {
                name ->
                if (findMasterByName(name) != null)
                    return@userInput "A prefab called '$name' that name already exists."


                // Create it.
                ret = Prefab(name, this)
                EventNotifier.notifyEvent(EventType.PROJECT_PREFAB_CREATED)

                return@userInput null
            }

            return ret!!
        }

        fun validatePrefabs() {
            BackgroundTask.submit {
                showNotification("Checking prefabs", "Checking your documents and prefabs are all OK.")

                val allDocs = mvc().Project.HTML.flattenTree().map { mvc().Project.loadDocument(it) }

                val report = arrayListOf<String>()

                allDocs.forEach document@ { doc  ->
                    doc.body().select(Attribute(PREFAB_UUID_ATTR)).forEach instancesInDoc@ {
                        val uuid = it.prefab_uuid()

                        val master = findMasterByUUID(uuid) ?: run onNoMaster@ {
                            report.add("[Warning] '${doc.title()}' has an element linked to prefab with uuid '$uuid', but no prefab with that ID was found, so the link was removed.")
                            it.removeAttr(PREFAB_UUID_ATTR)
                            return@instancesInDoc
                        }

                        if (master.toString() != it.toString())
                            report.add("${it.tagName()} in '${doc.title()}' is out of sync with linked prefab ${master.name}. No action taken.")
                    }
                }

                val allfabs = getAllPrefabs()

                allfabs.forEach {
                    prefab->
                    if (prefab.instances().isEmpty())
                        report.add("Prefab '${prefab.name}' is not being used in any documents. No action taken.")

                    val copies = allfabs.filter { it.uuid == prefab.uuid }
                    if (copies.size > 1) {
                        report.add("[Serious] Prefab ${copies.size} prefabs share the same unique ID. This should never happen, as the editor cannot tell them apart." +
                                "\n(${copies.joinToString() { it.name } })")
                        logWarning(report.last())
                    }

                    if (prefab.masterElement.id().isNotEmpty())
                        report.add("[Warning] '${prefab.name}' has an ID. This will cause issues if the prefab is used multiple times in a document.")
                }

                showNotification("Finished checking prefabs.", "There is ${report.size} messages.\n\nClick to view report.") {
                    showListOfStrings("Prefab validation report", report)
                }

            }
        }


        /**
         * Returns a list of all prefabs in the project.
         */
        fun getAllPrefabs() =
            getAllPrefabFiles().map { Prefab(it) }

        /**
         * Returns all the on-disk HTML files containing the prefab masters.
         */
        fun getAllPrefabFiles() =
            mvc().Project.PREFABS.flattenTree()


        /**
         * Searches all pages for instances of the [master] prefab.
         *
         * @return a list of all instances of the [master] prefab. May be empty if there are no instances.
         */
        fun getAllInstancesOf(prefab: Prefab) : List<Element> {
            val instances = ArrayList<Element>()

            // TODO make this a project function to get all documents.
            mvc().Project.HTML.flattenTree().map { mvc().Project.loadDocument(it) }.forEach { document ->
                instances.addAll(0, document.select(AttributeWithValueContaining(PREFAB_UUID_ATTR, prefab.uuid)))
            }

            return instances
        }
    }
}