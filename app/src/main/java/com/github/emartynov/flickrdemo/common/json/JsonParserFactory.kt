package com.github.emartynov.flickrdemo.common.json

abstract class JsonParserFactory {
    abstract fun <T> createParser(type: Class<T>): JsonParser<T>

    inline fun <reified T> create() = createParser(T::class.java)
}

// TODO: write own pair class to force generic type for class and parser
internal class JsonParserFactoryImpl(vararg parsers: Pair<Class<*>, JsonParser<*>>) : JsonParserFactory() {
    private val parserMap = HashMap<Class<*>, JsonParser<*>>()

    init {
        parsers.forEach {
            parserMap[it.first] = it.second
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> createParser(type: Class<T>): JsonParser<T> = parserMap[type] as JsonParser<T>
}
