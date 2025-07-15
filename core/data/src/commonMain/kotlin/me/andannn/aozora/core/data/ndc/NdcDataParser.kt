package me.andannn.aozora.core.data.ndc

import aosora.core.data.generated.resources.Res
import kotlinx.coroutines.sync.Mutex
import kotlinx.io.Buffer
import kotlinx.io.readString
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import me.andannn.aozora.core.domain.model.NDCClassification
import me.andannn.aozora.core.domain.model.NDCType
import me.andannn.aozora.core.domain.model.NdcData
import me.andannn.aozora.core.domain.model.isChildOf
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
private suspend fun bundledNdcData(): ByteArray = Res.readBytes("files/ndc/ndc_catalog.json")

@Serializable
private data class NdcNode(
    val notation: String,
    val type: String,
    val label: String,
)

private fun getNdcDataSequence(rawJson: ByteArray): Sequence<NdcData> =
    sequence {
        val string =
            Buffer()
                .apply {
                    write(rawJson)
                }.use {
                    it.readString()
                }

        val ndcNodes = Json.decodeFromString<List<NdcNode>>(string)
        for (node in ndcNodes) {
            val ndcClassification = NDCClassification(node.notation)
            val label = node.label
            yield(NdcData(ndcClassification, label))
        }
    }

private suspend fun getNdcDataSequence(): Sequence<NdcData> {
    val rawJson = bundledNdcData()
    return getNdcDataSequence(rawJson)
}

internal class NdcDataHodler(
    private val ndcDataProvider: suspend () -> Sequence<NdcData> = ::getNdcDataSequence,
) {
    private var ndcDataMap: Map<NDCClassification, NdcData>? = null

    private val mutex = Mutex()

    suspend fun getNdcDataByClassification(ndcClassification: NDCClassification): NdcData? =
        withMap { map ->
            map[ndcClassification]
        }

    suspend fun getChildrenOfNDC(ndcClassification: NDCClassification): List<NdcData> =
        withMap { map ->
            if (ndcClassification.ndcType == NDCType.SECTION) {
                return@withMap emptyList()
            }

            map.filter { it.key.isChildOf(ndcClassification) }.values.toList()
        }

    private suspend fun <T> withMap(block: (Map<NDCClassification, NdcData>) -> T): T {
        mutex.lock()

        ensureInitialized()
        return try {
            block(ndcDataMap!!)
        } finally {
            mutex.unlock()
        }
    }

    private suspend fun ensureInitialized() {
        if (ndcDataMap != null) return

        val sequence = ndcDataProvider.invoke()
        ndcDataMap = sequence.associateBy { it.ndcClassification }
    }
}
