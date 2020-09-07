package xyz.luchengeng.immuno.config


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import xyz.luchengeng.immuno.bean.ClasspathHostPool
import xyz.luchengeng.immuno.bean.ExternalHostPool
import xyz.luchengeng.immuno.bean.HostPool
import xyz.luchengeng.immuno.bean.unpack

@Configuration
@EnableConfigurationProperties(CallerProps::class)
class CallerAutoConfig @Autowired constructor(private val callerProps: CallerProps) {
    @Bean
    fun hostPool(): HostPool {
        return when (callerProps.scriptSource) {
            ScriptSource.EXTERNAL -> ExternalHostPool(callerProps.size, callerProps.rScriptPath, callerProps.rExecPath, callerProps.basePort)
            ScriptSource.CLASSPATH -> {
                val (init, api) = unpack(callerProps.initScript, callerProps.api)
                ClasspathHostPool(callerProps.size, init.absolutePath, api.absolutePath, callerProps.rExecPath, callerProps.basePort)
            }
        }
    }
}