package space.stanton.technicaltest

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody


object ApiCalls {

    fun loadAll(callBack: (Pair<ResponseBody?, Throwable?>) -> Unit) {
        val url = "https://jsonplaceholder.typicode.com/posts"
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        try {
            val response = client.newCall(request).execute()
            callBack(Pair(response.body, null))
        } catch (e: Throwable) {
            callBack(Pair<ResponseBody?, Throwable?>(null, e))
        }
    }

    fun getPostById(id: Int, callBack: (Pair<ResponseBody?, Throwable?>) -> Unit) {
        val url = "https://jsonplaceholder.typicode.com/posts/$id"
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        try {
            val response = client.newCall(request).execute()
            callBack(Pair(response.body, null))
        } catch (e: Throwable) {
            callBack(Pair<ResponseBody?, Throwable?>(null, e))
        }
    }

    fun getCommentsByPostId(id: String, callBack: (Pair<ResponseBody?, Throwable?>) -> Unit) {
        //TODO
    }
}