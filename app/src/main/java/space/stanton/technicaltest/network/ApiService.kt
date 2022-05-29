package space.stanton.technicaltest.network

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class ApiService<T> @Inject constructor() {
    private val builder: Retrofit.Builder = Retrofit.Builder()
        .baseUrl(BASE_API_URL)
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create()
            )
        )

    private var retrofit = builder.build()

    companion object {
        val TAG: String = ApiService::class.java.simpleName
    }

    fun <T> createService(serviceClass: Class<T>): T {
        return retrofit.create(serviceClass)
    }
}