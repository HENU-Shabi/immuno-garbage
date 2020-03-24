package xyz.luchengeng.immuno

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component
import xyz.luchengeng.immuno.bean.PoolProperties
import xyz.luchengeng.immuno.bean.RHost
import java.net.Socket
import java.util.concurrent.LinkedBlockingQueue

@Component
class PoolInit @Autowired constructor(private val env : Environment,
                                      private val poolProperties: PoolProperties,
                                      private val hostQueue: LinkedBlockingQueue<RHost>) :InitializingBean {

    override fun afterPropertiesSet() {
        var port : Int = env.getProperty("r.pool.basePort")?.toInt()?:7080
        val execR : String = env.getProperty("r.pool.rExecPath")?:"Rscript"
        val script : String = env.getProperty("r.pool.rScriptPath")?:"main.R"
        val initSize = env.getProperty("r.pool.initSize")?.toInt()?:4
        for( i in 1..(env.getProperty("r.pool.initSize")?.toInt()?:4)){
            hostQueue.add(RHost(port,execR,script,0))
            port++
        }
        GlobalScope.launch {
            while(true) {

                repeat(poolProperties.additionRequestCount){
                    val port1 = portFetch()
                    hostQueue.add(RHost(port1,execR,script,0))
                    println("Creating server at $port1")
                }
                poolProperties.additionRequestCount = 0
                delay(10)
            }
        }
        GlobalScope.launch {
            while(true){
                var idleCount = 0
                hostQueue.forEach {
                    if(it.usageFlag == 0){
                        idleCount++
                    }
                    it.usageFlag = 0
                }
                if(hostQueue.size > initSize && idleCount != 0){
                    repeat(idleCount){
                        val host = hostQueue.poll()
                        if(host != null) {
                            host.terminate()
                            launch {
                                delay(10)
                                if(!serverListening("localhost",host.port))poolProperties.freePort.add(host.port)
                            }
                        }
                    }
                }
                delay(2000)
            }
        }
    }

    private fun portFetch() : Int = poolProperties.freePort.run {
        if(this.size != 0){
            this.removeAt(0)
        }else{
                poolProperties.port++
                if(!serverListening("localhost",poolProperties.port))poolProperties.port else portFetch()
        }
    }
    fun serverListening(host: String, port: Int): Boolean {
        var s: Socket? = null
        return try {
            s = Socket(host, port)
            true
        } catch (e: Exception) {
            false
        } finally {
            if (s != null) try {
                s.close()
            } catch (e: Exception) {
            }
        }
    }
}