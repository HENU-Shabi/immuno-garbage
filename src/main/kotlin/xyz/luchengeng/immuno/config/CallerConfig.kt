package xyz.luchengeng.immuno.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import xyz.luchengeng.immuno.bean.ApiCaller

@Configuration
class CallerConfig @Autowired constructor(private val env : Environment){
    fun caller() : ApiCaller = ApiCaller(env.getProperty("r.host",Array<String>::class.java)?: arrayOf(), env.getProperty("r.time.retry",Int::class.java)?:1)
}