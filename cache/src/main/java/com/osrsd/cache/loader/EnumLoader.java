package com.osrsd.cache.loader;

import com.displee.cache.index.Index;
import com.displee.cache.index.archive.Archive;
import com.osrsd.cache.Configs;
import com.osrsd.cache.Indexes;
import com.osrsd.cache.Library;
import com.osrsd.cache.def.Definition;
import com.osrsd.cache.def.EnumDefinition;
import com.osrsd.cache.util.Serializable;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EnumLoader implements Loader {

    @Override
    public Serializable load(Library library) {
        Index index = library.getCacheLibrary().index(Indexes.CONFIG);
        Archive archive = index.archive(Configs.ENUMS);

        assert archive != null;
        List<Definition> definitions = new ArrayList<>(archive.fileIds().length);
        Arrays.stream(archive.fileIds()).forEach(fileId -> {
            byte[] data = library.data(index.getId(), archive.getId(), fileId);
            if (data != null) {
                EnumDefinition definition = new EnumDefinition(fileId);
                definition.decode(ByteBuffer.wrap(data));
                definitions.add(definition);
            }
        });
        return new Serializable(this, definitions, "/enums");
    }

}
