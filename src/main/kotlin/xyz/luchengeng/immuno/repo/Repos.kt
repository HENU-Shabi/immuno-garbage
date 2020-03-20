package xyz.luchengeng.immuno.repo

import org.springframework.data.mongodb.repository.MongoRepository
import xyz.luchengeng.immuno.bean.Clinical
import xyz.luchengeng.immuno.bean.HumanMethylation
import xyz.luchengeng.immuno.bean.IlluminaMethylation
import java.math.BigInteger

interface HumanMethylationRepo : MongoRepository<HumanMethylation,BigInteger>{
    fun findBySample(sample : String) : List<HumanMethylation>
}
interface IlluminaMethylationRepo : MongoRepository<IlluminaMethylation,BigInteger>{
    fun findBySample(sample: String) : List<IlluminaMethylation>
    fun findByGeneLike(gene : String) : List<IlluminaMethylation>
}
interface ClinicalRepo : MongoRepository<Clinical,BigInteger>{
    fun findByGsm(gsm : String) : List<Clinical>
}
