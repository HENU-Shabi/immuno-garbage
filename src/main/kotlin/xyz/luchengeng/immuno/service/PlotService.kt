package xyz.luchengeng.immuno.service

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import xyz.luchengeng.callr.bean.HostPool
import xyz.luchengeng.immuno.bean.Gender
import xyz.luchengeng.immuno.bean.Race
import xyz.luchengeng.immuno.bean.TNM
import xyz.luchengeng.immuno.exception.NotFoundException
import xyz.luchengeng.immuno.repo.IlluminaMethylationRepo
import java.math.BigInteger

@Service
class PlotService @Autowired constructor(private val queryService: QueryService,
                                         private val hostPool: HostPool,
                                         private val illuminaMethylationRepo: IlluminaMethylationRepo) {
    operator fun invoke(id : BigInteger,event : String,race: Race?,tnm: TNM?,gender: Gender?) : ByteArray =
        illuminaMethylationRepo.findByIdOrNull(id).run {
            hostPool("/plot/${this?.gene?:throw NotFoundException()}"){
                post(queryService(event,this@run.gene,tnm,gender,race).toRequestBody("text/csv".toMediaType()))
            }.body!!.bytes()
        }

    fun plotLinear(gene : String,sample : String) : ByteArray {
        val ok = hostPool("/linear") {
            post(queryService.linearPlotQuery(gene, sample).toRequestBody("text/csv".toMediaType()))
        }
        return ok.body!!.bytes()
    }
}