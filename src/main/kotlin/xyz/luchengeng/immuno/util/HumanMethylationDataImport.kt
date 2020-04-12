package xyz.luchengeng.immuno.util

import de.siegmar.fastcsv.reader.CsvReader
import de.siegmar.fastcsv.reader.CsvRow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import xyz.luchengeng.immuno.bean.HumanMethylation
import xyz.luchengeng.immuno.repo.ClinicalRepo
import xyz.luchengeng.immuno.repo.HumanMethylationRepo
import xyz.luchengeng.immuno.repo.IlluminaMethylationRepo
import java.io.File
import java.nio.charset.StandardCharsets
import java.util.regex.Pattern


@Component
open class HumanMethylationDataImport @Autowired constructor(
        private val humanMethylationRepo: HumanMethylationRepo) {

    operator fun invoke(filePath: String) : Unit {
        with(CsvReader().parse(File(filePath),StandardCharsets.UTF_8)) {
            val first = this.nextRow()
            while(true){
                val row = this.nextRow()
                if(row != null) row.apply {
                    val hm = HumanMethylation(null,row.getField(0),valueMapOf(first,row));
                    humanMethylationRepo.save(hm)
                }else break
            }
        }
    }


}

fun methylationDataFromColumnName(name: String, firstRow: CsvRow, targetRow: CsvRow): Double?  {
    val first = firstRow.fields
    val target = targetRow.fields
    first.forEachIndexed{ index,item->
        if (item == name) {
            if(target[index] == null || target[index] == ""){
                return null
            }
            return if(target[index] == "NA"){
                null
            }else{
                target[index].toDouble()
            }
        }
    }
    return null
}

fun typeExtraction(firstRow: CsvRow): MutableList<String> {
    val list = mutableListOf<String>()
    val first = firstRow.fields
    val pattern = Pattern.compile("(.*)(-01)")
    first.forEach {
        val m = pattern.matcher(it)
        if (m.find()) {
            list.add(m.group(1))
        }
    }
    return list
}

fun valueMapOf(first: CsvRow, target: CsvRow) : Map<String, Double> =
        mutableMapOf<String, Double>().apply {
            val titles = typeExtraction(first)
            titles.forEach {
                if(methylationDataFromColumnName("$it-01", first, target) != null)
                    this[it] =  methylationDataFromColumnName("$it-01", first, target)!!
            }
        }