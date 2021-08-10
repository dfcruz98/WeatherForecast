package com.example.weatherforecast.util

interface EntityMapping<I, O> {
    fun mapTo(entity: I): O
}