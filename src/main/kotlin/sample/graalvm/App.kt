package sample.graalvm

import io.javalin.Javalin

class App {
    val app = Javalin.create(/*config*/)
        .get("/") { ctx -> ctx.result(Foo().foo) }
}

fun main() {
    App().app
        .start(7070)
}
