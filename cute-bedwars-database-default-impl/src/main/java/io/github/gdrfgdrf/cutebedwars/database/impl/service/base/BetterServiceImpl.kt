package io.github.gdrfgdrf.cutebedwars.database.impl.service.base

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.github.dreamyoung.mprelation.ServiceImpl
import io.github.gdrfgdrf.cutebedwars.database.impl.common.Mappers

open class BetterServiceImpl<M : BaseMapper<T>, T>: ServiceImpl<M, T>() {
    var mapper: M? = null
        get() {
            if (field == null) {
                field = Mappers.getOrCreateMapper(mapperClass)
            }
            return field
        }

    fun insert(t: T): Int {
        val result = mapper?.insert(t) ?: return -1
        return result
    }
}