package xyz.luchengeng.immuno.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import xyz.luchengeng.immuno.bean.PoolProperties
import xyz.luchengeng.immuno.bean.RHost
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.LinkedBlockingQueue

@Configuration
class PoolConfig@Autowired constructor(private val env : Environment) {
    @Bean
    fun hostQueue() : LinkedBlockingQueue<RHost> = LinkedBlockingQueue()
    @Bean
    fun poolProperties() = PoolProperties(env.getProperty("r.pool.basePort")?.toInt()?:7080, mutableListOf(),0)
}