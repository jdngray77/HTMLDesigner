package com.jdngray77.htmldesigner.backend.data

import com.jdngray77.htmldesigner.backend.html.dom.Document
import java.io.File

/**
 * A user's project.
 */
class Project(

    /**
     * The location on disk containing this project.
     *
     * This must be validated as :
     *  - A folder, not a File
     *  - Does not exist already
     *
     * Then is created automatically when this project is made.
     *
     * Predicted project folder structure :
     *
     * ```
     * myProject
     * | project.designer (This class serialized)
     * |
     * | backups
     * | | 5-6-2022 13:13:13.meta (Historical copies of the above)
     * | | 4-6-2022 13:13:13.meta
     * | | 3-6-2022 13:13:13.meta
     * |
     * | export (The latest export)
     * | | HTML
     * | | | index.html
     * | | CSS
     * | | | styles.css
     * | | JS
     * | | | index.js
     * | | MEDIA
     * | | | dog.jpg
     * ```
     */
    val locationOnDisk: File


) : java.io.Serializable {

    // TODO this file is an un-used empty shell.

    /**
     * The pages in this project.
     */
    val pages = ArrayList<Document>()




}