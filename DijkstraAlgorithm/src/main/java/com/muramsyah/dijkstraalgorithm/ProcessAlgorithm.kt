/*
 * Copyright (c) 2022.
 * Created By M Ramdhan Syahputra on 16-05-2022
 * Github : https://github.com/ramdhanjr11
 * LinkedIn : https://www.linkedin.com/in/ramdhanjr11
 * Instagram : ramdhan.official
 */

@file:Suppress("LeakingThis")

import org.json.JSONObject

abstract class ProcessAlgorithm(mFile: JSONObject, startFrom: String, endDestination: String, userNode: Node) {

    init {
        // data from result of data-saw which was counted by saw method in excel by Ramdhan
        val baseOfFilteringSaw = mapOf(
            Pair("v1", "v2") to 36.2656,
            Pair("v2", "v3") to 42.7383,
            Pair("v2", "v30") to 37.5134,
            Pair("v3", "v4") to 58.5482,
            Pair("v3", "v5") to 38.1423,
            Pair("v4", "v6") to 35.2272,
            Pair("v4", "v8") to 19.1127,
            Pair("v5", "v6") to 42.4622,
            Pair("v5", "v9") to 42.2893,
            Pair("v6", "v7") to 41.0782,
            Pair("v6", "v14") to 20.3663,
            Pair("v7", "v8") to 31.3121,
            Pair("v9", "v10") to 19.7981,
            Pair("v9", "v11") to 26.0924,
            Pair("v10", "v11") to 32.8161,
            Pair("v10", "v12") to 31.9120,
            Pair("v11", "v56") to 28.2103,
            Pair("v12", "v56") to 24.8310,
            Pair("v14", "v13") to 19.3630,
            Pair("v14", "v15") to 26.8924,
            Pair("v13", "v16") to 28.007,
            Pair("v15", "v17") to 13.6294,
            Pair("v15", "v16") to 38.7913,
            Pair("v16", "v17") to 32.6332,
            Pair("v56", "v57") to 59.1899,
            Pair("v56", "v58") to 51.9781,
            Pair("v57", "v58") to 64.6815,
            Pair("v58", "v59") to 32.4985,
            Pair("v59", "v60") to 21.2612,
            Pair("v59", "v61") to 13.2203,
            Pair("v61", "v19") to 72.8690,
            Pair("v17", "v18") to 34.9624,
            Pair("v17", "v61") to 31.8778,
            Pair("v18", "v20") to 30.6276,
            Pair("v18", "v25") to 34.4662,
            Pair("v19", "v20") to 23.6507,
            Pair("v19", "v21") to 36.9811,
            Pair("v20", "v18") to 30.6276,
            Pair("v20", "v23") to 28.3031,
            Pair("v20", "v22") to 42.3748,
            Pair("v21", "v22") to 62.4020,
            Pair("v21", "v44") to 23.7233,
            Pair("v22", "v20") to 42.3748,
            Pair("v22", "v24") to 52.7302,
            Pair("v22", "v46") to 19.6493,
            Pair("v23", "v24") to 28.8189,
            Pair("v23", "v26") to 42.1102,
            Pair("v25", "v26") to 78.7324,
            Pair("v25", "v28") to 25.4698,
            Pair("v26", "v27") to 53.3484,
            Pair("v26", "v25") to 78.7324,
            Pair("v27", "v26") to 53.3484,
            Pair("v27", "v28") to 32.1003,
            Pair("v27", "v53") to 56.8477,
            Pair("v28", "v29") to 45.5241,
            Pair("v29", "v55") to 62.3534,
            Pair("v29", "v54") to 29.6839,
            Pair("v30", "v31") to 42.8076,
            Pair("v30", "v33") to 27.5286,
            Pair("v31", "v32") to 18.4661,
            Pair("v31", "v60") to 41.8804,
            Pair("v32", "v37") to 16.2205,
            Pair("v32", "v33") to 29.4262,
            Pair("v33", "v34") to 34.5258,
            Pair("v33", "v36") to 15.6944,
            Pair("v34", "v35") to 48.1100,
            Pair("v35", "v36") to 43.7854,
            Pair("v36", "v43") to 8.2756,
            Pair("v37", "v38") to 38.7985,
            Pair("v37", "v40") to 21.3216,
            Pair("v38", "v39") to 29.9508,
            Pair("v39", "v40") to 36.1122,
            Pair("v40", "v41") to 16.4172,
            Pair("v41", "v42") to 26.9823,
            Pair("v41", "v43") to 35.4692,
            Pair("v42", "v21") to 28.7655,
            Pair("v43", "v41") to 35.4692,
            Pair("v43", "v44") to 31.8804,
            Pair("v60", "v42") to 51.8552,
            Pair("v44", "v21") to 23.7233,
            Pair("v44", "v45") to 45.5789,
            Pair("v45", "v46") to 50.2140,
            Pair("v45", "v47") to 33.0338,
            Pair("v46", "v22") to 19.6493,
            Pair("v46", "v50") to 33.0617,
            Pair("v46", "v47") to 61.1926,
            Pair("v47", "v48") to 60.9859,
            Pair("v48", "v50") to 21.2982,
            Pair("v48", "v49") to 37.9029,
            Pair("v49", "v51") to 12.5367,
            Pair("v50", "v53") to 29.7003,
            Pair("v53", "v27") to 56.8477,
            Pair("v52", "v54") to 24.0134,
            Pair("v54", "v55") to 45.0589,
            Pair("v54", "v29") to 29.6839,
            Pair("v27", "v24") to 72.1449
        )

        // Menambah data map kebalikan (v1 - v2) -> (v2 - v1)
        val reverseFilteringSaw = baseOfFilteringSaw.map { (key, value) ->
            Pair(key.second, key.first) to value
        }

        val resultOfFiltering = baseOfFilteringSaw + reverseFilteringSaw
        val weights = processFile(mFile)

        // mendapatkan vertex yang akan dilalui terbaru dari lokasi user
        val newNodes = calculateAllNodes(
            start = startFrom,
            graph = GraphHaversine(weights),
            latUser = userNode.lat,
            longUser = userNode.long
        ) + weights

        // melakukan konversi node ke dalam haversine formula
        val processWeights = newNodes.entries
            .map { (k, _) ->
                k to haversine(
                    newNodes[k]?.get(0)!!.lat,
                    newNodes[k]?.get(0)!!.long,
                    newNodes[k]?.get(newNodes[k]?.size?.minus(1) ?: 0)!!.lat,
                    newNodes[k]?.get(newNodes[k]?.size?.minus(1) ?: 0)!!.long,
                )
            }

        // Membalikan keaadaan node (v1 - v2) -> (v2 - v1)
        val reverseNewNodes = newNodes.map { (key, value) ->
            Pair(key.second, key.first) to value
        }

        // Menyatukan node yang telah dibalikan dengan yang belum
        val resultNewNodes = newNodes + reverseNewNodes

        /**
         * Melakukan pengecekan apabila data yang ada didalam variable resultNewNodes
         * sama dengan yang ada didalam variable resultOfFiltering maka isikan nilai data saw
         * yang telah diproses didalam excel(variable resultOfFiltering) ke masing masing variable yang sama
         */
        val filterNewNodes = resultNewNodes.map {
            when (it.key) {
                in resultOfFiltering.keys -> {
                    val filterKeys = resultOfFiltering.keys.filter { key -> key == it.key }
                    filterKeys.associateWith { key ->
                        resultOfFiltering[key]
                    }
                }
                newNodes.keys.first() -> {
                    mapOf(it.key to 0.0)
                }
                else -> {
                    mapOf(it.key to Double.MAX_VALUE)
                }
            }
        }

        println(
            """
            Debugging :  
            $newNodes
            $resultNewNodes    
            $filterNewNodes
        """.trimIndent()
        )

        println()

        val resultFilterNewNodes = HashMap<Pair<String, String>, Double>()

        // Proses pengeluaran data dari List<Map<Pair<String, String>>> menjadi Map<Pair<Pair<String, String>>>
        repeat(filterNewNodes.size) {
            val elementOfFilter = filterNewNodes[it].entries.take(1)
            resultFilterNewNodes[
                    Pair(
                        elementOfFilter.first().key.first,
                        elementOfFilter.first().key.second
                    )
            ] = elementOfFilter.first().value!!
        }

        val start = processWeights[0].first.first
        val shortestPathTree = dijkstra(Graph(resultFilterNewNodes), start)
        val shortestPath = shortestPath(shortestPathTree, start, endDestination)

        /**
         * Melakukan logic terhadap hasil shortest path yang dihasilkan dari variable shortestPath
         * [v32, v33, v30, v2, v3, v4, v8] -> (v32, v33), (v33, v30) dan seterusnya
         */
        val mapNodes = HashMap<Pair<String, String>, List<Node>>()
        for (i in shortestPath.indices) {
            if (i != shortestPath.size - 1) {
                mapNodes[
                        Pair(
                            shortestPath[i],
                            shortestPath[i + 1]
                        )] = resultNewNodes.filterKeys {
                    it == Pair(
                        shortestPath[i],
                        shortestPath[i + 1]
                    )
                }.values.flatten()
            }
        }

        resultNodes(newNodes)
        resultWeights(resultFilterNewNodes)
        resultShortestPath(shortestPath)
        resultRouteMap(mapNodes)
    }

    private fun processFile(mFile: JSONObject): HashMap<Pair<String, String>, ArrayList<Node>> {
        val jsonData = mFile.getJSONArray("features")

        val weights = HashMap<Pair<String, String>, ArrayList<Node>>()

        for (data in 0 until jsonData.length()) {
            val dataObject = jsonData.getJSONObject(data)
            val dataNodes = dataObject.getString("nodes")
            val dataGeometry = dataObject.getJSONObject("geometry")
            val dataCoordinates = dataGeometry.getJSONArray("coordinates")

            val fromTo = dataNodes.split("-").toTypedArray()

            val pairFromTo = Pair(fromTo[0], fromTo[1])

            val nodes = arrayListOf<Node>()

            for (coordinate in 0 until dataCoordinates.length()) {
                val node = Node(
                    dataCoordinates.getJSONArray(coordinate).getDouble(1),
                    dataCoordinates.getJSONArray(coordinate).getDouble(0)
                )
                nodes.add(node)
            }

            weights[pairFromTo] = nodes
        }

        return weights
    }

    abstract fun resultShortestPath(shortestPath: List<String>)

    abstract fun resultNodes(newNodes: Map<Pair<String, String>, List<Node>>)

    abstract fun resultWeights(newWeights: Map<Pair<String, String>, Double>)

    abstract fun resultRouteMap(routes: Map<Pair<String, String>, List<Node>>)
}