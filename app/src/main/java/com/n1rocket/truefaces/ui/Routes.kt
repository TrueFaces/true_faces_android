package com.n1rocket.truefaces.ui


sealed class Routes(var route: String, var title: String) {
    object Login : Routes("login", title = "Login")
    object Main : Routes("main", title = "Main")
    object Upload : Routes("upload", title = "Upload")
}