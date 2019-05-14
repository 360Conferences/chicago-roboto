package com.chicagoroboto.model

data class Submission(
    var title: String? = null,
    var abstract: String? = null,
    var notes: String? = null,
    var duration: Int = 0,
    var accepted: Boolean = false
) {
  var id: String? = null
}