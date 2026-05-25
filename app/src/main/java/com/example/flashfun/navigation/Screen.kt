package com.example.flashfun.navigation

sealed class Screen(val route: String) {
    object DeckList : Screen("deck_list")
    object Study : Screen("study/{setId}") {
        fun createRoute(setId: String) = "study/$setId"
    }
    object Result : Screen("result/{correct}/{total}") {
        fun createRoute(correct: Int, total: Int) = "result/$correct/$total"
    }
}
