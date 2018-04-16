package com.chicagoroboto.data

interface ConfigProvider {
    fun getTimezoneName(callback: (String) -> Unit)
}