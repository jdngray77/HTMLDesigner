package com.jdngray77.htmldesigner.backend.html.dom

import com.jdngray77.htmldesigner.RemoveDuplicates
import com.jdngray77.htmldesigner.backend.html.style.Style
import com.jdngray77.htmldesigner.backend.html.style.StyleArray
import org.jsoup.nodes.Element


/**
 * The generic behaviour of a tag in our Tag Model.
 *
 * Represents one single HTML tag, with links
 * to one parent and many children.
 *
 * Handles serialization of tag to text for most tags,
 * not including edgecases.
 *
 * @author [Jordan T. Gray](https://www.jordantgray.uk) on 9/6/2022
 */
abstract class Tag : SerializableHTML {

    // TODO Tag variation configurations
    //      I.e some tags don't need a closing tag (like br)
    //      And some tags are better ended on the same line, whilst others are best ended on the next.
    //      -- OR -- Skip that shite and validate and format it using jsoup?


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //region                                                   Properties
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░


    /**
     * Tags that are under this tag in the tree.
     *
     * will be injected within this one when serialized.
     */
    val children = ArrayList<Tag>()

    /**
     * CSS classes applied to this tag.
     *
     * i.e `<example class="bright"></example>`
     */
    var cssClasses = StringArray()

    /**
     * Unique CSS styles applied to this element only.
     *
     * i.e `<example style="background: blue;"></example>`
     */
    var styles = StyleArray()

    /**
     * The [Tag] that this tag is a child of.
     */
    var parent: Tag? = null

    /**
     * Optional custom content to inject into the opening tag.
     *
     * Classes, ID and styles are handled, but there may be reason to add
     * other things into the opening tag.
     */
    @Deprecated("Use only if you have to. Styles, ID or classes should not be here.")
    var config: String? = null
        get() {
            if (field == null)
                field = ""

            return field
        }

    /**
     * Optional content that is injected after the open tag,
     * and before any [children]
     */
    @Deprecated("Use child tags, or [raw]")
    var content: String? = null

    /**
     * Optional javascript reference to this tag.
     */
    var ID : String? = null



    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                                Properties
    //region                                            Named Idiom CREATION API
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░


    // Tag attributes

    /**
     * Sets the ID of this tag.
     *
     * i.e.
     *
     * < tag ID="[ID]" >
     */
    fun ID(name : String) : Tag =
        this.apply { ID = name }


    /**
     * Adds css classes to this tag.
     * i.e.
     *
     * < tag class="[classes]" >
     *
     * Removes duplicates. Does not validate if the tags exist.
     */
    fun classes(vararg clazz: String) = this.apply {
        cssClasses.addAll(clazz)
        cssClasses.RemoveDuplicates()
    }

    /**
     * Adds a css style directly into the tag.
     * i.e.
     *
     * < tag style="[styles]" >
     *
     * Removes duplicates
     */
    fun styles(vararg style: Style) = this.apply {
        styles.addAll(style)
        styles.RemoveDuplicates()
    }

    /**
     * Adds raw content into the opening tag.
     *
     * @return this.
     */
    @Deprecated("Use only if you have to. Styles, ID or classes should not be here. Special elements /should/ be handled in the corresponding tag class.")
    fun config(content : String) : Tag =
        this.also { config = content }

    fun addConfig(content: String) : Tag =
        this.also { config += " $content" }






    // Children

    /**
     * Adds children to this tag.
     *
     * Also set the parent of [it] to this.
     *
     * @return this.
     */
    fun children(vararg it : Tag) : Tag =
        this.apply { it.map{ children.add(it) ; it.parent = this} }

    /**
     * Adds one or many children to this tag.
     *
     * Alias for `children()`
     *
     * @return this.
     */
    fun addChild(vararg it : Tag) : Tag = children(*it)

    //TODO unit test
    /**
     * Removes a child from this element,
     * and clear's it's parent only if it was actually
     * a child of this.
     *
     * If not a child of this, has no effect.
     *
     * @return this.
     */
    fun removeChild(that: Tag) : Tag {
        if (children.remove(that))
            that.parent = null

        return this
    }

    /**
     * Adds inner raw content to this tag.
     *
     * @return this.
     */
    @Deprecated("Use child tags, or [raw]")
    fun content(content : String) : Tag =
        this.also { this.content = content }


    /**
     * Populates this tag from HTML.
     */
    fun fromHTML() {
        TODO()
    }






    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                         Named Idiom CREATION API
    //region                                          Named Idiom DOM Mutation API
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░








    /**
     * Wraps this tag with another.
     *
     * i.e `a().wrap(div())` mutates :
     *
     * ```
     * <a ...>
     *     ...
     * </a>
     * ```
     *
     * into this :
     *
     * ```
     * <div>
     *  <a ...>
     *      ...
     *  </a>
     * </div>
     * ```
     *
     * @return this.
     */
    fun wrap(with: Tag) = this.apply {
        // TODO unit test
        // TODO make sure these operationes are swapped in place. Some remove
        //      then add it in a different place.
        parent?.children(with)
        parent?.removeChild(this)
    }


    /**
     * COMPLETELY REMOVES THIS TAG AND ALL INNER
     * CONTENT FROM THE DOM!
     *
     * Remove this tag, and replace it with [that].
     *
     * @return [that] . NB - RETURNS THE ***NEW*** TAG
     */
    // TODO unit test this. Make sure it swaps in place.
    fun completelyReplaceWith(that: Tag) = that.apply {
        parent?.children?.indexOf(this)?.also {
            parent?.children?.set(it, that)
        }

        that.parent = parent
    }

    /**
     * Removes this tag and replaces it with [that].
     *
     * This tag's properties are cleared, and [that]
     * inherits everything that this tag had.
     */
    //TODO unit test this
    fun changeTo(that: Tag) {
        completelyReplaceWith(that)
            .children(*children.toArray() as Array<out Tag>)

        that.content = content
        that.ID = ID
        that.cssClasses = cssClasses
        that.styles = styles
    }

    /**
     * Removes this from the tag model, then returns it.
     *
     * Can be used to remove this from the model, or
     * let you move it elsewhere.
     *
     * ---
     *
     * Transfers all [children] to [parent],
     * then breaks link between this and it's parent,
     * effectively removing it from the model entirely.
     *
     * i.e take this structure :
     *

     *
     *
     *```
     *      +----+
     *      |TagA|
     *      +--X-+
     *        / \
     *       /   \
     *  +---/+   +\---+
     *  |TagB|   |TagC|
     *  +---\+   +----+
     *       \
     *      +-\--+
     *      |TagD|
     *      +----+
     *```
     *
     * and apply `tagC.children(tagB.re_move())`.
     *
     * You'll get this :
     *
     *```
     *
     *
     *      +----+
     *      |TagA|
     *      +--X-+
     *        / \
     *       /   \
     *   +----+  +\---+
     *   |TagD|  |TagC|
     *   +----+  +-/--+
     *            /
     *      +----/
     *      |TagB|
     *      +----+
     *```
     *
     * The children of `B` (which was D) was moved to the parent of `B` (which was A).
     * `B` was then added to `C` as a child.
     */
    // TODO Unit test this
    fun re_move() = this.apply {
        parent?.children(*children.toArray() as Array<out Tag>)
        parent?.removeChild(this)
    }





    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                      Named Idiom DOM Mutation API
    //region                                       Named Idiom DOM Non Mutative API
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

    fun findTagsByName() {
        TODO()
    }

    fun findTagByID() {
        TODO()
    }

    fun <T : Tag> findTagsByType() {
        TODO()
    }


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                    Named Idiom DOM Non Mutative API
    //region                                                SERIALIZATION
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░




    /**
     * Converts this tag to text, in the format :
     *
     * <[name] ID="[ID]" [config] class="[cssClasses]" style="[styles]">
     *
     * [content]
     *
     * [children]
     *
     * </[name]>
     *
     */
    override fun serialize() : String
            = "${serializeOpen()}${notNullString(content)}${serializeChildren()}${serializeEnd()}"

    /**
     * Generates the opening tag, formatted as such :
     *
     * <[name] ID="[ID]" [config] class="[cssClasses]" style="[styles]">
     *
     * Note that the optional properties only exist in the tag if they
     * have been populated.
     */
    open fun serializeOpen() : String =
        "\n<${name()}${serializeID()}${notNullString(config)}${serializeClasses()}${serializeStyles()}>"

    /**
     * Generates the closing tag
     *
     * </[name]>
     */
    open fun serializeEnd() : String = "</${name()}>"

    /**
     * Returns a string representation of all children after being [serialize]d.
     */
    fun serializeChildren() : String =
        children.joinToString("") { it.serialize() }

    /**
     * Returns a string representation of the [styles]
     * unique to this tag, if there is any.
     */
    fun serializeStyles() =
        if (styles.isEmpty()) "" else
            " style=\"${styles.joinToString("") { it.toString() + " " }}\""

    /**
     * Returns a string representation of the CSS Classes
     * unique to this tag, if there is any.
     */
    fun serializeClasses() = if (cssClasses.isEmpty()) "" else
        " class=\"${cssClasses.joinToString("") { "$it " }}\""

    /**
     * Returns a string representation of the [ID]
     * of this tag, if there is one.
     */
    fun serializeID() = if (ID.isNullOrBlank()) "" else " ID=\"$ID\""

    /**
     * Shorthand for [serialize]. When a tag is used in a string, it'll be serialized.
     */
    override fun toString(): String = serialize()




    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                             SERIALIZATION
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░


    /**
     * Fetches the name of this tag
     *
     * (Tags name is the name of the class.)
     */
    fun name() = this::class.simpleName


    companion object {

        /**
         * An example piece of data.
         */
        val testDOM = org.jsoup.nodes.Document("").also {
            it.title("Dummy HTML")
            it.body()
                .id("MainContent")
                .addClass("myStyle")
                .attr("style", "background: pink; color: blue;")
                .insertChildren(
                    0,
                    Element("h1")
                        .appendText("Hello!")
                        .attr("style", "text-decoration: underline;"),
                    Element("hr"),
                    Element("p")
                        .appendText("So who's responsible for this shitshow, anyway?"),
                    Element("br"),
                    Element("a")
                        .id("Link")
                        .addClass("yeet")
                        .attr("href", "https://jordantgray.uk")
                        .attr("style", "color: yellow;")
                        .appendText("ME!"),
                )
        }

        /**
         * Prints some objects to the console.
         */
        @JvmStatic
        fun test () {
//            println(div())
//
//            println(head())
//
//            println(body())

            println(
                testDOM
            )
        }

        /**
         * Returns the toString of the object, with a space before it.
         *
         * However, if the object is null the string is empty.
         */
        fun notNullString(it : Any?) : String = if (it == null) "" else " $it"
    }
}
