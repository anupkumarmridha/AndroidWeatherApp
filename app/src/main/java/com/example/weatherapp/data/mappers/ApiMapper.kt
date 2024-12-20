package com.example.weatherapp.data.mappers

interface ApiMapper<Domain, Entity> {
    fun mapToDomain(apiEntity: Entity): Domain
}