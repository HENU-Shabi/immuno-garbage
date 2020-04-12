package xyz.luchengeng.immuno.repo

import org.springframework.data.mongodb.repository.MongoRepository
import xyz.luchengeng.immuno.bean.Clinical
import xyz.luchengeng.immuno.bean.HumanMethylation
import xyz.luchengeng.immuno.bean.IlluminaMethylation
import xyz.luchengeng.immuno.bean.MRNAMethylation
import java.math.BigInteger

interface HumanMethylationRepo : MongoRepository<HumanMethylation,BigInteger>{
    fun findFirstBySample(sample : String) : HumanMethylation?
    fun findBySampleIsIn(samples : List<String>) :  List<HumanMethylation>
}
interface IlluminaMethylationRepo : MongoRepository<IlluminaMethylation,BigInteger>{
    fun findBySample(sample: String) : List<IlluminaMethylation>
    fun findByGeneLike(gene : String) : List<IlluminaMethylation>
    fun findByGene(gene : String) : List<IlluminaMethylation>
}
interface ClinicalRepo : MongoRepository<Clinical,BigInteger>{
    fun findByGsm(gsm : String) : List<Clinical>
}

interface MRNAMethylationRepo : MongoRepository<MRNAMethylation,BigInteger>{
    fun findFirstBySample(sample : String) : MRNAMethylation?
}