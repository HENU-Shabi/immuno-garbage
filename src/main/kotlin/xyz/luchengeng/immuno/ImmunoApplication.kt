package xyz.luchengeng.immuno

import com.beust.jcommander.JCommander
import com.beust.jcommander.Parameter
import com.beust.jcommander.ParameterException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.core.env.Environment
import org.springframework.core.task.TaskExecutor
import xyz.luchengeng.immuno.repo.ClinicalRepo
import xyz.luchengeng.immuno.repo.HumanMethylationRepo
import xyz.luchengeng.immuno.repo.IlluminaMethylationRepo
import xyz.luchengeng.immuno.repo.MRNAMethylationRepo
import xyz.luchengeng.immuno.util.ClinicalDataImport
import xyz.luchengeng.immuno.util.HumanMethylationDataImport
import xyz.luchengeng.immuno.util.IlluminaMethylationDataImport
import xyz.luchengeng.immuno.util.MRNAMethylationDataImport
import java.util.*
import kotlin.system.exitProcess


class Args {
    @Parameter(names = ["--import-clinical-data"], description = "CSV files containing clinical data")
    var clinicalDataFiles: List<String> = ArrayList()

    @Parameter(names = ["--import-human-methyl"], description = "CSV files containing human methylation data")
    var humanMethylFiles: List<String> = ArrayList()

    @Parameter(names = ["--import-illumina-methyl"], description = "CSV files containing illumina methylation data")
    var illuminaMethylFiles: List<String> = ArrayList()

    @Parameter(names = ["--import-mRNA-methyl"], description = "CSV files containing mRNA methylation data")
    var mRNAMethylFiles: List<String> = ArrayList()

    @Parameter(names = ["--clear-clinical-data"], description = "delete clinical data in DB")
    var clearClinicalData : Boolean = false

    @Parameter(names = ["--clear-human-methyl"], description = "delete human methylation data in DB")
    var clearHumanMethyl : Boolean = false

    @Parameter(names = ["--clear-illumina-methyl"], description = "delete illumina methylation data in DB")
    var clearIlluminaMethyl : Boolean = false

    @Parameter(names = ["--clear-mRNA-methyl"], description = "delete mRNA methylation data in DB")
    var clearMRNAMethyl : Boolean = false

    @Parameter(names = ["--help"])
    var help = false
}

@SpringBootApplication
class ImmunoApplication @Autowired constructor(private val env : Environment,
                                               private val clinicalDataImport: ClinicalDataImport,
                                               private val clinicalRepo: ClinicalRepo,
                                               private val humanMethylationRepo: HumanMethylationRepo,
                                               private val mrnaMethylationRepo: MRNAMethylationRepo,
                                               private val humanMethylationDataImport: HumanMethylationDataImport,
                                               private val illuminaMethylationRepo: IlluminaMethylationRepo,
                                               private val illuminaMethylationDataImport: IlluminaMethylationDataImport,
                                               private val mrnaMethylationDataImport: MRNAMethylationDataImport) : CommandLineRunner {
    override fun run(vararg args: String?) {
        val argObj = Args()
        if(!env.containsProperty("spring.data.mongodb.uri")){
            throw ParameterException("MongoDB URI not set")
        }
        val cli = JCommander.newBuilder()
                .addObject(argObj)
                .build()
        cli.parse(*args)
        if(argObj.help){
            cli.usage()
            exitProcess(0)
        }
        if(argObj.clearIlluminaMethyl ||
                argObj.clearHumanMethyl ||
                argObj.clearClinicalData ||
                argObj.clearMRNAMethyl ||
                argObj.clinicalDataFiles.isNotEmpty() ||
                argObj.humanMethylFiles.isNotEmpty() ||
                argObj.illuminaMethylFiles.isNotEmpty() ||
                argObj.mRNAMethylFiles.isNotEmpty()) {
            if (argObj.clearClinicalData) {
                clinicalRepo.deleteAll()
            }
            if (argObj.clearHumanMethyl) {
                humanMethylationRepo.deleteAll()
            }
            if (argObj.clearIlluminaMethyl) {
                illuminaMethylationRepo.deleteAll()
            }
            if(argObj.clearMRNAMethyl){
                mrnaMethylationRepo.deleteAll();
            }
            argObj.clinicalDataFiles.forEach {
                clinicalDataImport(it)
            }
            argObj.humanMethylFiles.forEach {
                humanMethylationDataImport(it)
            }
            argObj.illuminaMethylFiles.forEach {
                illuminaMethylationDataImport(it)
            }
            argObj.mRNAMethylFiles.forEach {
                mrnaMethylationDataImport(it)
            }
            exitProcess(0)
        }
    }
}

fun main(args: Array<String>) {
    runApplication<ImmunoApplication>(*args)
}
