package com.chicagoroboto.data

import com.chicagoroboto.model.Session
import com.chicagoroboto.model.Speaker
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue

fun DataSnapshot.toSpeaker() = Speaker(
    id = child("id").getValue<String>() ?: "",
    name = child("name").getValue<String>() ?: "",
    title = child("title").getValue<String>() ?: "",
    company = child("company").getValue<String>() ?: "",
    email = child("email").getValue<String>() ?: "",
    twitter = child("twitter").getValue<String>() ?: "",
    github = child("github").getValue<String>() ?: "",
    bio = child("bio").getValue<String>() ?: ""
)

fun DataSnapshot.toSession() = Session(
    id = child("id").getValue<String>() ?: "",
    type = child("type").getValue<String>() ?: "",
    title = child("title").getValue<String>() ?: "",
    description = child("description").getValue<String>() ?: "",
    start_time = child("start_time").getValue<String>() ?: "",
    end_time = child("end_time").getValue<String>() ?: "",
    date = child("date").getValue<String>() ?: "",
    speakers = child("speakers").getValue<List<String>>() ?: emptyList(),
    location = child("location").getValue<String>() ?: "",
    address = child("address").getValue<String>() ?: "",
    tracks = child("tracks").getValue<List<String>>() ?: emptyList()
)
