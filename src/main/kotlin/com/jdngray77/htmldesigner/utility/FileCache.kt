package com.jdngray77.htmldesigner.utility

import java.io.File

/**
 * A cache that stores files in memory after loading them from disk.
 */
class FileCache : HashMap<File, CachedFile<*>>() {


    /**
     * Gets a file from the cache, or loads it from disk if it is not in the cache.
     * @param key The path to use for the file.
     * @param loader A function that takes a File and returns an object of type Any.
     */
    fun getOrLoad(key: String, loader: (File) -> Any) =
        getOrLoad(File(key), loader)

    /**
     * Gets a file from the cache, or loads it from the disk
     * via [loader]
     *
     * @param key The file on disk requested. Ideally, always absolute.
     * @param loader A function that loads the file from disk, if it is not cached.
     */
    fun <T> getOrLoad(key: File, loader: CachedFileLoader<T>): CachedFile<T> {

        if (!keys.contains(key))
            put(key, CachedFile(
                this,
                key,
                loader(key),
                loader
            ))

        return (super.get(key) as CachedFile<T>?)!!
    }

    fun <T> put(file: File, it: T) : CachedFile<T> {
        val existing = findT(it)

        super.put(file, CachedFile(
            this,file, it))

        return super.get(file) as CachedFile<T>
    }

    fun <T> findT(it : T) : CachedFile<T>? {
        for (cachedFile in values) {
            if (cachedFile.data == it) {
                return cachedFile as CachedFile<T>
            }
        }
        return null
    }

    fun validate() {
        entries.concmod().forEach {
            if (!it.key.exists()) {
                remove(it.key)
            }
        }
    }
}

/**
 * A file that has been loaded via a [FileCache].
 */
class CachedFile<T>(

    val cache: FileCache,

    val file: File,

    var data: T,

    var loader: CachedFileLoader<T>? = null

) {

    /**
     * Reloads the file from the disk.
     */
    fun requestReload() {
        loader?.let { data = it(file) }
    }

    fun removeCache() {
        cache.remove(file)
    }

}

typealias CachedFileLoader<T> = (File) -> T