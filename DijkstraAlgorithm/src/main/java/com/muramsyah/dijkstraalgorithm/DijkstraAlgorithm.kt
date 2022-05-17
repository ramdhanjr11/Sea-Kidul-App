/*
 * Copyright (c) 2022.
 * Created By M Ramdhan Syahputra on 16-05-2022
 * Github : https://github.com/ramdhanjr11
 * LinkedIn : https://www.linkedin.com/in/ramdhanjr11
 * Instagram : ramdhan.official
 */

import org.json.JSONObject

class DijkstraAlgorithm private constructor(builder: Builder) : ProcessAlgorithm(
    mFile = builder.file,
    startFrom = builder.start,
    endDestination = builder.end,
    userNode = builder.userNode
) {
    lateinit var shortestPath: List<String>
    lateinit var nodes: Map<Pair<String, String>, List<Node>>
    lateinit var weights: Map<Pair<String, String>, Double>
    lateinit var routes: Map<Pair<String, String>, List<Node>>

    class Builder(var file: JSONObject) {
        lateinit var start: String
        lateinit var end: String
        lateinit var userNode: Node

        fun setStartOrigin(start: String): Builder {
            this.start = start
            return this
        }

        fun setEndDestination(end: String): Builder {
            this.end = end
            return this
        }

        fun setUserNode(userLocation: Node): Builder {
            this.userNode = userLocation
            return this
        }

        fun create(): DijkstraAlgorithm {
            return DijkstraAlgorithm(this)
        }
    }

    override fun resultShortestPath(shortestPath: List<String>) {
        this@DijkstraAlgorithm.shortestPath = shortestPath
    }

    override fun resultNodes(newNodes: Map<Pair<String, String>, List<Node>>) {
        nodes = newNodes
    }

    override fun resultWeights(newWeights: Map<Pair<String, String>, Double>) {
        weights = newWeights
    }

    override fun resultRouteMap(routes: Map<Pair<String, String>, List<Node>>) {
        this@DijkstraAlgorithm.routes = routes
    }
}