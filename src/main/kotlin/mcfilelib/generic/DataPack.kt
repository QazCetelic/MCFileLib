package mcfilelib.generic

import java.io.File
import java.nio.file.Path

class DataPack: Pack {
    constructor(path: Path): super(path, false)
    constructor(file: File): super(file.toPath(), false)
}