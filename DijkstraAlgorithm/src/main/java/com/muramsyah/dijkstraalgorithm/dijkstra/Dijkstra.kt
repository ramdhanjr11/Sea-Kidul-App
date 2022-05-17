/*
 * Copyright (c) 2022.
 * Created By M Ramdhan Syahputra on 16-05-2022
 * Github : https://github.com/ramdhanjr11
 * LinkedIn : https://www.linkedin.com/in/ramdhanjr11
 * Instagram : ramdhan.official
 */

fun <T> dijkstra(graph: Graph<T>, start: T): Map<T, T?> {
    val S: MutableSet<T> = mutableSetOf() // subset dari simpul yang kita ketahui jarak sebenarnya, contoh [A, B, C, D, E]

    /*
     * variable delta mewakili jumlah dari banyaknya jalur terpendek
     * Isi pertama adalah infinity dengan Int.MAX_VALUE untuk nanti dibandingkan
     */
    val delta = graph.vertices.map { it to Double.MAX_VALUE }.toMap().toMutableMap()
    delta[start] = 0.0

    /* previous adalah variable untuk menampung path yang telah dikunjungi
     * inisialiasasi awal semua isi menjadi null
     */
    val previous: MutableMap<T, T?> = graph.vertices.map { it to null }.toMap().toMutableMap()

    // Melakukan perulangan jika S != subset simpul, contoh [A, B] != [A, B, C, D]
    while (S != graph.vertices) {
        // v adalah simpul terdekat yang belum dikunjungi
        val v: T = delta
            .filter { !S.contains(it.key) }
            .minByOrNull { it.value }!!
            .key

        graph.edges.getValue(v).minus(S).forEach { neighbor ->
            val newPath = delta.getValue(v) + graph.weights.getValue(Pair(v, neighbor))

            if (newPath < delta.getValue(neighbor)) {
                delta[neighbor] = newPath
                previous[neighbor] = v
            }
        }

        S.add(v)
    }

    return previous.toMap()
}

// fungsi untuk menampilkan shortest path dari fungsi dijkstra yang mengembalikan nilai previous path
fun <T> shortestPath(shortestPathTree: Map<T, T?>, start: T, end: T): List<T> {
    fun pathTo(start: T, end: T): List<T> {
        if (shortestPathTree[end] == null) return listOf(end)
        return listOf(pathTo(start, shortestPathTree[end]!!), listOf(end)).flatten()
    }

    return pathTo(start, end)
}
