package mcfilelib.util.file_entry.assets.blockstate

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive

class BlockStateModel {
    /**
     * Specifies the path to the model file of the block, in form of namespaced ID.
     */
    val model: String
    /**
     * Rotation of the model on the x-axis in increments of 90 degrees.
     */
    val x: Int
    /**
     * Rotation of the model on the y-axis in increments of 90 degrees.
     */
    val y: Int
    /**
     * Can be true or false (default). Locks the rotation of the texture of a block, if set to true. This way the texture does not rotate with the block when using the x and y-tags above.
     */
    val uvlock: Boolean
    /**
     * Sets the probability of the model for being used in the game, defaults to 1 (=100%).
     * If more than one model is used for the same variant, the probability is calculated by dividing the individual model's weight by the sum of the weights of all models.
     * (For example, if three models are used with weights 1, 1, and 2, then their combined weight would be 4 (1+1+2). The probability of each model being used would then be determined by dividing each weight by 4: 1/4, 1/4 and 2/4, or 25%, 25% and 50%, respectively.)
     **/
    val weight: Int

    constructor(model: String, x: Int = 0, y: Int = 0, uvlock: Boolean = false, weight: Int = 1) {
        this.model  = model
        this.x      = x
        this.y      = y
        this.uvlock = uvlock
        this.weight = weight
    }

    constructor(jsonObject: JsonObject) {
        this.model  = jsonObject["model"]!!.jsonPrimitive.content
        this.x      = jsonObject["x"]?.jsonPrimitive?.int ?: 0
        this.y      = jsonObject["y"]?.jsonPrimitive?.int ?: 0
        this.uvlock = jsonObject["uvlock"]?.jsonPrimitive?.boolean ?: false
        this.weight = jsonObject["weight"]?.jsonPrimitive?.int ?: 1
    }

    /**
     * Creates a string in the following format:
     *
     * `{ "model": "relative/path/to/model", "x": 90, "y": 90, "uvlock": true, "weight": 4 }`
     */
    override fun toString(): String = toString(false)
    fun toString(showWeight: Boolean): String = buildString {
        append("{ \"model\": \"$model\"")
        if (x != 0) append(", \"x\": $x")
        if (y != 0) append(", \"y\": $y")
        if (uvlock != false) append(", \"uvlock\": true")
        if ((weight != 1) || showWeight) append(", \"weight\": $weight")
        append(" }")
    }
}