package xyz.luchengeng.immuno.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import xyz.luchengeng.immuno.bean.IlluminaMethylation
import xyz.luchengeng.immuno.exception.NotFoundException
import xyz.luchengeng.immuno.repo.ClinicalRepo
import xyz.luchengeng.immuno.repo.HumanMethylationRepo
import xyz.luchengeng.immuno.repo.IlluminaMethylationRepo

@Service
class SelectionService @Autowired constructor(
        private val humanMethylationRepo: HumanMethylationRepo,
        private val illuminaMethylationRepo: IlluminaMethylationRepo,
        private val clinicalRepo: ClinicalRepo) {
    fun searchByGene(gene : String) : List<IlluminaMethylation> = illuminaMethylationRepo.findByGeneLike(gene).ifEmpty { throw NotFoundException() }
}