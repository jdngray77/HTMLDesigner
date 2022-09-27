package com.jdngray77.htmldesigner.backend.html

import com.fasterxml.jackson.databind.deser.std.UUIDDeserializer
import com.jdngray77.htmldesigner.RequiresEditorGUI
import com.jdngray77.htmldesigner.backend.html.Prefab.Companion.isPrefabInstance
import com.jdngray77.htmldesigner.backend.html.Prefab.Companion.prefab_uuid
import com.jdngray77.htmldesigner.frontend.Editor.Companion.mvc
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*


@ExtendWith(RequiresEditorGUI::class)
internal class PrefabTest {

    lateinit var prefab: Prefab

    lateinit var testDocument1: Document
    lateinit var testDocument2: Document

    @BeforeEach
    internal fun setUp() {

        prefab = Prefab(
            "_PREFAB_TEST",
            Element("div")
                .id("test")
                .text("This is a prefabricated div")
                .attr("style", "background-color: red;")
                .attr("class", "test-class"),
        )

        assert(prefab.locationOfMaster.exists())

        testDocument1 = mvc().Project.createDocument("_PREFAB_TEST1").also {
            it.body().appendChild(
                prefab.newInstance()
            )
        }

        testDocument2 = mvc().Project.createDocument("_PREFAB_TEST2").also {
            it.body().appendChild(
                prefab.newInstance()
            )
        }
    }

    @AfterEach
    internal fun tearDown() {
        prefab.locationOfMaster.delete()
        assert(!prefab.locationOfMaster.exists())

        mvc().Project.fileForDocument(testDocument1).delete()
        mvc().Project.fileForDocument(testDocument2).delete()
    }


    @Test
    fun instances() {
        val set = {
            prefab.deleteAllInstances()
            assertEquals(0, prefab.instances().size, "There should be no instances of the prefab after deleting them all.")

            testDocument1.body().appendChild(
                prefab.newInstance()
            )

            assertEquals(1, prefab.instances().size, "There should be two instances of the prefab")

            testDocument2.body().appendChild(
                prefab.newInstance()
            )

            assertEquals(2, prefab.instances().size, "There should be three instances of the prefab")
        }


        set()
        prefab.unlinkAllInstances()
        assertEquals(0, prefab.instances().size, "There should be no instances of the prefab after unlinking them all.")

        set()
        prefab.deleteAllInstances()
        assertEquals(0, prefab.instances().size, "There should be no instances of the prefab after deleting them all.")
    }


    @Test
    fun name() {
        assertNotNull(prefab.name)
        assertEquals("_PREFAB_TEST", prefab.name)
    }

    @Test
    fun replaceSavedElement() {
        val newElement = Element("div")
            .id("test")
            .text("This is a prefabricated div")
            .attr("style", "background-color: blue;")
            .attr("class", "test-class")

        val oldElement = prefab.masterElement

        val uuid = oldElement.prefab_uuid()

        val instances = prefab.instances()

        // Update the master without updating the instances.
        prefab.updateMaster(newElement)

        // None of the instances should have changed.
        instances.all { it.hasParent() }

        // Old element should have been revoked.
        assert(!oldElement.isPrefabInstance())

        // New element should have the id.
        assert(newElement.isPrefabInstance())
        assertEquals(uuid, newElement.prefab_uuid())

        // Instances should all be the same, and not be updated.
        assert(instances.containsAll(prefab.instances()))
        assert(instances.all { it.isPrefabInstance() })
        assert(instances.all { it.hasParent() })



        prefab.updateMasterAndInstances(newElement)

        assertEquals(instances.size, prefab.instances().size)

        // Old instances should have been revoked
        assert(instances.none { it.isPrefabInstance() })
        assert(instances.none { it.hasParent() })
        assert(prefab.instances().none { instances.contains(it) })

        assert(prefab.instances().all { it.isPrefabInstance() })

        assertEquals(instances.size, prefab.instances().size)

        assertEquals(newElement, prefab.masterElement)
    }

    @Test
    fun getLocationOnDisk() {
    }

    @Test
    fun getMasterElement() {
    }

    @Test
    fun getWasLoaded() {
    }

    @Test
    fun getUuid() {
    }

    @Test
    fun getUUIDFromMaster() {
    }

    @Test
    fun getName() {
    }

    @Test
    fun updateMasterAndInstances() {
    }

    @Test
    fun updateMaster() {
    }

    @Test
    fun updateInstances() {
    }

    @Test
    fun unlinkAllInstances() {
    }

    @Test
    fun deleteAllInstances() {
    }



    @Test
    fun getLocationOfMaster() {
    }


}