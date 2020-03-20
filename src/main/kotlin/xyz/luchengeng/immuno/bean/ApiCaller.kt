package xyz.luchengeng.immuno.bean

import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

class ApiCaller(private val hosts: Array<String>, private val timeRetry: Int) {
    private val hostList : List<Host> = hosts.map { Host(it) }
    class Host(private val host : String, private val lock : Lock = ReentrantLock()){
        operator fun component1() = host
        operator fun component2() = lock
    }
    class Request
    class Response(host:String,request: Request){
        init{

        }
    }
    class BackEndBusyException : Exception()
    fun call(request: Request):Response{
        for( i in 1..timeRetry) {
            for ((host, lock) in hostList) {
                if (lock.tryLock()) {
                    val response = Response(host, request)
                    lock.unlock()
                    return response
                }
            }
        }
        throw BackEndBusyException()
    }
}