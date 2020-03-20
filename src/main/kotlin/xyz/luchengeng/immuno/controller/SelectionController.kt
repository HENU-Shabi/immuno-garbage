package xyz.luchengeng.immuno.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import xyz.luchengeng.immuno.bean.IlluminaMethylation
import xyz.luchengeng.immuno.service.SelectionService

@Controller
class SelectionController @Autowired constructor(private val selectionService: SelectionService) {
    @RequestMapping(value = ["/illumina/{gene}"],method = [RequestMethod.GET])
    fun getIllumina(@PathVariable("gene") gene : String) : ResponseEntity<List<IlluminaMethylation>> =
            ResponseEntity.ok(selectionService.searchByGene(gene))
}