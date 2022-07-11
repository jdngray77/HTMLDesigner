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

import org.jsoup.safety.Safelist

/*
 *
 *      This file contains lists of tags that may be used to filter
 *      documents for safe tags, or filter lists of tags shown to the user.
 *
 */


object AllElements : Safelist() {
    init {
        addTags("a", "article", "aside", "b", "i", "blockquote", "body", "br", "button", "caption", "code", "details", "div", "em", "h1", "h2", "h3", "h4", "h5", "h6", "head", "header", "hr", "img", "label", "legend", "li", "link", "main", "mark", "nav", "p", "progress", "q", "script", "section", "span", "strong", "style", "textarea", "title", "noscript", "meter", "meta", "abbr", "address", "area", "base", "bdi", "bdo", "canvas", "cite", "col", "colgroup", "data", "datalist", "dd", "del", "dfn", "dialog", "dl", "dt", "embed", "fieldset", "figure", "font", "footer", "form", "iframe", "input", "ins", "kbd", "map", "object", "ol", "optgroup", "option", "output", "param", "picture", "pre", "rp", "rt", "ruby", "s", "samp", "select", "small", "source", "sub", "summary", "sup", "svg", "table", "tbody", "td", "template", "tfoot", "th", "thread", "time", "tr", "track", "u", "ul", "var", "video", "w", "b")
    }
}


object Headings : Safelist() {
    init {
        addTags("h1","h2","h3","h4","h5","h6")
    }
}

object Text : Safelist() {
    init {
        addTags("h1","h2","h3","h4","h5","h6","b","strong","u","em","i","p","mark","small","del","ins","sub","sup","blockquote")
    }
}