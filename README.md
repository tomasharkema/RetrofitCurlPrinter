# RetrofitCurlPrinter
A interceptor for Retrofit to print curl commands

[![](https://jitpack.io/v/tomasharkema/RetrofitCurlPrinter.svg)](https://jitpack.io/#tomasharkema/RetrofitCurlPrinter)

## Print your request as a curl command!

- `HttpLoggingInterceptor`
```
D/OkHttp: --> GET http://httpbin.org http/1.1
D/OkHttp: --> END GET
```

- `RetrofitCurlPrinterInterceptor`
```shell
D/RetrofitCurlPrinter: ==> CURL COMMAND: http://httpbin.org
D/RetrofitCurlPrinter: curl -X GET \
                       -i 'http://httpbin.org'
D/RetrofitCurlPrinter: ==> END CURL COMMAND
```

## Instructions:

- Create your own `OkHttpClient` and `Retrofit` instance
```kotlin
val httpClient = OkHttpClient.Builder()

val retrofit = Retrofit.Builder()
  .baseUrl("http://httpbin.org")
  .client(httpClient.build())
```

- Import the project

build.gradle
```gradle
// Add the dependency
dependencies {
  compile 'com.github.tomasharkema:RetrofitCurlPrinter:0.0.1'
}

// Import jitpack
repositories {
  mavenCentral()
  maven { url "https://jitpack.io" }
}
```

- Attach the `RetrofitCurlPrinterInterceptor`
```kotlin
val httpClient = OkHttpClient.Builder()

httpClient.addInterceptor(RetrofitCurlPrinterInterceptor(object: Logger {
  override fun log(message: String) {
    info { message }
  }
}))

val retrofit = Retrofit.Builder()
  .baseUrl("http://httpbin.org")
  .client(httpClient.build())
```

- Win! 🎉
