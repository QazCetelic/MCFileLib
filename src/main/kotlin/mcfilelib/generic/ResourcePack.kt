package mcfilelib.generic

import java.io.File
import java.nio.file.Path

open class ResourcePack: Pack {
    constructor(path: Path): super(path, true)
    constructor(file: File): super(file.toPath(), true)
}