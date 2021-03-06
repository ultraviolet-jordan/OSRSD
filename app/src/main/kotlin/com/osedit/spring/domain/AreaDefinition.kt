package com.osedit.spring.domain

import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class AreaDefinition(@Id private val id: Int? = 0): Definition {
    var field3292: IntArray? = null
    var spriteId: Int = -1
    var field3294: Int = -1
    var name: String? = null
    var tileHash = 0
    var field3297: Int = -1
    var field3298: Array<String?> = arrayOfNulls(5)
    var field3300: IntArray? = null
    var field3308: String? = null
    var field3309: ByteArray? = null
    var field3310 = 0
}