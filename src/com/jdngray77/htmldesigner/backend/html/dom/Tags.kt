package com.jdngray77.htmldesigner.backend.html.dom

import com.jdngray77.htmldesigner.UserWarning
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.safety.Safelist

typealias StringArray = ArrayList<String>

/**
 * Outlines all the possible tags.
 *
 * Defines the tags available, and how
 * they are created and then converted to text.
 *
 * @author [Jordan T. Gray](https://www.jordantgray.uk) on 9/6/2022
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

/**
 * An object in the tree with no tag,
 * just plain raw text to inject into the model.
 */
class raw(val value: String) : Tag() {
    override fun serialize(): String = value
    override fun serializeOpen(): String = ""
    override fun serializeEnd(): String  = ""
}




//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
//                                                          The main tag.
//░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░



/**
 * The HTML tag.
 *
 * Root to all other tags.
 *
 * Contains boilerplate for the HTML file itself.
 */
class html(val title: String) : Tag() {

    /**
     * The latest [Jsoup] [Document] representation of this
     * tag model.
     */
    @Transient var builtDOMCache: Document? = null

    /**
     * Serializes the entire document.
     *
     * Overrides [serializeOpen] and [serializeEnd] to inject
     * the HMTL:5 boilerplate.
     *
     * Validates and formats the generated HTML document using Jsoup.
     *
     * Jsoup generated [Document] will be cached in [builtDOMCache]
     * at runtime, but it will not be saved to the disk if this [Tag]
     * model is saved in the project, but since the editor will likely
     * regularly re-build the [Tag] model, this shouldn't matter.
     */
    override fun serialize(): String =
        super.serialize().let {
            if (Jsoup.isValid(it, Safelist.relaxed()))
                UserWarning("Jsoup is telling me that the generated HTML is not 100% valid!")

            Jsoup.parse(it).let {
                builtDOMCache = it
                super.serialize()
            }
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
