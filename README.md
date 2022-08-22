> "It's cool how it works, it just doesn't work right now" - [Tom](https://github.com/birdie2016)

# HTML Designer

A startup HTML website / application GUI creator.

Stand-by whilst we get the project started.

![Screen Shot 2022-07-11 at 23 12 12](https://user-images.githubusercontent.com/50697488/178367528-513878f9-b095-4f13-bad6-0a4d971b3d40.png)

Current features : 
 - Project creation and management
 - DOM manipulation
 - Isolate tags to edit them independantly
 - Prefabricated tags. Save tags to the disk, and re-use them,

Projected features : 
 - Easily curate front-end GUI's.
 - Create and use pre-fabricated elements.
 - Debug with webtools
 - Local & git VCS
 - No need to know or edit HTML or CSS.


# Getting Started Guide
> Incomplete, will be maintained as the programme is developed.


## Creating a project
Upon boot, you'll be prompted to load or create a project.

![Screen Shot 2022-07-22 at 17 05 40](https://user-images.githubusercontent.com/50697488/180479366-7ecbdf2f-2d9f-4590-8eb4-65b9dbff9e76.png)

Once a project has been selected, it will be remembered. The next time you boot, it will load the project opened previously.

If you wish, you can use `Menu > Project > Close Project` to return to the dialog.

Or, this auto-load behaviour can be disabled entirely with the registry key `AUTO_LOAD_PROJECT_BOOL`.

See [the documentation](https://github.com/jdngray77/HTMLDesigner/wiki/Registries) to see about to edit registries.

## Creating and opening a document
Upon project creation, a template `index.html` is created and opened.

Using the Project dock on the lower left, pages and directories can be create from the context menu, or opened with a single click.

![Screen Shot 2022-07-22 at 18 29 28](https://user-images.githubusercontent.com/50697488/180492961-17138489-dccc-431f-978f-3716ff146173.png)


## Editing a document
A [`DocumentEditor`](https://github.com/jdngray77/HTMLDesigner/blob/master/src/com/jdngray77/htmldesigner/frontend/DocumentEditor.kt) will open in the center of the IDE's display, rendering the document it contains.

Multiple document editors can be swapped between by switching tabs. The rest of the IDE will automatically update to reflect the Document Editor that is currently selected and displaying.

(Coming soon) ~~See the wiki for more about editing documents~~
