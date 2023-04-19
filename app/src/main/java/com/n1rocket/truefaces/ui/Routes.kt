package com.n1rocket.truefaces.ui


sealed class Routes(var route: String) {
    object Login : Routes("login")
    object Main : Routes("main")
    object Avatar : Routes("avatar")
}