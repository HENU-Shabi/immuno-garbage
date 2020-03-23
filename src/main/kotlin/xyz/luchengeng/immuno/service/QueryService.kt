package xyz.luchengeng.immuno.service

import de.siegmar.fastcsv.writer.CsvWriter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestHeader
import xyz.luchengeng.immuno.bean.*
import xyz.luchengeng.immuno.repo.ClinicalRepo
import xyz.luchengeng.immuno.repo.HumanMethylationRepo
import xyz.luchengeng.immuno.repo.IlluminaMethylationRepo
import xyz.luchengeng.immuno.util.IlluminaMethylationDataImport
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
    private fun averageValueEventQuery(event : String, gene : String,filter : (String)->Boolean) : List<DataFrameEntry>{
        val geneVals = mutableListOf<HumanMethylation>().apply {
            this.addAll(humanMethylationRepo.findBySampleIsIn(illuminaMethylationRepo.findByGene(gene).map { it.sample }))
        }
        val avgMap = mutableMapOf<String,Double>().apply {
            mutableMapOf<String, Double>().apply {
                geneVals.forEach { humanMethylation ->
                    humanMethylation.methylationMap.filter { filter(it.key) }.forEach { (str, double) ->
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

    private fun listToCsv(list : List<DataFrameEntry>) : String{
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
    operator fun invoke(event : String, gene : String,tnm : TNM?,gender : Gender?,race : Race?)
        = this.listToCsv(this.averageValueEventQuery(event, gene) {
            var flag = true
            if (tnm != null) {
                flag = flag && (clinicalRepo.findByGsm(it)[0].tnm == tnm)
            }
            if (gender != null) {
                flag = flag && (clinicalRepo.findByGsm(it)[0].gender == gender)
            }
            if (race != null) {
                flag = flag && (clinicalRepo.findByGsm(it)[0].race == race)
            }
            flag
        })
}