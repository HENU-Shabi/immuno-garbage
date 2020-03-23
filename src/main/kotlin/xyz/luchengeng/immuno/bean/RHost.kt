package xyz.luchengeng.immuno.bean

import org.apache.commons.exec.CommandLine
import org.apache.commons.exec.DefaultExecutor
import org.apache.commons.exec.Executor
import org.apache.commons.exec.ProcessDestroyer


class RHost(val port : Int,val rExec : String,val rScriptPath : String,var usageFlag : Int){
    val process : Process = ProcessBuilder().command(rExec,rScriptPath,port.toString()).start()
    protected fun finalize(){
        process.destroyForcibly()
    }
}