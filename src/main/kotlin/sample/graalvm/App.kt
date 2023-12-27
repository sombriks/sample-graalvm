package sample.graalvm

import io.javalin.Javalin

class App {
    val javalin = Javalin.create(/*config*/)
        .get("/") { ctx -> ctx.result(Foo().foo) }
}

fun main() {
    App().javalin
        .start(7070)
}
