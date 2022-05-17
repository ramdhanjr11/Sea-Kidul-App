/*
 * Copyright (c) 2022.
 * Created By M Ramdhan Syahputra on 16-05-2022
 * Github : https://github.com/ramdhanjr11
 * LinkedIn : https://www.linkedin.com/in/ramdhanjr11
 * Instagram : ramdhan.official
 */

import kotlin.math.*

// nilai default radius bumi
const val earthRadiusKm: Double = 6372.8

// method untuk melakukan perhitungan dengan metode haversine
fun haversine(latDest: Double, longDest: Double, latUser: Double, longUser: Double): Double {
    val dLat = Math.toRadians(latDest - latUser)
    val dLon = Math.toRadians(longDest - longUser)
    val originLat = Math.toRadians(latUser)
    val destinationLat = Math.toRadians(latDest)

//    println("origin lat : ${Math.toRadians(latUser)}")
//    println("origin long : ${Math.toRadians(longUser)}")
//    println("destination lat : ${Math.toRadians(latDest)}")
//    println("destination long : ${Math.toRadians(longDest)}")
//    println("lat - lat : $dLat")
//    println("long - long : $dLon")
//
//    println()

    val a = sin(dLat / 2).pow(2.toDouble()) + sin(dLon / 2).pow(2.toDouble()) * cos(originLat) * cos(destinationLat)
    val c = 2 * asin(sqrt(a))
    return earthRadiusKm * c
}

// method untuk mencari node terdekat dari posisi user
fun searchCloseDistance(nodes: List<Node>, latUser: Double = 106.8687474144803, longUser: Double = -6.252221922130799): Map<Int, Double> {
    val result = HashMap<Int, Double>()
    nodes.forEachIndexed { index, node ->
        result[index] = haversine(
            latDest = node.lat,
            longDest = node.long,
            latUser =  latUser,
            longUser = longUser
        )
    }
    return result
}

// method untuk melakukan perhitungan terhadap semua nodes dan menghasilkan nilai nodes baru
fun <T> calculateAllNodes(
    start: T,
    graph: GraphHaversine<T>,
    latUser: Double,
    longUser: Double
): Map<Pair<String, T>, List<Node>> {
    // 1
    val S: MutableSet<T> = mutableSetOf()

    // 2
    val delta = graph.vertices.associateWith { Double.MAX_VALUE }.toMutableMap()
    delta[start] = 0.0

    val trackNodes = HashMap<Pair<T, T>, Pair<Int, Double>>()

    // 3
    while (S != graph.vertices) {
        // 4
        // Mengambil nilai terkecil dari variable delta
        val k: T = delta
            .filter { !S.contains(it.key) } // Melakukan filtering terhadap nilai yang terdapat didalam variable delta selain dari nilai yang ada didalam variabel S (yang sedang dijalankan sekarang)
            .minByOrNull { it.value }!! // Mencari nilai terkecil, jika tidak maka null
            .key

        // 5
        graph.edges.getValue(k).minus(S).forEach { neighbor ->
            val values = graph.weights[Pair(k, neighbor)]
            val closePath = searchCloseDistance(values!!, latUser, longUser)
            val valuesSorted = closePath.values.sorted()[0]
            val indexOfValue = closePath.entries
                .associate { (key, value) -> value to key }[valuesSorted]

            // 6
            trackNodes[Pair(k, neighbor)] = Pair(indexOfValue!!, valuesSorted)
        }

        // 7
        S.add(k)
    }

//    println("S: $S")
//    println("trackNodes : $trackNodes")

    // 8
    val sortedResult = trackNodes.map { it }.sortedBy { it.value.second }
    val from = sortedResult[0].key.first
    val to = sortedResult[0].key.second
    val indexValue = sortedResult[0].value.first

//    println("sortedResult : $sortedResult")
//    println("""
//        from : $from
//        to   : $to
//        index : $indexValue
//        value : $valueResult
//    """.trimIndent())

    val resultFromToNode = HashMap<Pair<String, T>, Double>()
    val resultAll = HashMap<Pair<String, T>, List<Node>>()

    when (sortedResult[0].value.first) {
        0 -> {
            val fromFirstNodeToLast = graph.weights[Pair(from, to)]!!
            resultAll[Pair(from as String, to)] = fromFirstNodeToLast
//            println("new node = ${sortedResult[0].key.first}")
        }
        sortedResult[sortedResult.size-1].value.first -> {
            val fromFirstNodeToLast = graph.weights[Pair(from, to)]!!
            resultAll[Pair(from as String, to)] = fromFirstNodeToLast
//            println("new node = ${sortedResult[sortedResult.size-1].key.first}")
        }
        else -> {
            val valueFromIndex = graph.weights[Pair(from, to)]?.get(indexValue)

            val valueFromFirstNode = graph.weights[Pair(from, to)]?.get(0)

            val valueSize = graph.weights[Pair(from, to)]?.size!!

            val valueFromLastNode = graph.weights[Pair(from, to)]?.get(valueSize-1)

//            println("""
//                first node : $valueFromFirstNode : ${graph.weights[Pair(from, to)]?.get(0)}
//                last node : $valueFromLastNode : ${graph.weights[Pair(from, to)]?.get(valueSize-1)}
//                result node : $valueFromIndex
//            """.trimIndent())

            val closeDistanceToFirstNode = haversine(
                latDest = valueFromFirstNode!!.lat,
                longDest = valueFromFirstNode.long,
                latUser = valueFromIndex!!.lat,
                longUser = valueFromIndex.long
            )

            val closeDistanceToLastNode = haversine(
                latDest = valueFromLastNode!!.lat,
                longDest = valueFromLastNode.long,
                latUser = valueFromIndex.lat,
                longUser = valueFromIndex.long
            )

//            println("""
//                closeDistanceFirstNode : $closeDistanceToFirstNode
//                closeDistanceLastNode : $closeDistanceToLastNode
//            """.trimIndent())

            val fromToFirstNode = graph.weights[Pair(from, to)]!!.slice(0..indexValue)
            val fromToLastNode = graph.weights[Pair(from, to)]!!.slice(indexValue until graph.weights[Pair(from, to)]!!.size)

            resultFromToNode[Pair("first", sortedResult[0].key.first)] = closeDistanceToFirstNode
            resultFromToNode[Pair("first", sortedResult[0].key.second)] = closeDistanceToLastNode

//            println("resultFromToNode : $resultFromToNode")
//            println("fromToFirstNode : $fromToFirstNode")
//            println("fromToLastNode : $fromToLastNode")

            resultAll[Pair("first", sortedResult[0].key.first)] = fromToFirstNode
            resultAll[Pair("first", sortedResult[0].key.second)] = fromToLastNode
        }
    }

//    println("resultAll : $resultAll ")
//    println("resultAllSize : ${resultAll.size}")

    return resultAll
}
