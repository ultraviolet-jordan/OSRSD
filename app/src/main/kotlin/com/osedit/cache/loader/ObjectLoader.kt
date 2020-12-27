package com.osedit.cache.loader

import com.displee.cache.index.archive.Archive
import com.osedit.cache.Configs
import com.osedit.cache.Indexes
import com.osedit.cache.Library
import com.osedit.cache.provider.ObjectProvider
import com.osedit.cache.util.Serializable
import com.osedit.spring.domain.ObjectDefinition
import java.nio.ByteBuffer

class ObjectLoader : Loader {

    override fun load(): Serializable {
        val archive: Archive = Library.index(Indexes.CONFIG).archive(Configs.OBJECTS)!!
        val definitions = archive.fileIds().map {
            ObjectProvider.decode(ByteBuffer.wrap(archive.file(it)?.data), ObjectDefinition(it))
        }
        return Serializable(this, definitions, "/objects")
    }

}