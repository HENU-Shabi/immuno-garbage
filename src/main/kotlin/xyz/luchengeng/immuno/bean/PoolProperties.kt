package xyz.luchengeng.immuno.bean

data class PoolProperties(var port : Int,
                          val freePort :MutableList<Int>,
                          var additionRequestCount : Int)