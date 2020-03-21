package xyz.luchengeng.immuno.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import xyz.luchengeng.immuno.bean.ApiCaller
import xyz.luchengeng.immuno.exception.NotFoundException
import xyz.luchengeng.immuno.repo.IlluminaMethylationRepo
import java.math.BigInteger

@Service
class PlotService @Autowired constructor(private val caller: ApiCaller,
                                         private val queryService: QueryService,
                                         private val illuminaMethylationRepo: IlluminaMethylationRepo) {
    operator fun invoke(id : BigInteger,event : String) : ByteArray =
        illuminaMethylationRepo.findByIdOrNull(id).run { caller.call(ApiCaller.Request(queryService.listToCsv(queryService.averageValueEventQuery(event,this?.gene?:throw NotFoundException())),this.gene,event)).plotPngByteArray }
}