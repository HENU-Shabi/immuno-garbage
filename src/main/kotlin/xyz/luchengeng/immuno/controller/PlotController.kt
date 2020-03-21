package xyz.luchengeng.immuno.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import xyz.luchengeng.immuno.bean.IlluminaMethylation
import xyz.luchengeng.immuno.service.PlotService
import java.math.BigInteger

@Controller
class PlotController @Autowired constructor(private val plotService: PlotService) {
    @RequestMapping(value = ["/plot/{event}/{geneId}"],method = [RequestMethod.GET])
    fun getPlot(@PathVariable("geneId") geneId : BigInteger,@PathVariable("event") event : String) : ResponseEntity<ByteArray> =
            ResponseEntity.ok(plotService.invoke(geneId,event))
}