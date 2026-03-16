package sypg.runtogether

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RunTogetherApplication

fun main(args: Array<String>) {
    runApplication<RunTogetherApplication>(*args)
}
