package mcfilelib.generic

import java.io.File
import java.nio.file.Path

open class ResourcePack(path: Path) : Pack(path, true) {
    companion object {
        operator fun invoke(file: File) = ResourcePack(file.toPath())
    }
}