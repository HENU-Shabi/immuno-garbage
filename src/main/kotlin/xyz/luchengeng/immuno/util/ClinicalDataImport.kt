package xyz.luchengeng.immuno.util

import de.siegmar.fastcsv.reader.CsvReader
import de.siegmar.fastcsv.reader.CsvRow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import xyz.luchengeng.immuno.bean.*
import xyz.luchengeng.immuno.repo.ClinicalRepo
import xyz.luchengeng.immuno.repo.HumanMethylationRepo
import xyz.luchengeng.immuno.repo.IlluminaMethylationRepo
import java.io.File
import java.nio.charset.StandardCharsets
import java.util.regex.Pattern

@Component
class ClinicalDataImport @Autowired constructor(
        private val humanMethylationRepo: HumanMethylationRepo,
        private val illuminaMethylationRepo: IlluminaMethylationRepo,
        private val clinicalRepo: ClinicalRepo) {
    private val clinicalInfoList = listOf("gender", "race", "tnm")
    private fun enumerableMapOf(row: CsvRow, first: CsvRow) : Map<String,Enumerable> =
         mutableMapOf<String,Enumerable>().apply {
            clinicalInfoList.forEach {
                when(it){
                    "gender"->{
                        if(Gender.fromInt(dataFromColumnName("gender",first,row)?.toInt()) != null)
                            this[it] = Gender.fromInt(dataFromColumnName("gender",first,row)?.toInt()) as Enumerable
                    }
                    "race" ->{
                        if(Race.fromInt(dataFromColumnName("race",first,row)?.toInt()) != null)
                            this[it] = Race.fromInt(dataFromColumnName("race",first,row)?.toInt()) as Enumerable
                    }
                    "tnm" ->{
                        if(TNM.fromInt(dataFromColumnName("tnm",first,row)?.toInt()) != null)
                            this[it] = TNM.fromInt(dataFromColumnName("tnm",first,row)?.toInt()) as Enumerable
                    }
                }
            }
        }


    operator fun invoke(filePath: String) {
        with(CsvReader().parse(File(filePath), StandardCharsets.UTF_8)) {
            val first = this.nextRow()
            while(true){
                val row = this.nextRow()
                if(row != null) row.apply {
                    val cli =  Clinical(null, dataFromColumnName("GSM",first,row)!!, eventMapOf(first,row),enumerableMapOf(row, first))
                    clinicalRepo.save(cli)
                }else break
            }
        }
    }

    private fun dataFromColumnName(name: String, firstRow: CsvRow, targetRow: CsvRow): String?  {
        val first = firstRow.fields
        val target = targetRow.fields
        first.forEachIndexed { index, s ->
            if (s == name) {
                if(target[index] == null || target[index] == ""){
                    return null
                }
                return target[index]
            }
        }
        return null
    }

    private fun eventExtraction(firstRow: CsvRow): MutableList<String> {
        val list = mutableListOf<String>()
        val pattern = Pattern.compile("(.*)(_Event)")
        val first = firstRow.fields
        first.forEach {
            val m = pattern.matcher(it)
            if (m.find()) {
                list.add(m.group(1))
            }
        }
        return list
    }

    private fun eventMapOf(first: CsvRow, target: CsvRow) : Map<String,EventPair> =
            mutableMapOf<String,EventPair>().apply{
                val titles = eventExtraction(first)
                titles.forEach {
                    this[it] = eventPairOf(it,first,target)
            }
    }


    private fun eventPairOf(name: String, first: CsvRow, target: CsvRow): EventPair =
         EventPair(this.dataFromColumnName(name + "_Event", first, target)?.toInt(), this.dataFromColumnName(name, first, target)?.toInt())

}