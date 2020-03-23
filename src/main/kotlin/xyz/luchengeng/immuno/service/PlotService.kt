package xyz.luchengeng.immuno.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import xyz.luchengeng.immuno.bean.ApiCaller
import xyz.luchengeng.immuno.bean.Gender
import xyz.luchengeng.immuno.bean.Race
import xyz.luchengeng.immuno.bean.TNM
import xyz.luchengeng.immuno.exception.NotFoundException
import xyz.luchengeng.immuno.repo.IlluminaMethylationRepo
import java.math.BigInteger

@Service
class PlotService @Autowired constructor(private val caller: ApiCaller,
                                         private val queryService: QueryService,
                                         private val illuminaMethylationRepo: IlluminaMethylationRepo) {
    operator fun invoke(id : BigInteger,event : String,race: Race?,tnm: TNM?,gender: Gender?) : ByteArray =
        illuminaMethylationRepo.findByIdOrNull(id).run { caller.call(ApiCaller.Request(queryService(event,this?.gene?:throw NotFoundException(),tnm,gender,race),this.gene,event)).plotPngByteArray }
}