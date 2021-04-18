import mcfilelib.generic.ResourcePack
import neatlin.toPath

//This is used for testing stuff
fun main() {
    val unityResourcePack = ResourcePack("/home/qaz/Projects/Programming/MCFileLib/src/test/kotlin/resources/resourcepacks/Unity-1.16.X-Base-2.4.0.zip".toPath())
    println(unityResourcePack.description)
    //tests("/home/qaz/Projects/Programming/MCFileLib")
}