package mcfilelib.json

import com.google.gson.JsonElement
import neatlin.*
import mcfilelib.util.ifKey

/**
 * Processes authors in the fabric format
 */
internal fun MutableList<String>.addAuthorsFromArray(unprocessedAuthors: JsonElement) {
    this.addAll(
        fillList {
            for (author in unprocessedAuthors.asJsonArray.toList()) {
                when {
                    author.isJsonPrimitive -> add(author.asString)
                    author.isJsonObject -> {
                        author.asJsonObject.ifKey("name") { name ->
                            //Prevents lists to sneak into the author name field, because this should somewhere else (looking at you, ArloTheEpic!)
                            if ("\n" !in name.asString) add(name.asString)
                        }
                        //todo get more data from author object
                    }
                }
            }
        }
    )
}

/**
 * Processes authors in the newer forge format using TOML
 */
internal fun MutableList<String>.addAuthorsFromString(unprocessedAuthors: JsonElement) {
    // Splits up strings "Vazkii, BluSunrize and Damien A.W. Hazard" into [Vazkii, BluSunrize, Damien A.W. Hazard]
    this.addAll(
        fillList {
            for (author in unprocessedAuthors.asString.trim().split(" and ", ", ")) {
                add(author)
            }
        }
    )
}