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

import org.jsoup.nodes.Element

/**
 * When creating a new page in the [project],
 * document used to initalise the page.
 *
 * A simple gray page with some text/
 */
class DefaultDocument : org.jsoup.nodes.Document(""){
    init {
        title("Dummy HTML")

        body()
            .id("MainContent")
            .addClass("myStyle")
            .attr("style", "background: #373737; color: lightgray;")
            .insertChildren(
                0,
                Element("h1")
                    .id("PageTitle")
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
                    .appendText("ME!")
            )
    }
}