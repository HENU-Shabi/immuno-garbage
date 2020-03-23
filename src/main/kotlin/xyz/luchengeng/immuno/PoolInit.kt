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
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.LinkedBlockingQueue
import javax.annotation.PostConstruct
import javax.xml.bind.JAXBElement

@Component
class PoolInit @Autowired constructor(private val env : Environment,
                                      private val poolProperties: PoolProperties,
                                      private val hostQueue: LinkedBlockingQueue<RHost>) :InitializingBean {

    override fun afterPropertiesSet() {
        var port : Int = env.getProperty("r.pool.basePort")?.toInt()?:7080
        val execR : String = env.getProperty("r.pool.rExecPath")?:"Rscript"
        val script : String = env.getProperty("r.pool.rScriptPath")?:"main.R"
        for( i in 1..(env.getProperty("r.pool.initSize")?.toInt()?:4)){
            hostQueue.add(RHost(port,execR,script,0))
            port++
        }
        GlobalScope.launch {
            while(true) {
                var idleCount = 0
                hostQueue.forEach {
                    if(it.usageFlag == 0){
                        idleCount++
                    }
                    it.usageFlag = 0
                }
                if(hostQueue.size > 4 && idleCount != 0){
                    repeat(idleCount){
                        val host = hostQueue.poll()
                        if(host != null) poolProperties.freePort.add(host.port)
                    }
                }
                repeat(poolProperties.additionRequestCount){
                    hostQueue.add(RHost(portFetch(),execR,script,0))
                }
                poolProperties.additionRequestCount = 0
                delay(10)
            }
        }
    }

    private fun portFetch() : Int = poolProperties.freePort.run {
        if(this.size != 0){
            this.removeAt(0)
        }else{
                poolProperties.port++
                poolProperties.port
        }
    }
}