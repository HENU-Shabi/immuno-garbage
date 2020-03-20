package xyz.luchengeng.immuno.bean

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigInteger

@Document
data class HumanMethylation(@Id var id : BigInteger?,
                            @Indexed val sample : String,
                            val methylationMap : Map<String,Double>)

@Document
data class IlluminaMethylation(@Id var id : BigInteger?,
                               @Indexed val sample: String,
                               @Indexed val gene : String?,
                               val chromosome : String?,
                               val chromosomeStart : Long?,
                               val chromosomeEnd: Long?)
@Document
data class Clinical(
        @Id var id : BigInteger?,
        @Indexed val gsm : String,
        val eventMap : Map<String,EventPair>,
        val enumMap: Map<String,Enumerable>
)

class EventPair(val n : Int?, val value : Int?){
    operator fun component1() = n
    operator fun component2() = value
}