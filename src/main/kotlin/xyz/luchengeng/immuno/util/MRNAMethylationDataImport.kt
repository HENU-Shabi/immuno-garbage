package xyz.luchengeng.immuno.util

import de.siegmar.fastcsv.reader.CsvReader
import de.siegmar.fastcsv.reader.CsvRow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import xyz.luchengeng.immuno.bean.HumanMethylation
import xyz.luchengeng.immuno.bean.MRNAMethylation
import xyz.luchengeng.immuno.repo.ClinicalRepo
import xyz.luchengeng.immuno.repo.HumanMethylationRepo
import xyz.luchengeng.immuno.repo.IlluminaMethylationRepo
import xyz.luchengeng.immuno.repo.MRNAMethylationRepo
import java.io.File
import java.nio.charset.StandardCharsets
import java.util.regex.Pattern


@Component
open class MRNAMethylationDataImport @Autowired constructor(
        private val mRNAMethylationRepo: MRNAMethylationRepo
        ) {

    operator fun invoke(filePath: String) : Unit {
        with(CsvReader().parse(File(filePath),StandardCharsets.UTF_8)) {
            val first = this.nextRow()
            while(true){
                val row = this.nextRow()
                if(row != null) row.apply {
                    val hm = MRNAMethylation(null,row.getField(0),valueMapOf(first,row));
                    mRNAMethylationRepo.save(hm)
                }else break
            }
        }
    }
}
