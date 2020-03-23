package xyz.luchengeng.immuno.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import xyz.luchengeng.immuno.bean.ApiCaller
import xyz.luchengeng.immuno.bean.PoolProperties
import xyz.luchengeng.immuno.bean.RHost
import java.util.concurrent.LinkedBlockingQueue

@Configuration
class CallerConfig @Autowired constructor(private val env : Environment,
                                          private val hostQueue: LinkedBlockingQueue<RHost>,
                                          private val poolProperties: PoolProperties){
    @Bean fun caller() : ApiCaller = ApiCaller(env.getProperty("r.host",Array<String>::class.java)?: arrayOf(), env.getProperty("r.time.retry",Int::class.java)?:1,hostQueue,poolProperties)
}