package com.jdngray77.htmldesigner.backend.data.project

import java.io.Serializable
import java.time.Instant
import java.util.*


/*
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 *
 *
 *
 *                                         _
 *                                        (_)
 *               __      ____ _ _ __ _ __  _ _ __   __ _
 *               \ \ /\ / / _` | '__| '_ \| | '_ \ / _` |
 *                \ V  V / (_| | |  | | | | | | | | (_| |
 *                 \_/\_/ \__,_|_|  |_| |_|_|_| |_|\__, |
 *                                                  __/ |
 *                                                 |___/
 *
 *                !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 *
 *                  THIS FILE IS SENSITIVE TO CHANGES.
 *
 *                !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 *
 *                                   _ _   _
 *                ___  ___ _ __  ___(_) |_(_)_   _____
 *               / __|/ _ \ '_ \/ __| | __| \ \ / / _ \
 *               \__ \  __/ | | \__ \ | |_| |\ V /  __/
 *               |___/\___|_| |_|___/_|\__|_| \_/ \___|
 *
 *                _
 *               | |_ ___
 *               | __/ _ \
 *               | || (_) |
 *                \__\___/
 *
 *                     _
 *                 ___| |__   __ _ _ __   __ _  ___  ___
 *                / __| '_ \ / _` | '_ \ / _` |/ _ \/ __|
 *               | (__| | | | (_| | | | | (_| |  __/\__ \
 *                \___|_| |_|\__,_|_| |_|\__, |\___||___/
 *                                       |___/
 *
 *                !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 *
 *
 *      TRY TO AVOID CHANGING THE SCHEMA OF THIS CLASS IF POSSIBLE.
 *
 *      IT IMPACTS BACKWARDS COMPATABILITY WITH PROJECT LOADING.
 *
 *      CHANGES TO THE SCHEMA OF THIS CLASS WILL MAKE IT IMPOSSIBLE
 *      FOR A USER TO LOAD THEIR PROJECT WHEN THEY UPDATE.
 *
 *      What's OK :
 *
 *          Altering code within an existing function
 *
 *      What breaks compatability :
 *
 *          Literally anything that changes the schema of the class, i.e
 *
 *          - Changing a functions
 *              - return type
 *              - name
 *              - parameters
 *
 *          - Adding new or removing members from the class
 *              (functions & variables)
 *
 *
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 */



/**
 * Simple metadata container for a [Project].
 *
 * Contains some basic fields, such as the name and creation date.
 */
data class ProjectMeta (

    /**
     * The display name of the project.
     *
     * May differ from the name of the directory in which
     * the project is stored.
     */
    val name: String,

    /**
     * Name of the person or organisation
     * that created this project.
     *
     * Saves meta when altered.
     */
    var author: String? = null,

    /**
     * The date this project was created.
     *
     * The creation date of the files on disk may
     * differ with version control and whatnot.
     */
    val createdOn: Date = Date.from(Instant.now()),

    /**
     * A list of all files that are being tracked by the project.
     *
     * Used to detect files that were added, moved or deleted
     * externally.
     */
    val fileManifest: List<String> = listOf()

) : Serializable