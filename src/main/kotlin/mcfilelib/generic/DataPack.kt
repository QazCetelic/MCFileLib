package mcfilelib.generic

import java.io.File
import java.nio.file.Path

class DataPack(path: Path) : Pack(path, false) {
    companion object {
        operator fun invoke(file: File) = DataPack(file.toPath())
    }
}