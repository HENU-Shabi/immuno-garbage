package xyz.luchengeng.immuno.service

import de.siegmar.fastcsv.writer.CsvWriter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import xyz.luchengeng.immuno.bean.DataFrameEntry
import xyz.luchengeng.immuno.bean.HumanMethylation
import xyz.luchengeng.immuno.repo.ClinicalRepo
import xyz.luchengeng.immuno.repo.HumanMethylationRepo
import xyz.luchengeng.immuno.repo.IlluminaMethylationRepo
import java.io.File
import java.io.FileReader
import java.io.StringWriter
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

@Service
class QueryService @Autowired constructor(
        private val humanMethylationRepo: HumanMethylationRepo,
        private val illuminaMethylationRepo: IlluminaMethylationRepo,
        private val clinicalRepo: ClinicalRepo) {
    fun averageValueEventQuery(event : String,gene : String) : List<DataFrameEntry>{
        val geneVals = mutableListOf<HumanMethylation>().apply {
            this.addAll(humanMethylationRepo.findBySampleIsIn(illuminaMethylationRepo.findByGene(gene).map { it.sample }))
        }
        val avgMap = mutableMapOf<String,Double>().apply {
            mutableMapOf<String, Double>().apply {
                geneVals.forEach {
                    it.methylationMap.forEach { (str, double) ->
                        this[str] = this[str] ?: 0 + double
                    }
                }
            }.filter { it.value != 0.toDouble() }.forEach{
                this[it.key] = it.value / geneVals.size
            }
        }
        val entries = mutableListOf<DataFrameEntry>().apply {
            avgMap.forEach {
                this.add(DataFrameEntry(it.key,eventName = null,eventN = null,geneValue = it.value,eventValue = null))
            }
        }
        entries.forEach {
            val clinical = clinicalRepo.findByGsm(it.gsmName)
            it.eventN = clinical[0].eventMap[event]?.n
            it.eventValue = clinical[0].eventMap[event]?.value
            it.eventName = event
        }
        entries.filter {
            it.eventN != 0 && it.eventValue != 0
        }
        return entries
    }

    fun listToCsv(list : List<DataFrameEntry>) : String{
        //val temp = File.createTempFile("dataFrame","csv")
        val temp = StringWriter()
        with(CsvWriter().append(temp)){
            this.appendLine(list[0].eventName,"${list[0].eventName}_Event","genValue")
            for(l in list){
                this.appendLine(l.eventValue.toString(),l.eventN.toString(),l.geneValue.toString())
            }
            this.flush()
            this.close()
        }
       return temp.buffer.toString()
    }
}