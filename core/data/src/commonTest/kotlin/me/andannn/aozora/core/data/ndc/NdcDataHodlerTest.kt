/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.data.ndc

import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import me.andannn.aozora.core.domain.model.NDCClassification
import me.andannn.aozora.core.domain.model.NdcData
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class NdcDataHodlerTest {
    private lateinit var ndcDataHodler: NdcDataHodler
    private val testScope = TestScope()

    private val dummyNdcProvider: suspend () -> Sequence<NdcData> = {
        sequence {
            yield(NdcData(NDCClassification("0"), "Dummy NDC 0"))
            yield(NdcData(NDCClassification("01"), "Dummy NDC 001"))
            yield(NdcData(NDCClassification("02"), "Dummy NDC 002"))
            yield(NdcData(NDCClassification("03"), "Dummy NDC 003"))
            yield(NdcData(NDCClassification("020"), "Dummy NDC 020"))
            yield(NdcData(NDCClassification("021"), "Dummy NDC 021"))
            yield(NdcData(NDCClassification("022"), "Dummy NDC 022"))
        }
    }

    @BeforeTest
    fun setUp() {
        ndcDataHodler = NdcDataHodler(dummyNdcProvider)
    }

    @Test
    fun testNdcDataHolder() =
        testScope.runTest {
            ndcDataHodler
                .getNdcDataByClassification(NDCClassification("0"))
                .also { ndcData ->
                    assertTrue(ndcData?.ndcClassification?.value == "0")
                    assertTrue(ndcData.label == "Dummy NDC 0")
                }
        }

    @Test
    fun testGetChildrenOfNDC() =
        testScope.runTest {
            ndcDataHodler.getChildrenOfNDC(NDCClassification("02")).also { children ->
                assertTrue(children.size == 3)
                assertTrue(children.any { it.ndcClassification.value == "020" })
                assertTrue(children.any { it.ndcClassification.value == "021" })
                assertTrue(children.any { it.ndcClassification.value == "022" })
            }

            ndcDataHodler.getChildrenOfNDC(NDCClassification("0")).also { children ->
                assertTrue(children.size == 3)
                assertTrue(children.any { it.ndcClassification.value == "01" })
                assertTrue(children.any { it.ndcClassification.value == "02" })
                assertTrue(children.any { it.ndcClassification.value == "03" })
            }
        }
}
