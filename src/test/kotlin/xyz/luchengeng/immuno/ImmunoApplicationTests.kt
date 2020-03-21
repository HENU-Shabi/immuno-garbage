package xyz.luchengeng.immuno

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import xyz.luchengeng.immuno.repo.HumanMethylationRepo
import xyz.luchengeng.immuno.service.QueryService
import xyz.luchengeng.immuno.util.ClinicalDataImport
import xyz.luchengeng.immuno.util.HumanMethylationDataImport
import xyz.luchengeng.immuno.util.IlluminaMethylationDataImport

@SpringBootTest
class ImmunoApplicationTests @Autowired constructor(val clinicalDataImport: ClinicalDataImport,
                                                    val humanMethylationDataImport: HumanMethylationDataImport,
                                                    private val humanMethylationRepo: HumanMethylationRepo,
                                                    val queryService: QueryService,
                                                    val illuminaMethylationDataImport: IlluminaMethylationDataImport) {

    @Test
    fun contextLoads() {
        val str = queryService.listToCsv(queryService.averageValueEventQuery("OS","MIR148A"))
    }

}
