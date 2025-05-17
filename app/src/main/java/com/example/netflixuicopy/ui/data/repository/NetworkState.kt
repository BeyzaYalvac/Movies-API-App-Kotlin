package com.example.stajproje2024.data.repository

enum class Status{
    RUNNING,
    SUCCESS,
    FAILED
}
class NetworkState (val status: Status, val msg: String){
    companion object {
        val LOADED: NetworkState
        val LOADING: NetworkState
        val ERROR: NetworkState
        val ENDOFLIST: NetworkState
        init{
            LOADED=NetworkState(Status.SUCCESS, msg = "success")
            LOADING= NetworkState(Status.RUNNING, msg = "running")
            ERROR= NetworkState(Status.FAILED, msg ="failed")
            ENDOFLIST= NetworkState(Status.FAILED, msg ="You have reached the end")
        }
    }
}