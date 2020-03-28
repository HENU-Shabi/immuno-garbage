package xyz.luchengeng.immuno.util

import de.siegmar.fastcsv.reader.CsvReader
import de.siegmar.fastcsv.reader.CsvRow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import xyz.luchengeng.immuno.bean.IlluminaMethylation
import xyz.luchengeng.immuno.repo.ClinicalRepo
import xyz.luchengeng.immuno.repo.HumanMethylationRepo
import xyz.luchengeng.immuno.repo.IlluminaMethylationRepo
import java.io.File
import java.nio.charset.StandardCharsets

@Component
class IlluminaMethylationDataImport @Autowired constructor(
        private val humanMethylationRepo: HumanMethylationRepo,
        private val illuminaMethylationRepo: IlluminaMethylationRepo,
        private val clinicalRepo: ClinicalRepo) {
    private val propToColumnMap = mapOf("sample" to "#id",
            "gene" to "gene",
            "genomicRegion" to "genomicRegion",
            "methylationPosition" to "methylationPosition",
            "relationToIsland" to "relationToIsland")
    private fun dataFromColumnName(name: String, firstRow: CsvRow, targetRow: CsvRow): String?  {
        val first = firstRow.fields
        val target = targetRow.fields
        first.forEachIndexed{ index,item->
            if (item == name) {
                if(target[index] == null || target[index] == "") return null
                println(target[index])
                return target[index]
            }
        }
        return null
    }
    operator fun invoke(filePath: String) {
        with(CsvReader().parse(File(filePath), StandardCharsets.UTF_8)) {
            val first = this.nextRow()
            while(true){
                val row = this.nextRow()
                if(row != null) row.apply {
                    val cli =  IlluminaMethylation(null,
                            dataFromColumnName(propToColumnMap["sample"]!!,first,this)!!,
                            dataFromColumnName(propToColumnMap["gene"]!!,first,this),
                            dataFromColumnName(propToColumnMap["genomicRegion"]!!,first,this),
                            dataFromColumnName(propToColumnMap["methylationPosition"]!!,first,this),
                            dataFromColumnName(propToColumnMap["relationToIsland"]!!,first,this))
                    illuminaMethylationRepo.save(cli)
                }else break
            }
        }
    }
}