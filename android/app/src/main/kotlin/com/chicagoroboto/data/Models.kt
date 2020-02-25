package com.chicagoroboto.data

import com.chicagoroboto.model.Session
import com.chicagoroboto.model.Speaker
import com.chicagoroboto.model.Venue
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue

fun DataSnapshot.toSpeaker() = Speaker(
    id = child("id").getValue<String>() ?: "",
    name = child("name").getValue<String>()?.trim() ?: "",
    title = child("title").getValue<String>()?.trim() ?: "",
    company = child("company").getValue<String>()?.trim() ?: "",
    email = child("email").getValue<String>()?.trim() ?: "",
    twitter = child("twitter").getValue<String>()?.trim() ?: "",
    github = child("github").getValue<String>()?.trim() ?: "",
    bio = child("bio").getValue<String>()?.trim() ?: ""
)

fun DataSnapshot.toSession() = Session(
    id = child("id").getValue<String>() ?: "",
    type = child("type").getValue<String>()?.trim() ?: "",
    title = child("title").getValue<String>()?.trim() ?: "",
    description = child("description").getValue<String>()?.trim() ?: "",
    start_time = child("start_time").getValue<String>()?.trim() ?: "",
    end_time = child("end_time").getValue<String>()?.trim() ?: "",
    date = child("date").getValue<String>()?.trim() ?: "",
    speakers = child("speakers").getValue<List<String>>() ?: emptyList(),
    location = child("location").getValue<String>()?.trim() ?: "",
    address = child("address").getValue<String>()?.trim() ?: "",
    tracks = child("tracks").getValue<List<String>>() ?: emptyList()
)

fun DataSnapshot.toVenue(): Venue = Venue(
    name = child("name").getValue<String>()?.trim() ?: "",
    address = child("address").getValue<String>()?.trim() ?: "",
    city = child("city").getValue<String>()?.trim() ?: "",
    state = child("state").getValue<String>()?.trim() ?: "",
    zip = child("zip").getValue<String>()?.trim() ?: "",
    phone = child("phone").getValue<String>()?.trim() ?: "",
    latitude = child("latitude").getValue<Double>() ?: 41.8339042,
    longitude = child("longitude").getValue<Double>() ?: -88.012144
)
