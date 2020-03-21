package xyz.luchengeng.immuno.bean

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

class ApiCaller(private val hosts: Array<String>, private val timeRetry: Int) {
    private val hostList : List<Host> = hosts.map { Host(it) }
    class Host(private val host : String, private val lock : Lock = ReentrantLock()){
        operator fun component1() = host
        operator fun component2() = lock
    }
    data class Request(val csvAverageValueEvent : String,val gene : String,val event : String)
    class Response(host:String,request: Request){
        var plotPngByteArray: ByteArray
        init{
           plotPngByteArray =  okhttp3.OkHttpClient().newCall(okhttp3.Request.Builder().post(request.csvAverageValueEvent.toRequestBody("text/csv".toMediaType()))
                    .url("$host/plot/${request.gene}").build()).execute().body!!.bytes()
        }
    }
    class BackEndBusyException : RuntimeException()
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