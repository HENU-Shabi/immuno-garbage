package xyz.luchengeng.immuno.bean

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader


class RHost(val port : Int,val rExec : String,val rScriptPath : String,var usageFlag : Int){
    val process : Process = ProcessBuilder().command(rExec,rScriptPath,port.toString()).start()
    init{
        val errorGobbler = StreamGobbler(process.getErrorStream())
        errorGobbler.start()
        val outputGobbler = StreamGobbler(process.getInputStream())
        outputGobbler.start()
    }
    fun terminate(){
        println("Destroying server at $port")
        process.destroyForcibly()
    }
}

class StreamGobbler(ist: InputStream) : Thread() {
    var ist: InputStream
    override fun run() {
        try {
            val isr = InputStreamReader(ist)
            val br = BufferedReader(isr)
            var line: String?
            while (br.readLine().also { line = it } != null) println(line)
        } catch (ioe: IOException) {
            ioe.printStackTrace()
        }
    }

    // reads everything from is until empty.
    init {
        this.ist= ist
    }
}