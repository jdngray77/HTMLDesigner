package com.jdngray77.htmldesigner.html.dom

import com.jdngray77.htmldesigner.Warning
import com.jdngray77.htmldesigner.html.style.Style
import com.jdngray77.htmldesigner.html.style.StyleArray
import org.jsoup.Jsoup
import org.jsoup.safety.Safelist

typealias StringArray = ArrayList<String>

/**
 * The most crucial document.
 *
 * Defines the tags available, and how
 * they are created and then converted to text.
 *
 */


/**
 * A simple list of every tag available.
 *
 * TODO can this be done through reflection?
 */
val ALLTAGS = arrayOf(
        a(),
        article(),
        aside(),
        b(),
        i(),
        blockquote(),
        body(),
        br(),
        button(),
        caption(),
        code(),
        details(),
        div(),
        em(),
        h1(),
        h2(),
        h3(),
        h4(),
        h5(),
        h6(),
        head(),
        header(),
        hr(),
        img(),
        label(),
        legend(),
        li(),
        link(),
        main(),
        mark(),
        nav(),
        p(),
        progress(),
        q(),
        script(),
        section(),
        span(),
        strong(),
        style(),
        textarea(),
        title(),
        noscript(),
        meter(),
        meta(),
        abbr(),
        address(),
        area(),
        base(),
        bdi(),
        bdo(),
        canvas(),
        cite(),
        col(),
        colgroup(),
        data(),
        datalist(),
        dd(),
        del(),
        dfn(),
        dialog(),
        dl(),
        dt(),
        embed(),
        fieldset(),
        figure(),
        font(),
        footer(),
        form(),
        iframe(),
        input(),
        ins(),
        kbd(),
        map(),
        _object(),
        ol(),
        optgroup(),
        option(),
        output(),
        param(),
        picture(),
        pre(),
        rp(),
        rt(),
        ruby(),
        s(),
        samp(),
        select(),
        small(),
        source(),
        sub(),
        summary(),
        sup(),
        svg(),
        table(),
        tbody(),
        td(),
        template(),
        tfoot(),
        th(),
        thread(),
        time(),
        tr(),
        track(),
        u(),
        ul(),
        _var(),
        video(),
        wbr()
)

// High Priority tags

class a() : Tag()
class article : Tag()
class aside : Tag()
class b : Tag()
class i : Tag()
class blockquote : Tag()
class body : Tag()
class br : Tag()
class button : Tag()
class caption : Tag()
class code : Tag()
class details : Tag()
class div : Tag()
class em : Tag()
class h1 : Tag()
class h2 : Tag()
class h3 : Tag()
class h4 : Tag()
class h5 : Tag()
class h6 : Tag()
class head : Tag()
class header : Tag()
class hr : Tag()
class img : Tag()
class label : Tag()
class legend : Tag()
class li : Tag()
class link : Tag()
class main : Tag()
class mark : Tag()
class nav : Tag()
class p : Tag()
class progress : Tag()
class q : Tag()
class script : Tag()
class section : Tag()
class span : Tag()
class strong : Tag()
class style : Tag()
class textarea : Tag()
class title : Tag()


// Low Priority tags

class noscript : Tag()
class meter : Tag()
class meta : Tag()
class abbr : Tag()
class address : Tag()
class area : Tag()
class base : Tag()
class bdi : Tag()
class bdo : Tag()
class canvas : Tag()
class cite : Tag()
class col : Tag()
class colgroup : Tag()
class data : Tag()
class datalist : Tag()
class dd : Tag()
class del : Tag()
class dfn : Tag()
class dialog : Tag()
class dl : Tag()
class dt : Tag()
class embed : Tag()
class fieldset : Tag()
class figure : Tag()
class font : Tag()
class footer : Tag()
class form : Tag()
class iframe : Tag()
class input : Tag()
class ins : Tag()
class kbd : Tag()
class map : Tag()
class _object : Tag()
class ol : Tag()
class optgroup : Tag()
class option : Tag()
class output : Tag()
class param : Tag()
class picture : Tag()
class pre : Tag()
class rp : Tag()
class rt : Tag()
class ruby : Tag()
class s : Tag()
class samp : Tag()
class select : Tag()
class small : Tag()
class source : Tag()
class sub : Tag()
class summary : Tag()
class sup : Tag()
class svg : Tag()
class table : Tag()
class tbody : Tag()
class td : Tag()
class template : Tag()
class tfoot : Tag()
class th : Tag()
class thread : Tag()
class time : Tag()
class tr : Tag()
class track : Tag()
class u : Tag()
class ul : Tag()
class _var : Tag()
class video : Tag()
class wbr : Tag()


class raw(val value: String) : Tag() {
    override fun serialize(): String = value

    override fun serializeOpen(): String = ""

    override fun serializeEnd(): String  = ""
}




//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
//                                                          The main tag.
//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░



/**
 * The generic behaviour of a tag in our DOM,
 * including the normal serialization behaviour.
 */
open class Tag : SerializableHTML {

    // TODO Tag variation configurations
    //      I.e some tags don't need a closing tag (like br)
    //      And some tags are better ended on the same line, whilst others are best ended on the next.
    //      -- OR -- Skip that shite and validate and format it using jsoup?


    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //region                                                   Properties
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░




    /**
     * Other tags that will be injected within
     * this one.
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

    /**
     * Optional content that is injected after the open tag,
     * and before any [children]
     */
    @Deprecated("Ensure this is only used for direct tag content, not child tags.")
    var content: String? = null

    /**
     * Optional javascript reference to this tag.
     */
    var ID : String? = null



    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                                Properties
    //region                                            Named Idiom CREATION API
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░


    fun ID(name : String) : Tag =
        this.apply { ID = name }




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
     * Adds css classes to this tag.
     *
     * Removes duplicates. Does not validate if the tags exist.
     */
    fun classes(vararg clazz: String) = this.apply {
        cssClasses.addAll(clazz)
        cssClasses = StringArray(cssClasses.distinct())
    }

    /**
     * Adds a css style directly into the tag.
     *
     * Removes duplicates
     */
    fun styles(vararg style: Style) = this.apply {
        styles.addAll(style)
        styles = StyleArray(styles.distinct())
    }




    //TODO missing named idiom methods

    /**
     * Adds inner raw content to this tag.
     *
     * @return this.
     */
    @Deprecated("Ensure this is only used for direct tag content, not child tags.")
    fun content(content : String) : Tag =
        this.also { this.content = content }

    /**
     * Adds inner raw content to this tag.
     *
     * @return this.
     */
    @Deprecated("Use only if you have to. Styles, ID or classes should not be here.")
    fun config(content : String) : Tag =
        this.also { config = content }




    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                         Named Idiom CREATION API
    //region                                          Named Idiom DOM Mutation API
    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░




    // TODO unit test
    /**
     * Wraps this tag with another.
     *
     * @return this.
     */
    fun wrap(with: Tag) = this.apply {
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
    fun completelyReplaceWith(that: Tag) {
        TODO()
    }

    /**
     * Removes this tag and replaces it with [that].
     *
     * This tag's properties are cleared, and [that]
     * inherits everything that this tag had.
     */
    fun changeTo(that: Tag) {
        TODO()
    }






    //░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    //endregion                                      Named Idiom DOM Mutation API
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


    fun serializeStyles() =
        if (styles.isEmpty()) "" else
            " style=\"${styles.joinToString("") { it.toString() + " " }}\""


    fun serializeClasses() = if (cssClasses.isEmpty()) "" else
            " class=\"${cssClasses.joinToString("") { "$it " }}\""

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
        val testDOM = DOM(
            html("This is my document!").children(
                body()  .ID("MainContent")
                        .classes("myStyle")
                        .styles(
                            Style("color", "blue"),
                            Style("background", "pink")
                        )
                        .children(
                            h1().content("Hello!")
                                .styles(
                                    Style("text-decoration", "underline")
                                ),
                            hr(),
                            raw("So who's responsible for this shitshow, anyway?"),
                            br(),
                            a() .ID("Link")
                                .styles(
                                    Style("color", "yellow")
                                )
                                .classes("yeet")
                                .config("href=\"https://jordantgray.uk\"")
                                .content("ME!")
                    )
            )


//                html("This is my document!").children(
//                    body().config("style=\"background: rgb(43,43,43); color: white;\"")
//                        .children(
//                            h1().content("Welcome!"),
//                            hr(),
//                            raw("So who's responsible for this shitshow, anyway?"),
//                            br(),
//                            a().config("href=\"https://jordantgray.uk\"") .content("ME!")
//
//                        )

                 as html
        )

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

/**
 * The HTML tag.
 *
 * Root to all other tags.
 *
 * Contains boilerplate for the HTML file itself.
 */
class html(val title: String) : Tag() {

    override fun serialize(): String =
        super.serialize().let {
            if (Jsoup.isValid(it, Safelist.relaxed()))
                Warning("Generated HTML is not entirely valid!")

            Jsoup.parse(it).toString()
        }

    override fun serializeOpen(): String =
"""
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    ${title().content(title)}
</head>
"""


    override fun serializeEnd(): String =
"""
</html>
"""
}
