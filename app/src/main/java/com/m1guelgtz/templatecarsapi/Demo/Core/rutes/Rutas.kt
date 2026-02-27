package com.m1guelgtz.templatecarsapi.Demo.Core.rutes

import kotlinx.serialization.Serializable

@Serializable
object RutaLogin

@Serializable
object RutaProjectList

@Serializable
data class RutaProjectDetail(val projectId: String)
