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
                               val genomicRegion : String?,
                               val methylationPosition : String?,
                               val relationToIsland: String?)
@Document
data class Clinical(
        @Id var id : BigInteger?,
        @Indexed val gsm : String,
        val eventMap : Map<String,EventPair>,
        val gender: Gender?,
        val tnm: TNM?,
        val race: Race?,
        val radiationTherapy: RadiationTherapy?,
        val recurrence: Recurrence?,
        val metastasis: Metastasis?
)

class EventPair(val n : Int?, val value : Int?){
    operator fun component1() = n
    operator fun component2() = value
}

data class DataFrameEntry(val gsmName : String,
                          var eventName : String?,
                          var eventN : Int?,
                          var eventValue : Int?,
                          val geneValue : Double){
    constructor(geneName: String,eventName : String, eventPair: EventPair,geneValue: Double) : this(geneName,eventName,eventPair.n!!, eventPair.value!!, geneValue)
}