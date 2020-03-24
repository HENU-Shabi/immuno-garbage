package xyz.luchengeng.immuno.bean

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.net.ConnectException
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

class ApiCaller(private val hosts: Array<String>,
                private val timeRetry: Int,
                private val hostQueue : LinkedBlockingQueue<RHost>,
                private val poolProperties: PoolProperties) {
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
        var hostOrNull = hostQueue.poll()
        if(hostOrNull == null){
           poolProperties.additionRequestCount++
            hostOrNull = hostQueue.take()
        }
        val response : Response
        try{
              response = Response("http://127.0.0.1:${hostOrNull.port}",request)
        }catch (e : ConnectException){
            return call(request)
        }
        hostQueue.put(hostOrNull)
        return response
    }
}