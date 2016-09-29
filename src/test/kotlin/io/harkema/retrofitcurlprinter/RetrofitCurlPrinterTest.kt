package io.harkema.retrofitcurlprinter

import okhttp3.MediaType
import okhttp3.Request
import okhttp3.RequestBody
import org.jetbrains.spek.api.Spek
import kotlin.test.assertEquals

/**
 * Created by tomas on 19-09-16.
 */

class RetrofitCurlGeneratorTest: Spek({
  val generator = RetrofitCurlGenerator()
  given("A simple URL request") {
    val request = Request.Builder()
      .url("http://httpbin.org/get")
      .build()

    it("should generate curl command") {
      assertEquals("""curl \
-X GET \
-i 'http://httpbin.org/get'""", generator.curlCommandForRequest(request))
    }
  }

  given("A simple URL request with a header") {
    val request = Request.Builder()
      .addHeader("AHeader", "withsomevalue")
      .url("http://httpbin.org/headers")
      .build()

    it("should generate curl command containing header") {
      assertEquals("""curl \
-X GET \
-H 'AHeader: withsomevalue' \
-i 'http://httpbin.org/headers'""", generator.curlCommandForRequest(request))
    }
  }

  given("A simple URL request with a multiple headers") {
    val request = Request.Builder()
      .addHeader("AHeader", "withsomevalue")
      .addHeader("ABHeader", "withsomeothervalue")
      .url("http://httpbin.org/headers")
      .build()

    it("should generate curl command containing header") {
      assertEquals("""curl \
-X GET \
-H 'ABHeader: withsomeothervalue' \
-H 'AHeader: withsomevalue' \
-i 'http://httpbin.org/headers'""", generator.curlCommandForRequest(request))
    }
  }

  given("A simple URL request with some form urlencoded Data") {
    val body = RequestBody.create(MediaType.parse("plain/text"), "aaa=bbb&ccc=ddd")
    val request = Request.Builder()
      .url("http://httpbin.org/headers")
      .post(body)
      .build()

    it("should generate curl command containing header") {
      assertEquals("""curl \
-X POST \
-H 'Content-Type: plain/text; charset=utf-8' \
-H 'Content-Length: 15' \
-d 'aaa=bbb&ccc=ddd' \
-i 'http://httpbin.org/headers'""", generator.curlCommandForRequest(request))
    }
  }

  given("A POST request with json data") {
    val body = RequestBody.create(MediaType.parse("application/json; charset=UTF-8"), "{\"a\": \"b\"}")
    val request = Request.Builder()
      .url("http://httpbin.org/headers")
      .post(body)
      .build()

    it("should also print content type header") {
      assertEquals("""curl \
-X POST \
-H 'Content-Type: application/json; charset=UTF-8' \
-H 'Content-Length: 10' \
-d '{"a": "b"}' \
-i 'http://httpbin.org/headers'""", generator.curlCommandForRequest(request))
    }
  }
})