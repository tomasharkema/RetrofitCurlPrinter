package io.harkema.retrofitcurlprinter

import okhttp3.*
import okio.Buffer
import java.nio.charset.Charset

/**
 * Created by tomas on 19-09-16.
 */


private val UTF8 = Charset.forName("UTF-8")

fun FormBody.urlencodeString(): String {
    val size = size()
    return (1..size).fold("") { prev, el ->
        val name = name(el-1)
        val value = value(el-1)
        prev + if (prev == "") { "" } else { "&" } + "$name=$value"
    }
}

fun RequestBody.toCommand(): String {
    val buffer = Buffer()
    this.writeTo(buffer)
    val contentType = this.contentType()
    val charset = if (contentType != null) contentType.charset(UTF8) else UTF8

    return buffer.readString(charset)
}

class RetrofitCurlGenerator() {

    fun curlCommandForRequest(request: Request): String {

        var command = ShellCommand("curl", listOf("-X" to request.method()))

        request.headers().names().forEach { element ->
            command = command.argument("-H", "'$element: ${request.header(element)}'")
        }

        val body = request.body()
        if (body != null) {
            command = command.argument("-d" to "'${body.toCommand()}'")
        }

        command = command.argument("-i" to "'${request.url().toString()}'")

        return command.toString()
    }
}

class RetrofitCurlPrinterInterceptor(private val logger: Logger): Interceptor {
    private val generator = RetrofitCurlGenerator()
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        logger.log("==> CURL COMMAND: ${request.url()}")
        logger.log(" ${generator.curlCommandForRequest(request)}\n")
        logger.log("==> END CURL COMMAND")

        return chain.proceed(request)
    }
}
